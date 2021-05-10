package com.yoga.qrCode.service;

import com.yoga.qrCode.model.request.AuthByQrCodeRequest;
import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.UserResponse;

import java.util.Optional;

public interface UserService {
    boolean createNewUser(UserRequest userRequest);
    Optional<UserResponse> getUser(String emailorUserId, String requestId);
    boolean authenticate(UserRequest userRequest, String requestId);
    boolean updatePassword(UserRequest userRequest);
    boolean validatePassword(UserRequest userRequest);
    Optional<String> generateToken(UserRequest userRequest);
    Optional<String> generateQrCode(UserRequest userRequest);
    boolean validateQrCode(AuthByQrCodeRequest authByQrCodeRequest);
}