package com.qyk.exception;

import com.qyk.utils.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONResult handleMaxUploadFile(MaxUploadSizeExceededException e){
        return JSONResult.errorMsg("文件上传大小不能超过500k");
    }
}
