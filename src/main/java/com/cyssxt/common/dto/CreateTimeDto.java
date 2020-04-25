package com.cyssxt.common.dto;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class CreateTimeDto extends BaseDto{
    Timestamp createTime;
    Timestamp updateTime;
}
