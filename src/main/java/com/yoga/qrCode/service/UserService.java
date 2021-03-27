package com.yoga.qrCode.service;

import com.yoga.qrCode.model.request.UserRequest;

public interface UserService {
    boolean createNewUser(UserRequest userRequest);
}
