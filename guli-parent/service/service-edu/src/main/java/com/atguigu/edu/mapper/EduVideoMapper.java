package com.atguigu.edu.mapper;

import com.atguigu.edu.entity.EduVideo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
public interface EduVideoMapper extends BaseMapper<EduVideo> {

    //1 根据课程id删除小节
    void removeVideoByCourseId(String courseId);

}
