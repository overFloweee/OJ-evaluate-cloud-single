package com.hjw.eoj.judge.sandbox.impl;

import com.hjw.eoj.judge.sandbox.CodeSandbox;
import com.hjw.eoj.judge.sandbox.CodeSandboxFactory;
import com.hjw.eoj.judge.sandbox.CodeSandboxProxy;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeRequest;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeResponse;
import com.hjw.eoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExampleCodeSandboxTest
{

    @Test
    void executeCode()
    {
        ExampleCodeSandbox exampleCodeSandbox = new ExampleCodeSandbox();
        String code = "int main(){}";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> list = Arrays.asList("1 2", "3 4");

        ExecuteCodeRequest build = ExecuteCodeRequest.builder().code(code).language(language).inputList(list).build();

        ExecuteCodeResponse executeCodeResponse = exampleCodeSandbox.executeCode(build);

    }


    @Test
    void executeCodeFactory()
    {
        // 生成代码沙箱
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance();
        // 生成代码沙箱 代理类
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);

        String code = "int main(){}";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> list = Arrays.asList("1 2", "3 4");

        ExecuteCodeRequest build = ExecuteCodeRequest.builder().code(code).language(language).inputList(list).build();

         // 代理类 执行原始方法
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(build);

    }
}
