package com.anii.querydsl.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasePageRequest implements Serializable {

    private Integer pageNo;

    private Integer pageSize;
}
