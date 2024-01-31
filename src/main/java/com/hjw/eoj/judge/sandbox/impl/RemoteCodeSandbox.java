package com.hjw.eoj.judge.sandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.hjw.eoj.common.ErrorCode;
import com.hjw.eoj.exception.BusinessException;
import com.hjw.eoj.judge.sandbox.CodeSandbox;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeRequest;
import com.hjw.eoj.judge.sandbox.model.ExecuteCodeResponse;
import org.springframework.util.StringUtils;

/**
 * 远程调用代码的沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox
{

    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest)
    {
        System.out.println("调用远程代码沙箱");
        String url = "http://localhost:8081/executeCode";
        // 携带参数
        String jsonStr = JSONUtil.toJsonStr(executeCodeRequest);

        // 发送请求，携带请求头
        HttpResponse httpResponse = HttpUtil.createPost(url).header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(jsonStr).execute();

        String body = httpResponse.body();
        if (StrUtil.isBlank(body) || StrUtil.contains(body,"Error"))
        {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "execute remotesanbox error,message = " + body);
        }

        return JSONUtil.toBean(body, ExecuteCodeResponse.class);
    }
}
