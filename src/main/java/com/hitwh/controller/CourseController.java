package com.hitwh.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.hitwh.model.CourseModel;
import com.hitwh.request.PageBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import com.hitwh.response.Response;
import com.hitwh.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;

/**
 * 课程控制器类，用于处理与课程相关的HTTP请求
 */
@Slf4j
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@SecurityRequirement(name = "token")
public class CourseController {
    private final CourseService courseService;

    /**
     * 根据页码和页面大小查询课程
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @return 查询结果封装在Result对象中
     */
    @GetMapping("/admin/searchByPage")
    public Response searchByPage(@RequestParam(defaultValue = "1")Integer page,
                                 @RequestParam(defaultValue = "10")Integer pageSize) {
        log.info("进行分页查询所有课程操作");
        PageBean pageBean= courseService.searchByPage(page,pageSize);
        return Response.success("查询成功",pageBean);
    }

    /**
     * 添加新的课程信息
     *
     * @param courseModel 课程实体
     * @return 添加结果封装在Result对象中
     */
    @PostMapping("/teacher/add")
    public Response add(@RequestBody CourseModel courseModel,@RequestAttribute String userId) {
        log.info("进行添加课程操作");
        courseService.add(courseModel,userId);
        return Response.success("添加成功");
    }

    /**
     * 通过Excel文件批量添加课程信息
     *
     * @param file Excel文件
     * @return 添加结果封装在Result对象中
     * @throws Exception 如果读取Excel文件时发生错误
     */
    @PostMapping("/excel/teacher/add")
    @Operation(
            summary = "通过Excel文件批量添加课程",
            description = "上传Excel文件并解析课程数据",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "file", value = MultipartFile.class )

                            )
                    )
            )
    )
    @CrossOrigin(origins = "*")
    public Response addByExcel(@RequestParam("file") MultipartFile file,@RequestAttribute String userId) throws Exception{
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Response.error("仅支持上传 Excel 文件！");
        }
        log.info("进行使用excel添加课程操作");
        InputStream inputStream = file.getInputStream();
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);
        List<CourseModel> list=excelReader.readAll(CourseModel.class);
        courseService.addExcel(list,userId);
        return Response.success("添加成功");
    }

    /**
     * 更新课程信息
     *
     * @param courseModel 课程实体，包含更新后的信息
     * @return 更新结果封装在Result对象中
     */
    @PatchMapping("/teacher/update")
    public Response update(@RequestBody CourseModel courseModel, @RequestAttribute String userId) {
        log.info("进行更新课程操作");

        if(courseModel.getChooseTime()== null){
            courseService.update(courseModel,userId);
            return Response.success("更新完毕");
        }else
            return Response.error("不能在此界面设置选课时间");
    }

    /**
     * 删除课程信息
     *
     * @param courseModel 课程实体，包含要删除的课程信息
     * @return 删除结果封装在Result对象中
     */
    @DeleteMapping("/teacher/delete")
    public Response delete(@RequestBody CourseModel courseModel) {
        log.info("进行删除课程操作");
        // 检查用户角色，必须是超级管理员或教师才能删除课程

        courseService.delete(courseModel);
        return Response.success("删除成功");
    }

    /**
     * 获取指定教师的所有课程
     *
     * @param page       页码
     * @param pageSize   页面大小
     * @return 查询结果封装在Result对象中
     */
    @GetMapping("/teacher/getTeacherCourse")
    public Response getTeacherCourse(@RequestAttribute String userId, @RequestParam(defaultValue = "1")Integer page,
                                     @RequestParam(defaultValue = "10")Integer pageSize) {
        log.info("获取一位老师的所有课程");
        PageBean pageBean=courseService.getTeacherCourse(page, pageSize,userId);
        return Response.success("获取成功",pageBean);
    }
}
