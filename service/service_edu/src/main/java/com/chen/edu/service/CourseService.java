package com.chen.edu.service;

import com.chen.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.edu.entity.dto.CoursePublishDto;
import com.chen.edu.entity.dto.CourseWebVo;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
public interface CourseService extends IService<Course> {

    CoursePublishDto getPublishData(String id);


    CourseWebVo getBaseInfo(String id);

}
