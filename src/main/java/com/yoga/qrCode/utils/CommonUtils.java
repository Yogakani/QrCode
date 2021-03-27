package com.yoga.qrCode.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class CommonUtils {

    public static HttpStatus getHttpStatusCode(boolean status) {
        return status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
