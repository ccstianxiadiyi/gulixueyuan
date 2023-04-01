package com.chen.edu.service;

import com.chen.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.edu.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
public interface SubjectService extends IService<Subject> {

    void saveSubject(MultipartFile multipartFile,SubjectService subjectService);

    List<OneSubject> getAllCategory();

}
