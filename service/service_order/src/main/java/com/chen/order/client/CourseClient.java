package com.chen.order.client;

import com.chen.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="service-edu")
@Component
public interface CourseClient {
    @GetMapping("/edu/course/getChapter/{id}")
    public R getChapter(@PathVariable String id) ;
}
