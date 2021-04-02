package com.yoga.qrCode.service;

import com.yoga.qrCode.model.entity.Batch;
import com.yoga.qrCode.model.entity.Customer;
import com.yoga.qrCode.model.entity.UserGroup;
import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.UserResponse;
import com.yoga.qrCode.repository.BatchRepository;
import com.yoga.qrCode.repository.CustomerRepository;
import com.yoga.qrCode.repository.UserGroupRepository;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public boolean createNewUser(UserRequest userRequest) {
        Customer customer = customerRepository.save(userToCustomerModel(userRequest).get());
        if(customer.getId() > 0) {
            log.info("User Created with id {}", customer.getId());
            UserGroup userGroup = userGroupRepository.save(userGroupModel(customer,
                    getBatchByBatchId().apply(userRequest.getBatchId()).get()).get());
            if (userGroup.getId() > 0) {
                log.info("User Group created with id {}", userGroup.getId());
                return true;
            } else {
                log.info("User Group Creation problem");
                return false;
            }
        }
        log.info("User Creation Problem");
        return false;
    }

    @Override
    public Optional<UserResponse> getUser(String emailorUserId) {
        String operation = emailorUserId.contains("@") ? "emailId" : "userId";
        Optional<Customer> customerOp = operation.equals("emailId") ?
                getCustomerByEmailId().apply(emailorUserId) : getCustomerByUserId().apply(emailorUserId);
        if(customerOp.isPresent()) {
            log.info("Customer details available.. {}", customerOp.get().toString());
            Optional<UserGroup> userGroupOp = getUserGroupByCustomerId().apply(customerOp.get().getId());
            Optional<Batch> batchOp = batchRepository.findById(userGroupOp.get().getBatch().getId());
            return Optional.of(userResponse(customerOp.get(),batchOp.get().getBatchId()).get());
        }
        return Optional.empty();
    }

    private Function<String,Optional<Batch>> getBatchByBatchId() {
        return batchRepository :: findByBatchId;
    }

    private Function<String,Optional<Customer>> getCustomerByEmailId() {
        return customerRepository :: findByEmailId;
    }

    private Function<String,Optional<Customer>> getCustomerByUserId() {
        return customerRepository :: findByUserId;
    }

    private Function<Long,Optional<UserGroup>> getUserGroupByCustomerId() {
        return userGroupRepository :: findByCustomerId;
    }
}
