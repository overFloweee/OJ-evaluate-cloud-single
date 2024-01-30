package com.hjw.eoj.judge.sandbox;


import com.hjw.eoj.judge.sandbox.model.ExecuteCodeRequest;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱，执行代码
 */
public interface CodeSandbox
{

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
