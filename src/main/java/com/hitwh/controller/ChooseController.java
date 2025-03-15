package com.hitwh.controller;

import com.hitwh.annotation.RecordTime;
import com.hitwh.model.*;
import com.hitwh.request.PageBean;
import com.hitwh.request.PositionRequest;
import com.hitwh.service.ChooseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hitwh.response.Response;

/**
 * 选课控制器，处理与选课相关的HTTP请求
 */
@Slf4j
@RestController
@RequestMapping("/choose")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@SecurityRequirement(name = "token")
public class ChooseController {
    private final ChooseService chooseService;

    /**
     * 学生选课操作
     * @return 选课结果
     */
    @RecordTime
    @PostMapping("/student/chooseCourse")
    public Response chooseCourse(@RequestAttribute String userId, Integer courseId) {
        log.info("进行选课操作");
        chooseService.chooseCourse(courseId,userId);
        return Response.success("选择成功");
    }

    /**
     * 学生查看自己已选的课程
     *
     * @param page       页码，默认为1
     * @param pageSize   每页大小，默认为10
     * @return 学生已选课程的分页信息
     */

    @GetMapping("/student/showStudentCourse")
    public Response showStudentCourse(@RequestAttribute String userId, @RequestParam(defaultValue = "1")Integer page,
                                      @RequestParam(defaultValue = "10")Integer pageSize) {
        log.info("进行查看自己选完的课");

        PageBean pageBean= chooseService.getStudentCourse(userId,page,pageSize);
        return Response.success("查询成功",pageBean);
    }

    /**
     * 学生查看自己可以选的课程
     *
     * @param page       页码，默认为1
     * @param pageSize   每页大小，默认为10
     * @return 学生可选课程的分页信息
     */
    @RecordTime
    @GetMapping("/student/showCourseCanChoose")
    public Response showCourseCanChoose(@RequestAttribute String userId, @RequestParam(defaultValue = "1")Integer page,
                                        @RequestParam(defaultValue = "10")Integer pageSize, String courseName,String teacher) {
        log.info("进行查看自己可以选的课");

        PageBean pageBean= chooseService.getCourseCanChoose(userId,page,pageSize,courseName,teacher);
        return Response.success("查询成功",pageBean);
    }

    /**
     * 学生退选课程操作
     *
     * @return 退选结果
     */
    @DeleteMapping("/student/dropCourse")
    public Response dropCourse(@RequestAttribute String userId,Integer courseId) {
        log.info("进行退选课操作");
        chooseService.dropCourse(courseId,userId);
        return Response.success("退课成功");
    }

    /**
     * 管理员设置选课时间
     *
     * @param courseModel 课程实体，包含课程信息
     * @return 设置选课时间结果
     */
    @PatchMapping("/admin/setChooseTime")
    public Response setChooseTime(@RequestBody CourseModel courseModel) {
        log.info("进行设置选课时间");
        chooseService.setChooseTime(courseModel);
        return Response.success("设置完成");
    }

    /**
     * 管理员设置课程选课人数上限
     *
     * @return 设置选课人数上限结果
     */
    @PostMapping("/admin/setMax")
    public Response setMax(@RequestBody PositionRequest positionRequest,Integer max,String category) {
        log.info("进行设置选课人数");

        chooseService.setMax(positionRequest,max,category);
        return Response.success("设置完成");
    }
}
