package com.hjw.eoj.judge;

import com.hjw.eoj.judge.strategy.DefaultJudgeStrategyImpl;
import com.hjw.eoj.judge.strategy.JavaLanguageJudgeStrategyImpl;
import com.hjw.eoj.judge.strategy.JudgeContext;
import com.hjw.eoj.judge.strategy.JudgeStrategy;
import com.hjw.eoj.model.dto.questionsubmit.JudgeInfo;
import com.hjw.eoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理
 * 判断使用哪些判题策略
 */
@Service
public class JudgeManage
{
    /**
     * 执行判题策略
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext)
    {
        String language = judgeContext.getQuestionSubmit().getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategyImpl();
        if ("java".equals(language))
        {
            judgeStrategy = new JavaLanguageJudgeStrategyImpl();
        }

        return judgeStrategy.doJudge(judgeContext);
    }
}
