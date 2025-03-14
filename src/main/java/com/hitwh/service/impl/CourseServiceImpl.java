package com.hitwh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hitwh.exception.InvalidRequestException;
import com.hitwh.exception.ResultNotFoundException;
import com.hitwh.mapper.CommonMapper;
import com.hitwh.model.CourseModel;
import com.hitwh.model.UserModel;
import com.hitwh.request.PageBean;
import com.hitwh.mapper.CourseMapper;
import com.hitwh.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程服务实现类
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseMapper courseMapper;
    private final CommonMapper commonMapper;

    /**
     * 分页查询课程信息
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @return 返回分页结果对象
     */
    @Override
    public PageBean searchByPage(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<CourseModel> list = courseMapper.getAllByPage();
        PageInfo<CourseModel> pageInfo = new PageInfo<>(list);
        // 返回分页结果
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 添加课程信息
     *
     * @param courseModel 课程模型对象
     * @param userId    教师用户ID
     */
    @Override
    public void add(CourseModel courseModel, String userId) {
        // 校验课程信息是否完整
        if (courseModel.getCourseName() == null) {
            log.info("课程名不能为空");
            throw new InvalidRequestException("课程名不能为空");
        } else if (courseModel.getCapacity() == null) {
            log.info("课程容量不能为空");
            throw new InvalidRequestException("课程容量不能为空");
        } else if (courseModel.getStudyTime() == null) {
            log.info("课程时间不能为空");
            throw new InvalidRequestException("课程时间不能为空");
        } else if (courseModel.getCategory() == null) {
            log.info("课程类别不能为空");
            throw new InvalidRequestException("课程类别不能为空");
        } else if (courseModel.getCourseSort() == null) {
            throw new InvalidRequestException("课程类型不能为空");
        }
        courseModel.setTeacher(commonMapper.getTeacherByUserId(userId));
        courseMapper.add(courseModel);
    }

    /**
     * 更新课程信息
     *
     * @param courseModel 课程模型对象
     * @param userId      用户ID
     */
    @Override
    public void update(CourseModel courseModel, String userId) {
        List<CourseModel> list = commonMapper.getCourseModelByUserId(userId);
        // 检查用户是否有课程以及指定的课程是否存在
        CourseModel course= commonMapper.getCourseModelByCourseId(courseModel.getCourseId());
        if (list == null) {
            throw new ResultNotFoundException("您没有课程");
        } else if (!list.contains(course)) {
            throw new ResultNotFoundException("没有该课程");
        }
        courseMapper.update(courseModel);
    }

    /**
     * 删除课程信息
     *
     * @param courseModel 课程模型对象
     */
    @Override
    public void delete(CourseModel courseModel) {
        courseMapper.delete(courseModel);
    }

    /**
     * 根据用户ID分页查询教师的课程信息
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param userId   用户ID
     * @return 返回分页结果对象
     */
    @Override
    public PageBean getTeacherCourse(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page, pageSize);
        List<CourseModel> list = commonMapper.getCourseModelByUserId(userId);
        PageInfo<CourseModel> pageInfo = new PageInfo<>(list);
        // 返回分页结果
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 通过Excel添加课程信息
     *
     * @param list 课程模型列表
     */
    @Override
    public void addExcel(List<CourseModel> list, String userId) {
        for (CourseModel courseModel : list) {
            if (courseModel.getCourseName() == null) {
                log.info("课程名不能为空");
                throw new InvalidRequestException("课程名不能为空");
            } else if (courseModel.getCapacity() == null) {
                log.info("课程容量不能为空");
                throw new InvalidRequestException("课程容量不能为空");
            } else if (courseModel.getStudyTime() == null) {
                log.info("课程时间不能为空");
                throw new InvalidRequestException("课程时间不能为空");
            } else if (courseModel.getCategory() == null) {
                log.info("课程类别不能为空");
                throw new InvalidRequestException("课程类别不能为空");
            } else if (courseModel.getCourseSort() == null) {
                throw new InvalidRequestException("课程类型不能为空");
            }
            courseModel.setTeacher(commonMapper.getTeacherByUserId(userId));
            courseMapper.add(courseModel);
        }
    }
}
