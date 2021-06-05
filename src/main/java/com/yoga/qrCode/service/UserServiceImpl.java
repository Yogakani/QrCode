package com.yoga.qrCode.service;

import com.yoga.qrCode.agent.AgentService;
import com.yoga.qrCode.model.entity.Batch;
import com.yoga.qrCode.model.entity.Customer;
import com.yoga.qrCode.model.entity.UserGroup;
import com.yoga.qrCode.model.request.AuthByQrCodeRequest;
import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.UserResponse;
import com.yoga.qrCode.repository.BatchRepository;
import com.yoga.qrCode.repository.CustomerRepository;
import com.yoga.qrCode.repository.UserGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

import static com.yoga.qrCode.utils.ModelTransformer.*;

@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private AgentService agentService;

    @Override
    public boolean createNewUser(UserRequest userRequest) {
        Customer customer = customerRepository.save(userToCustomerModel(userRequest, this::generateTempPass).get());
        if (customer.getId() > 0) {
            log.info("RequestId : {}, User Created with id {}", userRequest.getRequestId(), customer.getId());
            UserGroup userGroup = userGroupRepository.save(userGroupModel(customer,
                    getBatchByBatchId().apply(userRequest.getBatchId()).get()).get());
            if (userGroup.getId() > 0) {
                log.info("RequestId: {}, User Group created with id {}", userRequest.getRequestId(), userGroup.getId());
                return true;
            } else {
                log.info("RequestId:{}, User Group Creation problem", userRequest.getRequestId());
                return false;
            }
        }
        log.info("RequestId:{}, User Creation Problem", userRequest.getRequestId());
        return false;
    }

    @Override
    public Optional<UserResponse> getUser(String emailorUserId, String requestId) {
        String operation = emailorUserId.contains("@") ? "emailId" : "userId";
        Optional<Customer> customerOp = operation.equals("emailId") ?
                getCustomerByEmailId().apply(emailorUserId) : getCustomerByUserId().apply(emailorUserId);
        if (customerOp.isPresent()) {
            log.info("RequestId : {}, Customer details available.. {}", requestId, customerOp.get().toString());
            Optional<UserGroup> userGroupOp = getUserGroupByCustomerId().apply(customerOp.get().getId());
            Optional<Batch> batchOp = batchRepository.findById(userGroupOp.get().getBatch().getId());
            return Optional.of(userResponse(customerOp.get(), batchOp.get().getBatchId()).get());
        }
        return Optional.empty();
    }

    @Override
    public boolean authenticate(UserRequest userRequest, String requestId) {
        Optional<Customer> customerOp = getCustomerByUserId().apply(userRequest.getCustomerId());
        if (customerOp.isPresent()) {
            log.info("RequestId : {}, Customer details available.. {}", requestId, customerOp.get().toString());
            Optional<UserGroup> userGroupOp = getUserGroupByCustomerId().apply(customerOp.get().getId());
            Optional<Batch> batchOp = batchRepository.findById(userGroupOp.get().getBatch().getId());
            return batchOp.get().getBatchId().equals(userRequest.getBatchId()) ? Boolean.TRUE : Boolean.FALSE;
        }
        log.info("RequestId : {}, Customer details not available.. {}", requestId, userRequest.getCustomerId());
        return false;
    }

    @Override
    public boolean updatePassword(UserRequest userRequest) {
        Optional<Customer> customerOp = getCustomerByUserId().apply(userRequest.getCustomerId());
        if (customerOp.isPresent()) {
            log.info("RequestId : {}, Customer details available.. {}", userRequest.getRequestId(), customerOp.get().toString());
            Customer customer = customerOp.get();
            String password = agentService.getEncString(userRequest.getPassword(), userRequest.getRequestId());
            customer.setPassword(password);
            customerRepository.save(customer);
            log.info("RequestId : {}, Password updated..", userRequest.getRequestId());
            return true;
        }
        return false;
    }

    @Override
    public boolean validatePassword(UserRequest userRequest) {
        Optional<Customer> customerOp = getCustomerByUserId().apply(userRequest.getCustomerId());
        if (customerOp.isPresent()) {
            log.info("RequestId : {}, Customer details available.. {}", userRequest.getRequestId(), customerOp.get().toString());
            Customer customer = customerOp.get();
            String password = agentService.getDctString(customer.getPassword(), userRequest.getRequestId());
            boolean status = Optional.ofNullable(password)
                    .filter(StringUtils::isNotEmpty)
                    .map(p -> StringUtils.equals(p, userRequest.getPassword()))
                    .orElse(Boolean.FALSE);
            log.info("RequestId : {}, Password Validation Status..", userRequest.getRequestId(), status);
            return status;
        }
        return false;
    }

    @Override
    public Optional<String> generateToken(UserRequest userRequest) {
        return Optional.of(agentService.getJwt(userRequest));
    }

    @Override
    public Optional<String> generateQrCode(UserRequest userRequest) {
        String data = String.join("-", userRequest.getCustomerId(), userRequest.getBatchId());
        Optional<Customer> customerOp = getCustomerByUserId().apply(userRequest.getCustomerId());
        if (customerOp.isPresent()) {
            log.info("RequestId : {}, Customer details available.. {}", userRequest.getRequestId(), customerOp.get().toString());
            Customer customer = customerOp.get();
            String qrCode = agentService.getEncString(data, userRequest.getRequestId());
            customer.setQrCode(qrCode);
            customerRepository.save(customer);
            log.info("RequestId : {}, QrCode updated..", userRequest.getRequestId());
            return Optional.of(qrCode);
        }
        return Optional.empty();
    }

    @Override
    public boolean validateQrCode(AuthByQrCodeRequest authByQrCodeRequest) {
        Optional<Customer> customerOp = getCustomerByUserId().apply(authByQrCodeRequest.getCustomerId());
        if (customerOp.isPresent()) {
            log.info("RequestId : {}, Customer details available.. {}", authByQrCodeRequest.getRequestId(), customerOp.get().toString());
            Customer customer = customerOp.get();
            boolean status = Optional.ofNullable(authByQrCodeRequest.getQrCode())
                    .filter(StringUtils::isNotEmpty)
                    .map(qr -> StringUtils.equals(qr, customer.getQrCode()))
                    .orElse(Boolean.FALSE);
            log.info("RequestId : {}, QrCode Validation Status..", authByQrCodeRequest.getRequestId(), status);
            return status;
        }
        return false;
    }

    @Override
    public String generateTempPass(String length) {
        return RandomStringUtils.randomAlphanumeric(Integer.parseInt(length));
    }

    @Override
    public String getTempPassword(String userId, String requestId) {
        log.info("{} Picking Temp Password for userId : {}", requestId, userId);
        Customer customer = getCustomerByUserId().apply(userId).get();
        if (customer != null) {
            log.info("{} Temp password for user is {}", requestId, customer.getTempPwd());
            return customer.getTempPwd();
        }
        log.error("{} No user record available for userId : {}", requestId, userId);
        return null;
    }

    private Function<String, Optional<Batch>> getBatchByBatchId() {
        return batchRepository::findByBatchId;
    }

    private Function<String, Optional<Customer>> getCustomerByEmailId() {
        return customerRepository::findByEmailId;
    }

    private Function<String, Optional<Customer>> getCustomerByUserId() {
        return customerRepository::findByUserId;
    }

    private Function<Long, Optional<UserGroup>> getUserGroupByCustomerId() {
        return userGroupRepository::findByCustomerId;
    }
}
