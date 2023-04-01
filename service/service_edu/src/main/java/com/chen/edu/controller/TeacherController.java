package com.chen.edu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.commonutils.R;
import com.chen.edu.entity.Course;
import com.chen.edu.entity.Teacher;
import com.chen.edu.service.CourseService;
import com.chen.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ccs
 * @since 2023-03-21
 */
@RestController
@Api(tags = "教师管理模块")
@RequestMapping("/edu/teacher")
public class TeacherController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherService teacherService;
    @ApiOperation("查询所有老师")
    @GetMapping("/findAll")
    public R getLists(){
        List<Teacher> list = teacherService.list(null);
        if(CollectionUtils.isEmpty(list)){
            return R.error();
        }
        return R.ok().data("items",list);
    }

    @ApiOperation("逻辑删除老师")
    @DeleteMapping("/{id}")
    public R remove(@PathVariable @ApiParam(name="教师id",required = true) String id){
        boolean b = teacherService.removeById(id);
        return b ? R.ok():R.error();
    }

    @ApiOperation("分页查询")
    @PostMapping("/pageTeacher/{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize,@RequestBody(required = false) Teacher teacher){
        Page<Teacher> page=new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Teacher> queryWrapper=new LambdaQueryWrapper<>();
        /**
         * 查询条件构造
         */
        queryWrapper.like(StringUtils.hasLength(teacher.getName()),Teacher::getName,teacher.getName());
        queryWrapper.eq(teacher.getLevel()!=null,Teacher::getLevel,teacher.getLevel());
        queryWrapper.gt(teacher.getGmtCreate()!=null,Teacher::getGmtCreate,teacher.getGmtCreate());
        queryWrapper.lt(teacher.getGmtModified()!=null,Teacher::getGmtCreate,teacher.getGmtModified());
        queryWrapper.eq(Teacher::getIsDeleted,0);
        teacherService.page(page,queryWrapper);
        long total = page.getTotal();
        List<Teacher> records = page.getRecords();

        HashMap<String,Object> map=new HashMap<>();
        map.put("total",total);
        map.put("items",records);
        return R.ok().data(map);
    }

    @ApiOperation("添加教师功能")
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody Teacher teacher){
        boolean save = teacherService.save(teacher);
        if(save){
            return R.ok();
        }
        return R.error();
    }

    @ApiOperation("根据id查询老师")
    @GetMapping("/get/{id}")
    public R getOne(@PathVariable String id){
        LambdaQueryWrapper<Teacher> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getId,id);
        Teacher one = teacherService.getOne(queryWrapper);
        if(one!=null){
            return R.ok().data("item",one);
        }
        return R.error();
    }

    @ApiOperation("编辑教师信息")
    @PostMapping("/edit")
    public R editTeacher(@RequestBody Teacher teacher){
        boolean b = teacherService.updateById(teacher);
        if(b){
            return R.ok();
        }
        return R.error();
    }
    @GetMapping("/index")
    @ApiOperation("前台首页数据显示")
    public R getIndex(){
        QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 4");
        List<Teacher> teacherList = teacherService.list(queryWrapper);
        QueryWrapper<Course> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("id");
        courseQueryWrapper.last("limit 8");
        List<Course> list = courseService.list(courseQueryWrapper);
        return R.ok().data("teacherList",teacherList).data("courseList",list);


    }
    @GetMapping("/getEightData/{currentPage}/{pageSize}")
    @ApiOperation("分页获取讲师列表8个一页")
    public R getEightData(@PathVariable Integer currentPage,@PathVariable Integer pageSize){
        Page<Teacher> page=new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Teacher> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Teacher::getSort);
        teacherService.page(page,queryWrapper);
        List<Teacher> records = page.getRecords();
        long total = page.getTotal();
        long current = page.getCurrent();
        long size = page.getSize();
        long pages = page.getPages();
        boolean next = page.hasNext();
        boolean previous = page.hasPrevious();
        Map<String,Object> map=new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", next);
        map.put("hasPrevious", previous);
        return R.ok().data(map);
    }
    @GetMapping("/getDetailInfo/{id}")
    @ApiOperation("根据讲师id查询基本信息以及讲的课程的信息")
    public R getDetail(@PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        LambdaQueryWrapper<Course> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Course::getTeacherId,id);
        List<Course> list = courseService.list(lambdaQueryWrapper);
        Map<String,Object> map=new HashMap<>();
        map.put("teacher",teacher);
        map.put("courseList",list);
        return R.ok().data(map);
    }
}

