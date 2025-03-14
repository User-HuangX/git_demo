package com.hitwh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hitwh.exception.ResultNotFoundException;
import com.hitwh.mapper.CommonMapper;
import com.hitwh.model.*;
import com.hitwh.mapper.UserMapper;
import com.hitwh.request.PageBean;
import com.hitwh.request.PositionRequest;
import com.hitwh.request.UpdateMeRequest;
import com.hitwh.service.UserService;
import com.hitwh.utils.MD5Encryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色操作实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final CommonMapper commonMapper;

    /**
     * 生成一个与数据库中所有学号都不相同的数字用于生成学号
     * @return 生成的唯一数字
     */
    @Override
    public String generateUniqueUserId() {
        // 获取所有用户
        List<UserModel> allUsers = userMapper.getAll();
        Set<String> existingStudentIds = new HashSet<>();
        for (UserModel user : allUsers) {
            existingStudentIds.add(user.getUserId());
        }

        Random random = new Random();
        int newStudentId;
        // 确保生成的学号是唯一的
        do {
            newStudentId = random.nextInt(1000000);
        } while (existingStudentIds.contains(Integer.toString(newStudentId)));
        // 创建一个 Date 对象
        Date date = new Date();

// 创建一个 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

// 获取年份
        int year = calendar.get(Calendar.YEAR);

        // 将生成的数字转换为实际的学号
        int realId = newStudentId+year*1000000;
        return Integer.toString(realId);
    }



    /**
     * 添加新用户
     * @param userModel 待添加的用户信息
     */
    @Override
    public void add(UserModel userModel) {
        userMapper.addIdPasswordUserNameInUser(userModel);
    }

    /**
     * 更新用户信息
     * @param userModel 包含更新信息的用户模型
     */
    @Override
    public void update(UserModel userModel) {

        userMapper.update(userModel);
    }

    /**
     * 删除用户
     * @param userModel 包含用户ID的用户模型
     */
    @Override
    public void delete(UserModel userModel) {
        userMapper.delete(userModel);
    }

    /**
     * 获取用户详细信息
     * @param userId 用户ID
     * @return 用户详细信息
     */
    @Override
    public UserModel getMe(String userId) {
        return userMapper.getMeByUserId(userId);
    }

    /**
     * 更新用户个人信息
     * @param updateMeRequest 包含更新信息的请求对象
     * @param userId 用户ID
     */
    @Override
    public void upDateMe(UpdateMeRequest updateMeRequest,String userId) {
        // 对新密码进行MD5加密
        updateMeRequest.setPassword(MD5Encryptor.encryptToMD5(updateMeRequest.getPassword()));
        // 调用Mapper方法更新用户信息
        userMapper.updateMe(updateMeRequest,userId);
    }

    /**
     * 通过Excel添加用户
     * @param list 包含多个用户信息的列表
     */
    @Override
    public void addExcel(List<UserModel> list) {
        for (UserModel userModel : list) {
            // 对每个用户的密码进行MD5加密
            userModel.setPassword(MD5Encryptor.encryptToMD5(userModel.getPassword()));
            // 调用Mapper方法添加用户
            userMapper.addByExcel(userModel);
        }
    }

    /**
     * 获取用户课程时间和类别信息
     * @param userId 用户ID，可能包含筛选条件
     * @param page 当前页
     * @param pageSize 每页大小
     * @return 分页结果
     */
    @Override
    public PageBean getCourseTimeAndCategory(String userId, Integer page, Integer pageSize) {

        // 获取课程时间和类别信息
        UserModel userModel = commonMapper.getMyModelByUserId(userId);
        PositionRequest positionRequest = new PositionRequest();
        positionRequest.setTargetGrade(userModel.getGrade());
        positionRequest.setTargetMajor(userModel.getMajor());
        positionRequest.setTargetDepartment(userModel.getDepartment());
        List<CourseModel> list= userMapper.getCourseTimeAndCategory(positionRequest);
        // 将结果转换为分页对象
        if(list.isEmpty())
            throw new ResultNotFoundException("没有可查看的课程");
        // 启用分页
        PageHelper.startPage(page,pageSize);
        PageInfo<CourseModel> pageInfo = new PageInfo<>(list);
        // 返回分页结果
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 用户注册
     * @param userModel 用户模型，包含用户信息
     */
    @Override
    public void register(UserModel userModel) {
        userMapper.addIdPasswordUserNameInUser(userModel);
    }

    /**
     * 分页显示用户信息
     * @param page 当前页
     * @param pageSize 每页大小
     * @return 分页结果
     */
    @Override
    public PageBean show(Integer page, Integer pageSize) {
        // 启用分页
        PageHelper.startPage(page,pageSize);
        // 获取分页用户信息
        List<UserModel> list= userMapper.getAllByPage();
        // 将结果转换为分页对象
        PageInfo<UserModel> pageInfo = new PageInfo<>(list);
        // 返回分页结果
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }
}

