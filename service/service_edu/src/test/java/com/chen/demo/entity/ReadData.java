package com.chen.demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReadData {
    @ExcelProperty(index=0)
    private int sno;
    @ExcelProperty(index=1)
    private String name;
}
