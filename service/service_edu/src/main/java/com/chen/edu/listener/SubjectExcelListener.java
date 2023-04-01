package com.chen.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.commonutils.GuliException;
import com.chen.edu.entity.Subject;
import com.chen.edu.entity.excel.SubjectData;
import com.chen.edu.service.SubjectService;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    private SubjectService subjectService;

    public SubjectExcelListener(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData==null){
            throw new GuliException(20001,"文件数据为空");
        }
        String oneSubjectName = subjectData.getOneSubjectName();
        Subject subject = this.existsOneSubject(subjectService, oneSubjectName);
        //判断一级分类存不存在
        if(subject==null){
            subject=new Subject();
            subject.setParentId("0");
            subject.setTitle(oneSubjectName);
            subjectService.save(subject);
        }
        //判断二级分类存不存在
        String twoSubjectName = subjectData.getTwoSubjectName();
        Subject subject1 = this.existsTwoSubject(subjectService, twoSubjectName, subject.getId());
        if(subject1==null){
            subject1=new Subject();
            subject1.setParentId(subject.getId());
            subject1.setTitle(twoSubjectName);
            subjectService.save(subject1);
        }


    }
    //判断一级分类不能重复添加
    private Subject existsOneSubject(SubjectService subjectService,String name){
        LambdaQueryWrapper<Subject> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getTitle,name);
        queryWrapper.eq(Subject::getParentId,"0");
        Subject one = subjectService.getOne(queryWrapper);
        return one;
    }

    //判断二级分类不能重复添加
    private Subject existsTwoSubject(SubjectService subjectService,String name,String pid){
        LambdaQueryWrapper<Subject> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getTitle,name);
        queryWrapper.eq(Subject::getParentId,pid);
        Subject one = subjectService.getOne(queryWrapper);
        return one;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
