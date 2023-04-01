package com.chen.edu.service;

import com.chen.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.edu.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
public interface ChapterService extends IService<Chapter> {

    List<ChapterVo> getChapterVo(String id);
}
