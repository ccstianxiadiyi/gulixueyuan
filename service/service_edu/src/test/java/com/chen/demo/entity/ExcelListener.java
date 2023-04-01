package com.chen.demo.entity;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelListener extends AnalysisEventListener<ReadData> {
    List<ReadData> list = new ArrayList<ReadData>();
    @Override
    public void invoke(ReadData readData, AnalysisContext analysisContext) {
        System.out.println("***"+readData);
        list.add(readData);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
