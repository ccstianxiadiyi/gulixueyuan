package com.chen.demo.entity;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        String name = "D:\\test.xlsx";
//        List<DemoData> list=new ArrayList<>();
//        list.add(new DemoData("001","1"));
//        list.add(new DemoData("002","2"));
//        list.add(new DemoData("003","3"));
//        EasyExcel.write(name, DemoData.class).sheet("学生列表").doWrite(list);
        EasyExcel.read(name, ReadData.class, new
                ExcelListener()).sheet().doRead();
    }
}
