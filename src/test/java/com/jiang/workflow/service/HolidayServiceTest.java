package com.jiang.workflow.service;

import com.jiang.workflow.WorkflowApplicationTests;
import com.jiang.workflow.holiday.constant.StatusEnum;
import com.jiang.workflow.holiday.constant.TypeEnum;
import com.jiang.workflow.holiday.domain.entity.HolidayEntity;
import com.jiang.workflow.holiday.domain.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 版权所有 (C) 2020-2022 - gziiim
 *
 * @author: shijiang.luo
 * @date: 2021/1/5 星期二
 * @description:
 */
public class HolidayServiceTest extends WorkflowApplicationTests {

    @Resource
    private HolidayRepository holidayRepository;

    @Test
    public void insert() {
        HolidayEntity entity = new HolidayEntity();
        entity.setDays(1.0);
        entity.setReason("test add");
        entity.setStatus(StatusEnum.DRAUGHT);
        entity.setType(TypeEnum.LEAVE_PERSON_AFFAIRS);
        entity.setUserId(1L);
        entity.setUserName("zhangsan");
        entity.setStartTime(new Date());
        entity.setEndTime(new Date());
        int result = holidayRepository.insert(entity);
        Assert.isTrue(result > 0, "insert failure");
    }

    @Test
    public void update() {
        HolidayEntity queryEntity = holidayRepository.selectById(1346374290331713537L);
        Assert.notNull(queryEntity, "entity is null !");
        queryEntity.setStatus(StatusEnum.AUDIT);
        int result = holidayRepository.updateById(queryEntity);
        Assert.isTrue(result > 0, "update failure");
    }

}
