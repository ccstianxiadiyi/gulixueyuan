package com.chen.edu.service.impl;

import com.chen.edu.entity.Course;
import com.chen.edu.entity.dto.CoursePublishDto;
import com.chen.edu.entity.dto.CourseWebVo;
import com.chen.edu.mapper.CourseMapper;
import com.chen.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Resource
    private CourseMapper courseMapper;
    @Override
    public CoursePublishDto getPublishData(String id) {
        CoursePublishDto coursePublishDto = courseMapper.selectCoursePublishVoById(id);
        return coursePublishDto;
    }

    @Override
    public CourseWebVo getBaseInfo(String id) {
        return baseMapper.getBaseInfo(id);
    }


}
