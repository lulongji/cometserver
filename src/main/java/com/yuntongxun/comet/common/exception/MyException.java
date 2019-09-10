package com.yuntongxun.websocket.common.exception;

import com.yuntongxun.base.constants.CommonConstants;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author lu
 */
@Data
public class MyException extends Exception {

    private static final long serialVersionUID = 2797300151072458673L;

    private String errCode = CommonConstants.ExceptionCode.EXCEPTION_CODE_ERROR;

    private String errMsg;

    public MyException() {
        super();
    }

    public MyException(String errCode) {
        super("errCode:" + errCode);
        this.errCode = errCode;
    }

    public MyException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getInfo());
        this.errCode = exceptionEnum.getCode();
        this.errMsg = exceptionEnum.getInfo();
    }


    public MyException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }


    public MyException(Throwable cause) {
        super(cause);
        if (cause != null) {
            if (StringUtils.isNotEmpty(cause.getMessage())) {
                this.errMsg = cause.getMessage();
            } else {
                this.errMsg = cause.getLocalizedMessage();
            }

        }
    }

    public MyException(String errCode, Throwable cause) {
        super(cause);
        this.errCode = errCode;
        if (cause != null) {
            if (StringUtils.isNotEmpty(cause.getMessage())) {
                this.errMsg = cause.getMessage();
            } else {
                this.errMsg = cause.getLocalizedMessage();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"errCode\":\"");
        builder.append(errCode);
        builder.append("\", \"errMsg\":\"");
        builder.append(errMsg);
        builder.append("\"}");
        return builder.toString();
    }
}
