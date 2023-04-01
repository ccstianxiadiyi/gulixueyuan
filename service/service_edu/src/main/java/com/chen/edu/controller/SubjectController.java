package com.chen.edu.controller;


import com.chen.commonutils.R;
import com.chen.edu.entity.subject.OneSubject;
import com.chen.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
@RestController
@Api(tags="课程分类管理")
@RequestMapping("/edu/subject")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @PostMapping("/file")
    @ApiOperation("增加课程分类")
    public R upFile(MultipartFile multipartFile){
        subjectService.saveSubject(multipartFile,subjectService);
        return R.ok();
    }

    @ApiOperation("查询课程分类")
    @GetMapping("/getCategory")
    public R getCategory(){
       List<OneSubject> list=subjectService.getAllCategory();
        return R.ok().data("datas",list);
    }



}

