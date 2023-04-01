package com.chen.edu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.commonutils.GuliException;
import com.chen.commonutils.JwtUtils;
import com.chen.commonutils.R;
import com.chen.edu.clients.OrderClient;
import com.chen.edu.entity.*;
import com.chen.edu.entity.chapter.ChapterVo;
import com.chen.edu.entity.dto.*;
import com.chen.edu.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
@RestController
@Api(tags = "课程管理")
@RequestMapping("/edu/course")
public class CourseController {
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private CourseDescriptionService courseDescriptionService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/getAllCourse")
    @ApiOperation("获取所有课程信息")
    public R getAll() {
        List<Course> list = courseService.list(null);
        return R.ok().data("datas", list);
    }

    @PostMapping("/get/{currentPage}/{pageSize}")
    @ApiOperation("分页查询课程")
    public R getCourses(@PathVariable Integer currentPage, @PathVariable Integer pageSize, @RequestBody(required = false) CourseQuery courseQuery) {
        Page<Course> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseLambdaQueryWrapper.like
                (StringUtils.hasLength(courseQuery.getTitle()), Course::getTitle, courseQuery.getTitle());
        courseLambdaQueryWrapper.eq
                (StringUtils.hasLength(courseQuery.getTeacherId()), Course::getTeacherId, courseQuery.getTeacherId());
        courseLambdaQueryWrapper.eq(StringUtils.hasLength(courseQuery.getSubjectParentId()), Course::getSubjectParentId, courseQuery.getSubjectParentId());
        courseLambdaQueryWrapper.eq(StringUtils.hasLength(courseQuery.getSubjectId()), Course::getSubjectId, courseQuery.getSubjectId());
        courseLambdaQueryWrapper.eq(Course::getIsDeleted, 0);
        courseService.page(page, courseLambdaQueryWrapper);
        int total = (int) page.getTotal();
        List<Course> records = page.getRecords();
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("total", total);
        dataMap.put("datas", records);
        return R.ok().data(dataMap);
    }

    @PostMapping("/add")
    @ApiOperation("添加课程信息")
    @Transactional
    public R addCourse(@RequestBody CourseDto courseDto) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        Course courseServiceById = courseService.getById(course.getId());
        if (courseServiceById != null) {
            throw new GuliException(20001, "数据已经存在");
        }

        boolean save = courseService.save(course);
        if (!save) {
            return R.error().message("添加失败");
        }
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseDto.getDescription());
        courseDescription.setId(course.getId());
        boolean save1 = courseDescriptionService.save(courseDescription);
        if (!save1) {
            return R.error().message("添加失败");
        }

        return R.ok().data("id", course.getId());
    }

    @GetMapping("/getone/{id}")
    @ApiOperation("回显数据，用于课程基本信息回显")
    public R getone(@PathVariable String id) {
        Course course = courseService.getById(id);
        LambdaQueryWrapper<CourseDescription> courseDescriptionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseDescriptionLambdaQueryWrapper.eq(CourseDescription::getId, id);
        CourseDescription one = courseDescriptionService.getOne(courseDescriptionLambdaQueryWrapper);
        CourseDto courseDto = new CourseDto();
        BeanUtils.copyProperties(course, courseDto);
        courseDto.setDescription(one.getDescription());
        return R.ok().data("item", courseDto);
    }

    @PostMapping("/update")
    @ApiOperation("更新数据")
    @Transactional
    public R updateCourse(@RequestBody CourseDto courseDto) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        boolean b = courseService.updateById(course);
        if (!b) {
            throw new GuliException(20001, "编辑失败");
        }
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(courseDto.getId());
        courseDescription.setDescription(courseDto.getDescription());
        boolean b1 = courseDescriptionService.updateById(courseDescription);
        if (!b1) {
            throw new GuliException(20001, "编辑失败");
        }
        return R.ok();
    }

    @GetMapping("/publish/{id}")
    @ApiOperation("课程发布显示的数据")
    public R publishData(@PathVariable String id) {
        CoursePublishDto coursePublishDto = courseService.getPublishData(id);
        return R.ok().data("publish", coursePublishDto);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    @ApiOperation("根据id逻辑删除")
    public R deleteCourseFalse(@PathVariable String id) {
        boolean b = courseService.removeById(id);
        courseDescriptionService.removeById(id);
        LambdaQueryWrapper<Chapter> chapterWrapper = new LambdaQueryWrapper<>();
        chapterWrapper.eq(Chapter::getCourseId, id);
        boolean remove = chapterService.remove(chapterWrapper);
        LambdaQueryWrapper<Video> videoWrapper = new LambdaQueryWrapper<>();
        videoWrapper.eq(Video::getCourseId, id);
        boolean remove1 = videoService.remove(videoWrapper);

        return b && remove && remove1 ? R.ok() : R.error();
    }

    @GetMapping("/getChapter/{id}")
    @ApiOperation("根据id获取课程信息以及章节信息")
    public R getChapter(@PathVariable String id, HttpServletRequest request) {
        CourseWebVo courseWebVo=courseService.getBaseInfo(id);
        List<ChapterVo> chapterVo = chapterService.getChapterVo(id);
        String jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        if(!StringUtils.hasLength(jwtToken)){
            return R.ok().data("courseWebVo",courseWebVo).data("chapterVo",chapterVo).data("isbuy",false);
        }
        boolean buyCourse =
                orderClient.isBuyCourse(jwtToken, id);
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVo",chapterVo).data("isbuy",buyCourse);
    }

    @PostMapping("/getPage/{currentPage}/{pageSize}")
    @ApiOperation("前台查询带条件带分页")
    public R getPage(@PathVariable Integer currentPage, @PathVariable Integer pageSize, @RequestBody(required = false) CourseFront courseFront) {
        Page<Course> pageParam = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasLength(courseFront.getSubjectParentId()), Course::getSubjectParentId, courseFront.getSubjectParentId());
        queryWrapper.eq(StringUtils.hasLength(courseFront.getSubjectId()), Course::getSubjectId, courseFront.getSubjectId());
        queryWrapper.orderByDesc(StringUtils.hasLength(courseFront.getBuyCountSort()), Course::getBuyCount);
        queryWrapper.orderByDesc(StringUtils.hasLength(courseFront.getGmtCreateSort()), Course::getGmtCreate);
        queryWrapper.orderByDesc(StringUtils.hasLength(courseFront.getPriceSort()), Course::getPrice);
        courseService.page(pageParam,queryWrapper);
        List<Course> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return R.ok().data(map);
    }



}

