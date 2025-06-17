package com.welab.k8s_backend_user.advice;

import com.welab.k8s_backend_user.common.dto.ApiResponseDto;
import com.welab.k8s_backend_user.common.exception.BadParameter;
import com.welab.k8s_backend_user.common.exception.ClientError;
import com.welab.k8s_backend_user.common.exception.NotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@Order(value = 1)
@RestControllerAdvice
public class ApiCommonAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ClientError.class})
    public ApiResponseDto<String> handleClientError(ClientError e){
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }
//    모든 예외를 받을 수 있는 핸들러
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ApiResponseDto<String> handleException(Exception e) {
        log.error("Unhandled server error occurred: {}", e.getMessage(), e);
        return ApiResponseDto.createError(
                "serverError",
                "서버 에러입니다.");
    }
//    리소스 foundexception
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoResourceFoundException.class})
    public ApiResponseDto<String> handleNoResourceFoundException(NoResourceFoundException e){
        e.printStackTrace();
        return ApiResponseDto.createError(
                "No Resource",
                "잘못된 URL입니다."
        );
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadParameter.class})
    public ApiResponseDto<String> handleBadParameter(BadParameter e){
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFound.class})
    public ApiResponseDto<String> handleNotFound(NotFound e){
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }
}
