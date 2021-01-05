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
public enum StatusEnum {

    DRAUGHT("草稿"),
    AUDIT("审核中"),
    NORMAL("正常");

    private String value;

    StatusEnum(String value){
       this.value = value;
    }
}
