package com.yoga.qrCode.utils;

import com.yoga.qrCode.model.entity.Batch;
import com.yoga.qrCode.model.entity.Customer;
import com.yoga.qrCode.model.entity.UserGroup;
import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.UserResponse;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class ModelTransformer {

    public static Supplier<Customer> userToCustomerModel(UserRequest userRequest) {
        return () -> new Customer()
                        .setUserId(userRequest.getCustomerId())
                        .setEmailId(userRequest.getEmailId())
                        .setSetupCompleted(false);
    }

    public static Supplier<UserGroup> userGroupModel(Customer customer, Batch batch) {
        return () -> new UserGroup()
                        .setCustomer(customer)
                        .setBatch(batch);
    }

    public static Supplier<UserResponse> userResponse(Customer customer, String batchId) {
        return () -> new UserResponse()
                        .setCustomerId(customer.getUserId())
                        .setBatchId(batchId)
                        .setEmailId(customer.getEmailId())
                        .setSetupCompleted(customer.isSetupCompleted());
    }
}
