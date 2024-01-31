package com.hjw.eoj.judge.sandbox.impl;

import com.hjw.eoj.judge.sandbox.CodeSandbox;
import com.hjw.eoj.judge.sandbox.CodeSandboxFactory;
import com.hjw.eoj.judge.sandbox.CodeSandboxProxy;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeRequest;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeResponse;
import com.hjw.eoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExampleCodeSandboxTest
{

    @Value("${codesandbox.type:example}")
    private String type;

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
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        // 生成代码沙箱 代理类
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);

        String code = "public class Main\n" + "{\n" + "    public static void main(String[] args)\n" + "    {\n" + "        int arg1 = Integer.parseInt(args[0]);\n" + "        int arg2 = Integer.parseInt(args[1]);\n" + "        System.out.println(\"参数1：\" + arg1);\n" + "        System.out.println(\"参数2：\" + arg2);\n" + "        System.out.println(\"结果是：\" + (arg1 + arg2));\n" + "    }\n" + "}\n" + "\n";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> list = Arrays.asList("11 88", "3 4");

        ExecuteCodeRequest build = ExecuteCodeRequest.builder().code(code).language(language).inputList(list).build();

        // 代理类 执行原始方法
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(build);

    }
}
