package com.hjw.eoj.controller;


import com.hjw.eoj.common.ErrorCode;
import com.hjw.eoj.common.R;
import com.hjw.eoj.exception.BusinessException;
import com.hjw.eoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.hjw.eoj.service.QuestionSubmitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
@RequiredArgsConstructor
@Deprecated
public class QuestionSubmitController
{

    private final QuestionSubmitService questionSubmitService;


    @PostMapping("/")
    private R<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest)
    {
        if (questionSubmitAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest);

        return R.success(result);
    }

}
