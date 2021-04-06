package com.yoga.qrCode.service;

import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.UserResponse;

import java.util.Optional;

public interface UserService {
    boolean createNewUser(UserRequest userRequest);
    Optional<UserResponse> getUser(String emailorUserId, String requestId);
}
