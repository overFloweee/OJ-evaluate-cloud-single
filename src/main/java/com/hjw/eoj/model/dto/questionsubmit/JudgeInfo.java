package com.hjw.eoj.model.dto.questionsubmit;


import lombok.Data;

/**
 * 判题运行信息
 */
@Data
public class JudgeInfo
{
    /**
     * 判题运行信息
     */
    private String message;


    /**
     * 程序消耗内存
     */
    private Long memory;
    /**
     * 程序运行时间
     */
    private Long time;
}
