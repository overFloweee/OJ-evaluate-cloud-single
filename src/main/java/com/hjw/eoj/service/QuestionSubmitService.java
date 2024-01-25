package com.hjw.eoj.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjw.eoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.hjw.eoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.hjw.eoj.model.entity.QuestionSubmit;
import com.hjw.eoj.model.entity.User;
import com.hjw.eoj.model.vo.QuestionSubmitVO;

/**
 * @author 86157
 * @description 针对表【question_submit(题目提交)】的数据库操作Service
 * @createDate 2024-01-22 21:50:08
 */
public interface QuestionSubmitService extends IService<QuestionSubmit>
{

    /**
     * 提交代码
     *
     * @param questionSubmitAddRequest
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest);


    /**
     * 获取 提交题目 查询wrapper
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    LambdaQueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);


    /**
     * 提交代码 封装（脱敏）
     *
     * @param questionSubmit
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit,User loginUser);


    /**
     * 分页获取提交代码 封装
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> page);
}
