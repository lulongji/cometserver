package com.yuntongxun.comet.model;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;

@Data
public class User implements Serializable {

    private String keys;

    private LinkedList<Message> messages;

}
