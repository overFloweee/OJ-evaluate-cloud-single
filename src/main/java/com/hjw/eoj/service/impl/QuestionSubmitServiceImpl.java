package com.hjw.eoj.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjw.eoj.common.ErrorCode;
import com.hjw.eoj.constant.CommonConstant;
import com.hjw.eoj.exception.BusinessException;
import com.hjw.eoj.mapper.QuestionSubmitMapper;
import com.hjw.eoj.model.dto.question.QuestionQueryRequest;
import com.hjw.eoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.hjw.eoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.hjw.eoj.model.entity.Question;
import com.hjw.eoj.model.entity.QuestionSubmit;
import com.hjw.eoj.model.entity.User;
import com.hjw.eoj.model.enums.QuestionSubmitLanguageEnum;
import com.hjw.eoj.model.enums.QuestionSubmitStatusEnum;
import com.hjw.eoj.model.vo.QuestionSubmitVO;
import com.hjw.eoj.model.vo.QuestionVO;
import com.hjw.eoj.model.vo.UserVO;
import com.hjw.eoj.service.QuestionService;
import com.hjw.eoj.service.QuestionSubmitService;
import com.hjw.eoj.service.UserService;
import com.hjw.eoj.utils.SqlUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 86157
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-01-22 21:50:08
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService
{

    @Resource
    private QuestionService questionService;
    @Resource
    private UserService userService;
    @Resource
    private HttpServletRequest request;

    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest)
    {
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        if (questionSubmitAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        // 判断题目是否存在
        long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 复制部分字段属性
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtil.copyProperties(questionSubmitAddRequest, questionSubmit);
        // 填充信息
        questionSubmit.setUserId(userId);
        // 初始化状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");

        boolean isSave = this.save(questionSubmit);
        if (!isSave)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户提交失败");
        }

        return questionSubmit.getId();
    }


    /**
     * 获取查询包装类（根据用户传递的查询包装类，生成mybatis支持的查询类）
     */
    @Override
    public LambdaQueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest)
    {
        LambdaQueryWrapper<QuestionSubmit> queryWrapper = new LambdaQueryWrapper<>();
        if (questionSubmitQueryRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();


        queryWrapper.eq(StrUtil.isNotBlank(language), QuestionSubmit::getLanguage, language);
        queryWrapper.like(QuestionSubmitStatusEnum.getEnumByValue(status) != null, QuestionSubmit::getStatus, status);
        queryWrapper.eq(ObjectUtil.isNotEmpty(questionId), QuestionSubmit::getQuestionId, questionId);
        queryWrapper.eq(ObjectUtil.isNotEmpty(userId), QuestionSubmit::getUserId, userId);
        queryWrapper.eq(QuestionSubmit::getIsDelete, false);
        // 根据 查询条件进行排序 ，sortField - 需要排序的字段， sortOrder - 排序方式 asc、desc
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                question -> sortField
        );

        return queryWrapper;
    }


    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser)
    {
        long userId = loginUser.getId();

        // 1. questionSubmit 转 vo
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);


        // 根据用户信息，返回相应的 脱敏信息
        //     不是两者，则需要脱敏
        if (!userService.isAdmin(loginUser) && !questionSubmit.getUserId().equals(userId))
        {
            questionSubmitVO.setCode(null);
        }


        // todo 填充 QuestionSubmitVO 的 UserVO 和 QuestionVO

        return questionSubmitVO;
    }


    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage)
    {
        // 获取当前用户
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        // 取出信息
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        long current = questionSubmitPage.getCurrent();
        long total = questionSubmitPage.getTotal();
        long size = questionSubmitPage.getSize();
        // 填充page信息
        Page<QuestionSubmitVO> questionVOPage = new Page<>(current, size, total);


        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> this.getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionVOPage.setRecords(questionSubmitVOList);

        return questionVOPage;

    }

}




