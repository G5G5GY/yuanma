package com.yuanma.webmvc.handler;


import com.yuanma.exception.LoginException;
import com.yuanma.webmvc.vo.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(LoginException.class)
    @ResponseBody
    public ApiResult loginException(LoginException e){
        LOGGER.error(e.getMessage(),e);
        if(e.getCode().startsWith("E90")){
            return new ApiResult().addError("-1",e.getMessage());
        }
        return new ApiResult().addError(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public ApiResult exceptionHandle(SQLException e){
        LOGGER.error(e.getMessage(),e);
        return new ApiResult().addError("System Failed please contact to administrator");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult exceptionHandle(Exception e){
        LOGGER.error(e.getMessage(),e);

        /*StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        writer.flush();
        writer.close();
        stringWriter.toString()
         */
        return new ApiResult().addError(e.getMessage());
    }


}
