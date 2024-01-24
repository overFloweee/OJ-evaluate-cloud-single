package com.hjw.eoj.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjw.eoj.common.ErrorCode;
import com.hjw.eoj.exception.BusinessException;
import com.hjw.eoj.mapper.QuestionSubmitMapper;
import com.hjw.eoj.model.dto.questionsubmit.QuestionAddSubmitRequest;
import com.hjw.eoj.model.entity.Question;
import com.hjw.eoj.model.entity.QuestionSubmit;
import com.hjw.eoj.model.entity.User;
import com.hjw.eoj.model.enums.QuestionSubmitLanguageEnum;
import com.hjw.eoj.model.enums.QuestionSubmitStatusEnum;
import com.hjw.eoj.service.QuestionService;
import com.hjw.eoj.service.QuestionSubmitService;
import com.hjw.eoj.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    public long questionSubmit(QuestionAddSubmitRequest questionAddSubmitRequest)
    {
        if (questionAddSubmitRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验编程语言是否合法
        String language = questionAddSubmitRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 判断题目是否存在
        long questionId = questionAddSubmitRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 复制部分字段属性
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtil.copyProperties(questionAddSubmitRequest, questionSubmit);
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
}




