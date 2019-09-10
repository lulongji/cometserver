package com.yuntongxun.comet.model;

import lombok.Data;

@Data
public class IMClient {
    private String id;
    private String name;
    private long saveTime;
    private int expire;
}
