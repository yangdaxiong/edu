package com.atguigu.edu.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.edu.client.OrderClient;
import com.atguigu.edu.client.OrderFile;
import com.atguigu.edu.client.UcenterClient;
import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.chapter.ChapterVo;
import com.atguigu.edu.entity.frontvo.CourseFrontVo;
import com.atguigu.edu.entity.frontvo.CourseWebVo;
import com.atguigu.edu.service.EduChapterService;
import com.atguigu.edu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    //1 条件查询带分页查询课程
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo) {
        Page<EduCourse> pageCourse = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseFrontList(pageCourse,courseFrontVo);
        //返回分页所有数据
        return R.ok().data(map);
    }

    //2 课程详情的方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId) {
        //根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList);
    }

    //根据课程id查询课程信息
    @PostMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id) {
        CourseWebVo courseInfo = courseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo,courseWebVoOrder);
        return courseWebVoOrder;
    }


    //根据id查询课程详情信息
    @GetMapping("getCourseInfo/{id}")
    public R getCourseInfo(@PathVariable String id, HttpServletRequest request) {
        //课程查询课程基本信息
        CourseWebVo courseFrontInfo = courseService.getBaseCourseInfo(id);
        //查询课程里面大纲数据
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(id);

        //远程调用，判断课程是否被购买
        Boolean buyCourse = orderClient.isBuyCourse(JwtUtils.getMemberIdByJwtToken(request), id);
        return R.ok().data("courseWebVo",courseFrontInfo).data("chapterVideoList",chapterVideoList).data("isbuy",buyCourse);
    }
}












