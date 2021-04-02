package com.yoga.qrCode.api;

import com.yoga.qrCode.model.response.Response;
import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.UserResponse;
import com.yoga.qrCode.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.yoga.qrCode.utils.CommonUtils.getHttpStatusCode;

@RequestMapping(value = "/api/v1.0/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/create")
    public ResponseEntity<Response> createUser(@RequestBody UserRequest userRequest) {
        boolean status = userService.createNewUser(userRequest);
        return new ResponseEntity<>(new Response().setStatus(status), getHttpStatusCode(status));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<UserResponse> getUser(@RequestHeader("userId") String userId) {
        Optional<UserResponse> userResponseOp = userService.getUser(userId);
        return userResponseOp.isPresent() ?
             new ResponseEntity<>((UserResponse) userResponseOp.get().setStatus(true), getHttpStatusCode(true)) :
             new ResponseEntity<>((UserResponse) new UserResponse().setStatus(false), getHttpStatusCode(false));
    }


}
