package com.atguigu.edu.controller;


import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.commonutils.R;
import com.atguigu.edu.entity.Teacher;
import com.atguigu.edu.entity.vo.TeacherQuery;
import com.atguigu.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 *
 * @author daXiong
 * @since 2020-05-14
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin //跨域
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher() {
        List<Teacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeById( @ApiParam(name = "id", value = "讲师ID", required = true)
                                 @PathVariable String id) {
        boolean flag = teacherService.removeById(id);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "分页讲师列表")
    @GetMapping("{page}/{limit}")
    public R pageList(    @ApiParam(name = "page", value = "当前页码", required = true)  @PathVariable Long page,
                          @ApiParam(name = "limit", value = "每页记录数", required = true)  @PathVariable Long limit,
                          @ApiParam(name = "teacherQuery", value = "查询对象", required = false) TeacherQuery teacherQuery){
        try{
            Page<Teacher> pageParam = new Page<>(page, limit);
            teacherService.pageQuery(pageParam, teacherQuery);
            List<Teacher> records = pageParam.getRecords();
            long total = pageParam.getTotal();
            return  R.ok().data("total", total).data("rows", records);
        }catch (Exception e){
            throw new GuliException(20003,"分页讲师列表查询异常");

        }

    }

    //3 分页查询讲师的方法
    //current 当前页
    //limit 每页记录数
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit) {
        //创建page对象
        Page<Teacher> pageTeacher = new Page<>(current,limit);
        try {
            int i = 10/0;
        }catch(Exception e) {
            //执行自定义异常
            throw new GuliException(20001,"执行了自定义异常处理....");
        }
        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal();//总记录数
        List<Teacher> records = pageTeacher.getRecords(); //数据list集合

//        Map map = new HashMap();
//        map.put("total",total);
//        map.put("rows",records);
//        return R.ok().data(map);

        return R.ok().data("total",total).data("rows",records);
    }

    //4 条件查询带分页的方法
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,@PathVariable long limit,
                                  @RequestBody(required = false)  TeacherQuery teacherQuery) {
        //创建page对象
        Page<Teacher> pageTeacher = new Page<>(current,limit);

        //构建条件
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if(!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name",name);
        }
        if(level!=null) {
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create",end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        teacherService.page(pageTeacher,wrapper);

        long total = pageTeacher.getTotal();//总记录数
        List<Teacher> records = pageTeacher.getRecords(); //数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation(value = "新增老师")
    @PostMapping("addTeacher")
    public R save(@ApiParam(name = "teacher", value = "讲师对象", required = true) @RequestBody Teacher teacher ){
        boolean save = teacherService.save(teacher);
        if(save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getById(@ApiParam(name = "id", value = "讲师ID", required = true)  @PathVariable String id){
        try {
            Teacher eduTeacher = teacherService.getById(id);
            return R.ok().data("teacher",eduTeacher);
        }catch (Exception e){
            throw new GuliException(20002,"查询讲师出现异常");

        }
    }


    //讲师修改功能
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody Teacher eduTeacher) {
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

}

