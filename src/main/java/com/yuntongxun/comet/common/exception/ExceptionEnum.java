package com.yuntongxun.comet.common.exception;

import com.yuntongxun.base.constants.CommonConstants;

/**
 * @Description:
 * @Author: lu
 * @Date: Created in 10:09 2018/9/29
 */
public enum ExceptionEnum {

    /**
     * 微信音频格式转换Mp3失败
     */
    EXCEPTION_CODE_ERROR_260001("220001", "！"),
    /**
     * An unknown error.
     */
    UNKONW_ERROR(CommonConstants.ValType.UNKNOWN_ERROR, "An unknown error."),

    /**
     * 成功
     */
    SUCCESS(CommonConstants.ValType.SUCCESS_CODE, CommonConstants.ValType.FAILURE_CODE),
    ;

    private String code;

    private String info;

    ExceptionEnum(String code, String info) {
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