package com.chen.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.commonutils.R;
import com.chen.edu.entity.Subject;
import com.chen.edu.entity.excel.SubjectData;
import com.chen.edu.entity.subject.OneSubject;
import com.chen.edu.entity.subject.TwoSubject;
import com.chen.edu.listener.SubjectExcelListener;
import com.chen.edu.mapper.SubjectMapper;
import com.chen.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Resource
    private SubjectMapper subjectMapper;

    /*
     * 添加课程分类
     * */
    @Override
    public void saveSubject(MultipartFile multipartFile, SubjectService subjectService) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OneSubject> getAllCategory() {
        //1.查询所有一级分类

        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getParentId, "0");
        List<Subject> list = subjectMapper.selectList(queryWrapper);

        //2.根据一级分类查询所有二级分类
        List<OneSubject> oneSubjects = list.stream().map((item) -> {
            LambdaQueryWrapper<Subject> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Subject::getParentId, item.getId());
            List<Subject> subjects = subjectMapper.selectList(queryWrapper1);
            //***********************************************
            List<TwoSubject> collect = subjects.stream().map((two) -> {
                TwoSubject twoSubject = new TwoSubject();
                BeanUtils.copyProperties(two, twoSubject);
                return twoSubject;
            }).collect(Collectors.toList());

            OneSubject one = new OneSubject();
            one.setId(item.getId());
            one.setTitle(item.getTitle());
            one.setChildren(collect);

            return one;
        }).collect(Collectors.toList());

        return oneSubjects;
    }
}
