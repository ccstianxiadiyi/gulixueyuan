package com.chen.edu.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.commonutils.GuliException;
import com.chen.commonutils.JwtUtils;
import com.chen.commonutils.R;
import com.chen.edu.clients.UcenterMemberClient;
import com.chen.edu.entity.Comment;
import com.chen.edu.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-30
 */
@RestController
@Api(tags="前台评论")
@RequestMapping("/edu/comment")
public class CommentController {
    @Autowired
    private UcenterMemberClient ucenterMemberClient;
    @Autowired
    private CommentService commentService;

    @GetMapping("/getData/{id}/{currentPage}/{pageSize}")
    @ApiOperation("根据课程id获取评论")
    public R addComment(@PathVariable String id,@PathVariable Integer currentPage,@PathVariable Integer pageSize){
        Page<Comment> page= new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCourseId,id);
        queryWrapper.orderByDesc(Comment::getGmtCreate);
        commentService.page(page,queryWrapper);
        List<Comment> list = page.getRecords();
        long total = page.getTotal();
        return R.ok().data("items",list).data("total",total);
    }

    @PostMapping("/addData/comment/{courseId}/{teacherId}")
    @ApiOperation("前台用户发布评论")
    public R addData(@RequestBody Comment comment, @PathVariable String courseId, @PathVariable String teacherId,
                     HttpServletRequest request){
        comment.setCourseId(courseId);
        comment.setTeacherId(teacherId);
        String id = JwtUtils.getMemberIdByJwtToken(request);
        if(!StringUtils.hasLength(id)){
            throw new GuliException(20001,"您还没登陆");
        }
        comment.setMemberId(id);
        R clientInfo = ucenterMemberClient.getInfo(id);
        Map<String, Object> data = clientInfo.getData();
        Object user = data.get("user");
        String s = JSON.toJSONString(user);
        JSONObject jsonObject = JSON.parseObject(s);
        String nickname = (String) jsonObject.get("nickname");
        String avatar = (String) jsonObject.get("avatar");
        comment.setNickname(nickname);
        comment.setAvatar(avatar);
        commentService.save(comment);
        return R.ok();
    }
}

