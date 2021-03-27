package com.yoga.qrCode.service;

import com.yoga.qrCode.model.entity.Batch;
import com.yoga.qrCode.model.entity.Customer;
import com.yoga.qrCode.model.entity.UserGroup;
import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.repository.BatchRepository;
import com.yoga.qrCode.repository.CustomerRepository;
import com.yoga.qrCode.repository.UserGroupRepository;
import com.yoga.qrCode.utils.ModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.yoga.qrCode.utils.ModelTransformer.userGroupModel;
import static com.yoga.qrCode.utils.ModelTransformer.userToCustomerModel;

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
                    getBatchByBatchId().apply(userRequest.getBatchId())).get());
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

    private Function<String,Batch> getBatchByBatchId() {
        return batchRepository :: findByBatchId;
    }
}
