package com.hjw.eoj.judge.sandbox;


import com.hjw.eoj.judge.sandbox.model.ExecuteCodeRequest;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码沙箱 代理
 * 增强功能
 */
@AllArgsConstructor
@Slf4j
public class CodeSandboxProxy implements CodeSandbox
{

    private final CodeSandbox codeSandbox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest)
    {
        log.info("代码沙箱请求信息:" + executeCodeRequest);
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱请求信息:" + executeCodeResponse);
        return executeCodeResponse;


    }

}
