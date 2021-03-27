package com.yoga.qrCode.api;

import com.yoga.qrCode.model.Response.Response;
import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
