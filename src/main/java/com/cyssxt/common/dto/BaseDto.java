package com.cyssxt.common.dto;

import com.sean.datacentercommon.annotations.PrimaryKey;
import lombok.Data;

import java.util.Date;

@Data
public class BaseDto {
    @PrimaryKey
    String rowId;
    Date createTime;
    Boolean delFlag;
}
