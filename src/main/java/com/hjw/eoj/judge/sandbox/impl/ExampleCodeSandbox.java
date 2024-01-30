package com.hjw.eoj.judge.sandbox.impl;

import com.hjw.eoj.judge.sandbox.CodeSandbox;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeRequest;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeResponse;
import com.hjw.eoj.model.dto.questionsubmit.JudgeInfo;
import com.hjw.eoj.model.enums.JudgeInfoEnum;
import com.hjw.eoj.model.enums.QuestionSubmitStatusEnum;

import java.util.Arrays;
import java.util.List;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandbox implements CodeSandbox
{

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest)
    {
        List<String> inputList = executeCodeRequest.getInputList();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(Arrays.asList("3 4","6 7"));
        executeCodeResponse.setMessage("示例代码沙箱执行");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoEnum.ACCEPTED.getValue());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);

        return executeCodeResponse;
    }
}
