package com.hjw.eoj.controller;


import com.hjw.eoj.common.ErrorCode;
import com.hjw.eoj.common.R;
import com.hjw.eoj.exception.BusinessException;
import com.hjw.eoj.model.dto.questionsubmit.QuestionAddSubmitRequest;
import com.hjw.eoj.service.QuestionSubmitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
@RequiredArgsConstructor
public class QuestionSubmitController
{

    private final QuestionSubmitService questionSubmitService;


    @PostMapping("/")
    private R<Long> doQuestionSubmit(@RequestBody QuestionAddSubmitRequest questionAddSubmitRequest)
    {
        if (questionAddSubmitRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = questionSubmitService.questionSubmit(questionAddSubmitRequest);

        return R.success(result);
    }

}
