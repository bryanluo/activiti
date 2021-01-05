package com.jiang.workflow.holiday.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jiang.workflow.holiday.constant.StatusEnum;
import com.jiang.workflow.holiday.constant.TypeEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 版权所有 (C) 2020-2022 - gziiim
 *
 * @author: shijing.luo
 * @date: 2021/1/5 星期二
 * @description:
 */
@TableName("holiday")
@Data
public class HolidayEntity implements Serializable {
    private static final long serialVersionUID = -4019504798850020826L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long userId;
    private String userName;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date endTime;
    private TypeEnum type;
    private Double days;
    private String reason;
    private StatusEnum status;
}
