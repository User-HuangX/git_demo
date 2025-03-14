package com.hitwh.usersearch;

import com.hitwh.mapper.CommonMapper;
import com.hitwh.mapper.CommentMapper;
import com.hitwh.model.UserModel;
import com.hitwh.request.CommentRequest;
import com.hitwh.request.PageBean;
import com.hitwh.response.CommentResponse;
import com.hitwh.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.github.pagehelper.Page;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;

class CommentServiceImplTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommonMapper commonMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentRequest commentRequest;
    private final String userId = "user123";
    private final UserModel userModel = new UserModel();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentRequest = new CommentRequest();
        commentRequest.setId(1);
        commentRequest.setType("TEACHER");
        commentRequest.setCommentator(1001);

        userModel.setMyId(2001);
    }

    @Test
    void checkComment() {
        // 执行方法
        commentService.checkComment(commentRequest);

        // 验证交互
        verify(commentMapper, times(1)).checkComment(commentRequest);
    }

    @Test
    void commentTo() {
        // 模拟CommonMapper返回用户模型
        when(commonMapper.getMyModelByUserId(userId)).thenReturn(userModel);

        // 执行方法
        commentService.commentTo(commentRequest, userId);

        // 验证Commentator被正确设置
        assertEquals(userModel.getMyId(), commentRequest.getCommentator());
        // 验证插入方法被调用
        verify(commentMapper, times(1)).commentTo(commentRequest);
    }

    @Test
    void showAll() {
        // 模拟分页数据
        Page<CommentRequest> mockPage = new Page<>();
        mockPage.addAll(Arrays.asList(new CommentRequest(), new CommentRequest()));
        mockPage.setTotal(2);
        when(commentMapper.showAll()).thenReturn(mockPage);

        // 执行方法
        PageBean result = commentService.showAll(1, 10);

        // 验证分页结果
        assertEquals(2, result.getTotal());
    }

    @Test
    void showPublicCommentWithTeacherType() {
        commentRequest.setType("TEACHER");
        commentRequest.setCommentator(1001);

        // 模拟CommonMapper返回教师名称
        when(commonMapper.getTeacherById(1001)).thenReturn("张老师");

        // 模拟分页数据
        Page<CommentResponse> mockPage = new Page<>();
        mockPage.addAll(Arrays.asList(new CommentResponse(), new CommentResponse()));
        mockPage.setTotal(2);
        when(commentMapper.showPublicComment(commentRequest.getCommentator(),commentRequest.getType())).thenReturn(mockPage);

        // 执行方法
        PageBean result = commentService.showPublicComment(commentRequest.getCommentator(),commentRequest.getType(), 1, 10);

        // 验证结果
        assertEquals(2, result.getTotal());
        // 验证教师查询被调用
        verify(commonMapper, times(1)).getTeacherById(1001);
    }

    @Test
    void showPublicCommentWithCourseType() {
        commentRequest.setType("COURSE");
        commentRequest.setCommentator(2002);

        // 模拟CommonMapper返回课程名称
        when(commonMapper.getCourseById(2002)).thenReturn("数学课");

        // 模拟分页数据
        Page<CommentResponse> mockPage = new Page<>();
        mockPage.addAll(Arrays.asList(new CommentResponse()));
        mockPage.setTotal(1);
        when(commentMapper.showPublicComment(commentRequest.getCommentator(),commentRequest.getType())).thenReturn(mockPage);

        // 执行方法
        PageBean result = commentService.showPublicComment(commentRequest.getCommentator(),commentRequest.getType(), 1, 10);

        // 验证结果
        assertEquals(1, result.getTotal());
        // 验证课程查询被调用
        verify(commonMapper, times(1)).getCourseById(2002);
    }
}