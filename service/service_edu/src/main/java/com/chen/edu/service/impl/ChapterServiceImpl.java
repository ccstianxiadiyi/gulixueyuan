package com.chen.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.edu.entity.Chapter;
import com.chen.edu.entity.Video;
import com.chen.edu.entity.chapter.ChapterVo;
import com.chen.edu.entity.chapter.VideoVo;
import com.chen.edu.mapper.ChapterMapper;
import com.chen.edu.mapper.VideoMapper;
import com.chen.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.edu.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {
    @Resource
    private ChapterMapper chapterMapper;
    @Resource
    private VideoMapper videoMapper;
    @Override
    public List<ChapterVo> getChapterVo(String id) {
        //根据课程id查询所有的章节
        LambdaQueryWrapper<Chapter> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Chapter::getCourseId,id);
        List<Chapter> chapters = chapterMapper.selectList(queryWrapper);
        List<ChapterVo> chapterVoList = chapters.stream().map((item) -> {
            ChapterVo chapterVo = new ChapterVo();
            chapterVo.setId(item.getId());
            chapterVo.setTitle(item.getTitle());
            LambdaQueryWrapper<Video> videoWrapper = new LambdaQueryWrapper<>();
            videoWrapper.eq(Video::getCourseId, item.getCourseId());
            videoWrapper.eq(Video::getChapterId,item.getId());
            List<Video> videos = videoMapper.selectList(videoWrapper);
            List<VideoVo> collect = videos.stream().map((one) -> {
                VideoVo videoVo = new VideoVo();
                videoVo.setId(one.getId());
                videoVo.setTitle(one.getTitle());
                return videoVo;
            }).collect(Collectors.toList());
            chapterVo.setChildren(collect);
            return chapterVo;
        }).collect(Collectors.toList());
        return chapterVoList;
    }
}
