package com.chen.edu.mapper;

import com.chen.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.edu.entity.dto.CoursePublishDto;
import com.chen.edu.entity.dto.CourseWebVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
public interface CourseMapper extends BaseMapper<Course> {
    CoursePublishDto selectCoursePublishVoById(@Param("id") String id);

    CourseWebVo getBaseInfo(String id);

}
