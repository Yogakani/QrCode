package com.yoga.qrCode.api;

import com.yoga.qrCode.model.request.Request;
import com.yoga.qrCode.model.response.Response;
import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.UserResponse;
import com.yoga.qrCode.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.yoga.qrCode.utils.CommonUtils.getHttpStatusCode;

@RequestMapping(value = "/api/v1.0/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/create")
    public ResponseEntity<Response> createUser(@RequestBody UserRequest userRequest, @RequestHeader("requestId") String requestId) {
        log.info("RequestId : {} - Create User process starts..",requestId);
        userRequest.setRequestId(requestId);
        boolean status = userService.createNewUser(userRequest);
        log.info("RequestId : {} - Create User process ends..",requestId);
        return new ResponseEntity<>(new Response().setStatus(status), getHttpStatusCode(status));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getUser(@RequestHeader("userId") String userId, @RequestHeader("requestId") String requestId) {
        log.info("RequestId : {} - Get User process starts..",requestId);
        Optional<UserResponse> userResponseOp = userService.getUser(userId, requestId);
        log.info("RequestId : {} - Get User process ends..",requestId);
        return userResponseOp.isPresent() ?
             new ResponseEntity<>(userResponseOp.get().setStatus(true), getHttpStatusCode(true)) :
             new ResponseEntity<>(new Response().setStatus(false), getHttpStatusCode(false));
    }

    @PutMapping(value = "/update/password")
    public ResponseEntity<Response> updatePassword(@RequestBody UserRequest userRequest, @RequestHeader("requestId") String requestId) {
        log.info("RequestId : {} - User Password update process starts..",requestId);
        userRequest.setRequestId(requestId);
        boolean status = Optional.ofNullable(userRequest)
                                .filter(r -> StringUtils.isNotEmpty(r.getCustomerId()) && StringUtils.isNotEmpty(r.getBatchId()))
                                .map(r -> userService.updatePassword(r))
                                .orElse(Boolean.FALSE);
        log.info("RequestId : {} - User Password update process ends..",requestId);
        return new ResponseEntity<>(new Response().setStatus(status), getHttpStatusCode(status));
    }


}
