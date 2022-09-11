package com.zhuo.seckill.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String nickname;

    private String password;

    private String slat;

    private String head;

    private Date registerDate;

    private Date lastLoginDate;

    private Integer loginCount;


}
