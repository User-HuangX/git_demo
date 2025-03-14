package com.hitwh.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hitwh.exception.InvalidRequestException;
import com.hitwh.exception.ResultNotFoundException;
import com.hitwh.mapper.CommonMapper;
import com.hitwh.model.CourseModel;
import com.hitwh.request.PageBean;
import com.hitwh.model.UserModel;
import com.hitwh.mapper.ChooseMapper;
import com.hitwh.request.PositionRequest;
import com.hitwh.service.ChooseService;
import com.hitwh.utils.RedisDistributedLock;
import com.hitwh.utils.RedisIdWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 实现选课服务的类
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ChooseServiceImpl implements ChooseService {
    private final ChooseMapper chooseMapper;
    private final CommonMapper commonMapper;
    private final RedisDistributedLock redisDistributedLock;
    private final RedisIdWorker redisIdWorker;

    /**
     * 学生选课方法
     *
     * @param courseId 课程ID
     * @param userId 用户ID
     */
    @Override
    public void chooseCourse(Integer courseId,String userId) {
        UserModel user=commonMapper.getMyModelByUserId(userId);
        CourseModel courseModel = commonMapper.getCourseModelByCourseId(courseId);
        String lockKey = "lock:choose:" + userId + ":" + courseModel.getCourseId();
        long id = redisIdWorker.nextId(lockKey);
        String requestId = Long.toString(id);
        int EXPIRE_TIME = 100000; // 锁的过期时间，单位：秒

        try {
            if (redisDistributedLock.tryLock(lockKey, requestId, EXPIRE_TIME)) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // 格式化当前时间
                String formattedDateTime = formatter.format(date);
                Date now =formatter.parse(formattedDateTime);
                PositionRequest req=commonMapper.getPositionRequestByChooseModel(courseModel);

                // 检查选课条件
                if (legitimacyCheck(user, courseModel, req, now) && courseModel.getStatus() == 1) {
                    chooseMapper.addChooseByMyIdAndCourseId(user.getMyId(), courseModel.getCourseId());
                    chooseMapper.updateCourseCapacityByCourseId(courseModel.getCourseId(),-1);
                } else {
                    throw new InvalidRequestException("您不允许选择该课程");
                }
            } else {
                throw new InvalidRequestException("选课操作被拒绝");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            if (redisDistributedLock.releaseLock(lockKey, requestId)) {
                log.info("释放锁成功");
            } else {
                log.info("释放锁失败");
            }
        }
    }

    private boolean legitimacyCheck(UserModel user, CourseModel courseModel, PositionRequest req, Date now) {
        return user.getGrade().equals(courseModel.getTargetGrade()) &&
                user.getDepartment().equals(courseModel.getTargetDepartment()) &&
                user.getMajor().equals(courseModel.getTargetMajor()) &&
                chooseMapper.getChooseByMyIdAndCourseId(user.getMyId(), courseModel.getCourseId()).isEmpty() &&
                chooseMapper.getNumFormChooseByCourseIdDepartmentAndCategoryAndMajorAndGrade(user.getMyId(), courseModel.getCourseId(), courseModel.getTargetMajor(), courseModel.getTargetGrade(), courseModel.getTargetDepartment(), courseModel.getCategory()) < chooseMapper.getMaxNumFormMaxByPosition(req.getPositionId(), courseModel.getCategory()) &&
                courseModel.getChooseTime().before(now) &&
                now.before(courseModel.getEndChooseTime());
    }

    /**
     * 获取学生选过的课程
     *
     * @param UserId 用户ID
     * @param page 页码
     * @param pageSize 页面大小
     * @return 课程列表
     */
    @Override
    public PageBean getStudentCourse(String UserId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        UserModel userModel = commonMapper.getMyModelByUserId(UserId);
        if(Objects.equals(userModel.getStatus(), "0")){
            throw new ResultNotFoundException("该用户已被删除");
        }
        List<CourseModel> list = chooseMapper.getCourseByMyId(userModel.getMyId());
        PageInfo<CourseModel> pageInfo = new PageInfo<>(list);
        // 返回分页结果
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 获取学生可以选的课程
     *
     * @param UserId 用户ID
     * @param page 页码
     * @param pageSize 页面大小
     * @param teacher 教师
     * @param courseName 课程名
     * @return 可选课程列表
     */
    @Override
    public PageBean getCourseCanChoose(String UserId, Integer page, Integer pageSize, String courseName, String teacher) {
        Date date = new Date(System.currentTimeMillis());
        PageHelper.startPage(page, pageSize);


        UserModel userModel = commonMapper.getMyModelByUserId(UserId);
        if (Objects.equals(userModel.getStatus(), "0")) {
            throw new ResultNotFoundException("该用户已被删除");
        }
        // 获取所有可选课程
        PositionRequest positionRequest = new PositionRequest(0,userModel.getGrade(), userModel.getMajor(), userModel.getDepartment());
        List<CourseModel> list = chooseMapper.getCourseCanChoose(userModel.getMyId(),positionRequest.getTargetGrade(),positionRequest.getTargetMajor(),positionRequest.getTargetDepartment());


        // 过滤符合条件的课程
        List<CourseModel> filteredList = new ArrayList<>();
        List<CourseModel> difference = null;
        for (CourseModel courseModel : list) {
            PositionRequest req = commonMapper.getPositionRequestByChooseModel(courseModel);
            if (legitimacyCheck(userModel, courseModel, req, date)) {
                filteredList.add(courseModel);
            }
            if (courseName != null && teacher!=null) {
                List<CourseModel> list1 = chooseMapper.getCourseByReq(courseName,teacher);
                List<CourseModel> intersection = list1.stream().filter(filteredList::contains).collect(Collectors.toList());
                log.info("交集: {}", intersection);
                difference = filteredList.stream().filter(e -> !intersection.contains(e)).collect(Collectors.toList());
                log.info("差集: {}", difference);
            }else throw new InvalidRequestException("请输入课程名或教师名");
        }

        // 创建新的 PageBean 并返回
        PageInfo<CourseModel> pageInfo = new PageInfo<>(difference);
        // 返回分页结果
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 设置课程选课时间
     *
     * @param courseModel 课程信息
     */
    @Override
    public void setChooseTime(CourseModel courseModel) {
        chooseMapper.updateChooseTimeFromCourseByMajorAndGradeAndDepartmentAndCategory(courseModel);
    }

    /**
     * 设置最大选课人数
     *
     * @param positionRequest 职位请求
     * @param max 最大人数
     * @param category 课程类别
     */
    @Override
    public void setMax(PositionRequest positionRequest, Integer max,String category) {
        chooseMapper.addMaxNumByPosition(category,positionRequest.getPositionId(), max);
    }

    /**
     * 退选课程
     *
     * @param courseId 课程ID
     * @param userId 用户ID
     */
    @Override
    public void dropCourse(Integer courseId,String userId) {
        CourseModel courseModel=commonMapper.getCourseModelByCourseId(courseId);
        UserModel user=commonMapper.getMyModelByUserId(userId);
        if(chooseMapper.getChooseByMyIdAndCourseId(user.getMyId(),courseModel.getCourseId()).isEmpty()){
            throw new InvalidRequestException("您没有选过该课程");
        }
        chooseMapper.deleteChooseByMyIdAndCourseId(user.getMyId(),courseModel.getCourseId());
        chooseMapper.updateCourseCapacityByCourseId(courseModel.getCourseId(),+1);
    }
}
