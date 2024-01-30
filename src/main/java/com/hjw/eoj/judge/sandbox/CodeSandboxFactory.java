package com.hjw.eoj.judge.sandbox;


import com.hjw.eoj.judge.sandbox.impl.ExampleCodeSandbox;
import com.hjw.eoj.judge.sandbox.impl.RemoteCodeSandbox;
import com.hjw.eoj.judge.sandbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数，创建指定的代码沙箱实例）
 * 根据用户传入的参数，自动生成对应 实现类的代码沙箱
 */
public class CodeSandboxFactory
{

    public static CodeSandbox newInstance( )
    {
        return new ExampleCodeSandbox();
    }

    public static CodeSandbox newInstance(String type)
    {

        switch (type)
        {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }

     }
}
