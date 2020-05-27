package com.atguigu.edu.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.chapter.ChapterVo;
import com.atguigu.edu.entity.vo.CourseInfoVo;
import com.atguigu.edu.entity.vo.CoursePublishVo;
import com.atguigu.edu.entity.vo.CourseQuery;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@Api(description="课程管理")
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //添加课程基本信息的方法
    @ApiOperation(value = "新增课程")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@ApiParam(name = "CourseInfoVo", value = "课程基本信息", required = true)
                            @RequestBody CourseInfoVo courseInfoVo) {
        //返回添加之后课程id，为了后面添加大纲使用
        String courseId = courseService.saveCourseInfo(courseInfoVo);
        if(!StringUtils.isEmpty(courseId)){
            return R.ok().data("courseId", courseId);
        }else{
            return R.error().message("保存失败");
        }
    }

    //根据课程id查询课程基本信息
    @ApiOperation(value = "根据课程id查询课程基本信息")
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId) {
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");//设置课程发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

   //课程列表 基本实现
    //TODO  完善条件查询带分页
   @ApiOperation(value = "分页课程列表")
   @PostMapping("{page}/{limit}")
   public R getCourseList(        @ApiParam(name = "page", value = "当前页码", required = true)   @PathVariable Long page,
                                  @ApiParam(name = "limit", value = "每页记录数", required = true)         @PathVariable Long limit,
                                  @ApiParam(name = "courseQuery", value = "查询对象", required = false)   @RequestBody(required = false) CourseQuery courseQuery
                                  ) {
       Page<EduCourse> pageParam = new Page<>(page, limit);
       courseService.pageQuery(pageParam, courseQuery);
       List<EduCourse> records = pageParam.getRecords();
       long total = pageParam.getTotal();
       return  R.ok().data("total", total).data("rows", records);

   }

   @ApiOperation(value = "根据ID删除课程")
   @DeleteMapping("{id}")
   public R removeById(    @ApiParam(name = "id", value = "课程ID", required = true) @PathVariable String id
                           ){
        try {
            boolean result = courseService.removeCourseById(id);
            if (result) {
                return R.ok();
            } else {
                return R.error().message("删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw  new GuliException(20002,"删除课程异常");
        }

   }


}

