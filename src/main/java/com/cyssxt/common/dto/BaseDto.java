package com.cyssxt.common.dto;

import com.cyssxt.common.annotations.PrimaryKey;
import lombok.Data;


@Data
public class BaseDto {
    @PrimaryKey
    String rowId;
}
