package com.hjw.eoj.judge;


import com.hjw.eoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;


public interface JudgeService
{
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
