package com.hitwh.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.hitwh.model.*;
import com.hitwh.request.PageBean;
import com.hitwh.request.UpdateMeRequest;
import com.hitwh.service.UserService;
import com.hitwh.utils.MD5Encryptor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.hitwh.response.Response;

import java.io.InputStream;
import java.util.List;

import static com.hitwh.utils.pinYinUtils.getPinyinInitial;

/**
 * 用户控制器类，处理与用户相关的HTTP请求
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@SecurityRequirement(name = "token")
public class UserController {
    private final UserService userService;
    /**
     * 处理用户注册请求
     *
     * @param userModel 用户信息，包括手机号和密码
     * @return 响应结果，包括注册是否成功
     */
    @PostMapping("/admin/autoRegister")
    public Response register(@RequestBody UserModel userModel) {
        // 生成唯一用户ID
        userModel.setUserId(userService.generateUniqueUserId());
        // 设置初始密码为用户名首字母加用户ID
        userModel.setPassword(getPinyinInitial(userModel.getUserName())+ userModel.getUserId());
        // 对密码进行MD5加密
        userModel.setPassword(MD5Encryptor.encryptToMD5(userModel.getPassword()));
        // 调用认证服务进行用户注册
        userService.register(userModel);
        // 返回注册成功响应
        return Response.success("注册成功",userModel);
    }

    /**
     * 根据页码和页面大小查询用户信息
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @return 用户信息的分页结果
     */
    @GetMapping("/admin/getAllByPage")
    public Response getAllByPage(@RequestParam(defaultValue = "1")Integer page,
                                 @RequestParam(defaultValue = "10")Integer pageSize) {
        log.info("进行查询用户的操作");

        PageBean pageBean= userService.show(page,pageSize);
        return Response.success("获取用户成功",pageBean);
    }

    /**
     * 通过Excel文件批量添加用户
     *
     * @param file 包含用户信息的Excel文件
     * @return 添加结果
     * @throws Exception 如果文件读取或数据解析出错
     */
    @PostMapping("/excel/admin/addUser")
    @Operation(
            summary = "通过Excel文件批量添加用户",
            description = "上传Excel文件并解析用户数据",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "file", value = MultipartFile.class )

                            )
                    )
            )
    )
    @CrossOrigin(origins = "*")
    public Response addByExcel(@RequestParam("file") MultipartFile file) throws Exception{
        log.info("进行使用excel添加User操作");
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Response.error("仅支持上传 Excel 文件！");
        }
        InputStream inputStream = file.getInputStream();
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);
        List<UserModel> list=excelReader.readAll(UserModel.class);
        if (list != null) {
            list.forEach(user -> {
                try {
                    if (user != null) {
                        log.info("User: {}", user);
                    }
                } catch (Exception e) {
                    // 记录异常信息，避免程序崩溃
                    System.err.println("Error while converting user to string: " + e.getMessage());
                }
            });
        }
        userService.addExcel(list);
        return Response.success("添加成功");
    }

    /**
     * 更新用户信息
     *
     * @param userModel 用户实体
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    public Response update(@RequestBody UserModel userModel) {
        log.info("管理员进行更新用户操作");
        userService.update(userModel);
        return Response.success("更新完成");
    }

    /**
     * 删除用户
     *
     * @param userModel 用户实体
     * @return 删除结果
     */
    @PostMapping("/admin/delete")
    public Response delete(@RequestBody UserModel userModel) {
        log.info("进行删除用户操作");

        userService.delete(userModel);
        return Response.success("删除成功");
    }

    /**
     * 获取当前用户信息
     *
     * @param
     * @return 用户信息
     */
    @GetMapping("/getMe")
    public Response getMe(@RequestAttribute String userId) {
        log.info("进行获取自己信息操作");

        UserModel e= userService.getMe(userId);
        return Response.success("获取成功",e);
    }

    /**
     * 更新当前用户信息
     *
     * @param updateMeRequest 更新用户的请求体
     * @param userId 当前用户的ID
     * @return 更新结果
     */
    @PostMapping("/updateMe")
    public Response updateMe(@RequestBody UpdateMeRequest updateMeRequest,@RequestAttribute String userId) {
        log.info("进行更新自己信息操作");

        userService.upDateMe(updateMeRequest,userId);
        return Response.success("更新个人身份信息完成");
    }

    /**
     * 查询待选课的课程类别和选课时间
     *
     * @param userId 用户Id
     * @param page       页码
     * @param pageSize   页面大小
     * @return 课程类别和选课时间的分页结果
     */
    @GetMapping("/showCategoryAndTheTime")
    public Response showCategoryAndTheTime(@RequestAttribute String userId, @RequestParam(defaultValue = "1")Integer page,
                                           @RequestParam(defaultValue = "10")Integer pageSize) {
        log.info("查询待选课课程类别与选课时间");

        PageBean pageBean= userService.getCourseTimeAndCategory(userId,page,pageSize);
        return Response.success("查询成功",pageBean);
    }
}
