package com.cyssxt.common.dto;

import com.cyssxt.common.annotations.PrimaryKey;
import lombok.Data;

import java.util.Date;

@Data
public class BaseDto {
    @PrimaryKey
    String rowId;
    Date createTime;
    Boolean delFlag;
}
