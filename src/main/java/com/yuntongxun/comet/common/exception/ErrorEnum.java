package com.yuntongxun.comet.common.exception;

import com.yuntongxun.base.constants.CommonConstants;

/**
 * @Description:
 * @Author: lu
 * @Date: Created in 10:09 2018/9/29
 */
public enum ErrorEnum {

    /**
     * An unknown error.
     */
    UNPOLL("-200", "断开心跳！"),

    /**
     * 成功
     */
    SUCCESS(CommonConstants.ValType.SUCCESS_CODE, CommonConstants.ValType.FAILURE_CODE),
    ;

    private String code;

    private String info;

    ErrorEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {

        return code;
    }

    public String getInfo() {
        return info;
    }
}