package com.yoga.qrCode.utils;

import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.AuthResponse;
import com.yoga.qrCode.model.response.QrCodeResponse;
import com.yoga.qrCode.model.response.Response;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class CommonUtils {

    public static HttpStatus getHttpStatusCode(boolean status) {
        return status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public static AuthResponse prepreAuthResponse(UserRequest userRequest, String jwt, boolean stauts) {
        AuthResponse authResponse = new AuthResponse()
                .setCustomerId(userRequest.getCustomerId())
                .setBatchId(userRequest.getBatchId())
                .setJwt(jwt);
        authResponse.setStatus(stauts);
        return authResponse;
    }

    public static QrCodeResponse prepareQrCodeResponse(UserRequest userRequest, String qrCode, boolean status) {
        QrCodeResponse qrCodeResponse = new QrCodeResponse();
        if (status) {
            qrCodeResponse.setCustomerId(userRequest.getCustomerId());
            qrCodeResponse.setBatchId(userRequest.getBatchId());
            qrCodeResponse.setQrCode(qrCode);
        }
        qrCodeResponse.setStatus(status);
        return qrCodeResponse;
    }

    public static String tempPwdContent(String content, String name) {
        String header = "<p> Hi " + name + ",";
        String body = "<br> Your Temporary Password is <b>" + content + "</b>. Please use this within 48 hrs.";
        String footer = "<br> Regards,<br>Onboarding Team.</p>";
        return new StringBuilder()
                .append(header)
                .append(body)
                .append(footer)
                .toString();
    }
}
