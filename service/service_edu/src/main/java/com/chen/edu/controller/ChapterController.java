package com.chen.edu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.commonutils.R;
import com.chen.edu.entity.Chapter;
import com.chen.edu.entity.Video;
import com.chen.edu.entity.chapter.ChapterVo;
import com.chen.edu.service.ChapterService;
import com.chen.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
@RestController
@Slf4j
@Api(tags = "章节管理")
@RequestMapping("/edu/chapter")
public class ChapterController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private ChapterService chapterService;

    @GetMapping("/findAll/{id}")
    @ApiOperation("查询课程中的章节和小节")
    public R getAllChapter(@PathVariable String id){
        List<ChapterVo> lists=chapterService.getChapterVo(id);
        return R.ok().data("items",lists);
    }


    @PostMapping("/add/{id}")
    @ApiOperation("增加课程小结")
    public R addChapter(@RequestBody Chapter chapter,@PathVariable String id){
        log.info(id+"---------------"+chapter.toString());
        chapter.setCourseId(id);
        boolean save = chapterService.save(chapter);
        if(save){
            return R.ok().data("courseId",id);
        }
        return R.error().message("添加小结失败");
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    @ApiOperation("根据章节小结id删除章节")
    public R deleteChapter(@PathVariable String id){
        boolean b = chapterService.removeById(id);
        LambdaQueryWrapper<Video> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getChapterId,id);
        boolean remove = videoService.remove(queryWrapper);
        if(b&&remove){
            return R.ok();
        }
        return R.error();

    }

    @GetMapping("/getchapter/{chapterId}")
    @ApiOperation("根据章节小结获取课程")
    public R getOneChapter(@PathVariable String chapterId){
        Chapter chapter = chapterService.getById(chapterId);
        if(chapter==null){
            return R.error().message("课程Id输入错误");
        }
        return R.ok().data("chapter",chapter);
    }

    @PostMapping("/editChapter/{id}")
    @ApiOperation("根据小结id更新数据")
    public R editData(@RequestBody Chapter chapter,@PathVariable String id){
        chapter.setId(id);
        boolean b = chapterService.updateById(chapter);
        return b ? R.ok():R.error();

    }


}

