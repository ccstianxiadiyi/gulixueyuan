package com.chen.demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DemoData {
    @ExcelProperty("学生编号")
    private String sno;
    @ExcelProperty("学生姓名")
    private String sname;
}
