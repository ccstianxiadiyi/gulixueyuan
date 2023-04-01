package com.chen.edu.controller;


import com.chen.commonutils.R;
import com.chen.edu.entity.Video;
import com.chen.edu.entity.chapter.VideoVo;
import com.chen.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
@RestController
@Api(tags="课程课时管理")
@RequestMapping("/edu/video")
public class VideoController {
    @Autowired
    private VideoService videoService;
    @PostMapping("/add/{courseId}/{chapterId}")
    @ApiOperation("增加课时")
    public R add(@RequestBody Video video,@PathVariable String courseId,@PathVariable String chapterId){
       video.setChapterId(chapterId);
       video.setCourseId(courseId);
        boolean save = videoService.save(video);
        return save?R.ok():R.error();
    }

    @DeleteMapping("/delete/{videoId}")
    @ApiOperation("根据video的id删除video")
    public R delete(@PathVariable String videoId){
        boolean b = videoService.removeById(videoId);
        return b?R.ok():R.error();
    }

    @GetMapping("/getone/{videoId}")
    @ApiOperation("根据video的Id查询，用于数据回显")
    public R getOne(@PathVariable String videoId){
        Video video = videoService.getById(videoId);
        return video!=null ? R.ok().data("video",video) :R.error();
    }

    @PutMapping("/edit/{videoId}")
    @ApiOperation("编辑课时")
    public R editData(@RequestBody Video video,@PathVariable String videoId){
        video.setId(videoId);
        boolean b = videoService.updateById(video);
        return b?R.ok():R.error();
    }
}

