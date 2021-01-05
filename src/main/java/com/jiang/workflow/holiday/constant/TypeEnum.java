package com.jiang.workflow.holiday.constant;

import lombok.Getter;

/**
 * 版权所有 (C) 2020-2022 - gziiim
 *
 * @author: shijiang.luo
 * @date: 2021/1/5 星期二
 * @description:
 */
@Getter
public enum TypeEnum {

    LEAVE_SICK("病假"),
    LEAVE_MATERNITY("产假"),
    LEAVE_PERSON_AFFAIRS("事假"),
    OTHER("其它");

    private String value;

    TypeEnum(String value){
        this.value = value;
    }
}
