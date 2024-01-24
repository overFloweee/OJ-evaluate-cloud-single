package com.hjw.eoj.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hjw.eoj.model.dto.questionsubmit.QuestionAddSubmitRequest;
import com.hjw.eoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
* @author 86157
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-01-22 21:50:08
*/
public interface QuestionSubmitService extends IService<QuestionSubmit>
{

    /**
     * 题目提交
     * @param questionAddSubmitRequest
     * @return 提交记录的id
     */
    long questionSubmit(QuestionAddSubmitRequest questionAddSubmitRequest);
}
