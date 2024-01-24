package com.hjw.eoj.controller;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjw.eoj.annotation.AuthCheck;
import com.hjw.eoj.common.R;
import com.hjw.eoj.common.ErrorCode;
import com.hjw.eoj.constant.UserConstant;
import com.hjw.eoj.exception.BusinessException;
import com.hjw.eoj.exception.ThrowUtils;
import com.hjw.eoj.model.dto.question.QuestionAddRequest;
import com.hjw.eoj.model.dto.question.QuestionEditRequest;
import com.hjw.eoj.model.dto.question.QuestionQueryRequest;
import com.hjw.eoj.model.dto.question.QuestionUpdateRequest;
import com.hjw.eoj.model.dto.questionsubmit.QuestionAddSubmitRequest;
import com.hjw.eoj.model.entity.Question;
import com.hjw.eoj.model.entity.QuestionSubmit;
import com.hjw.eoj.model.entity.User;
import com.hjw.eoj.model.vo.QuestionVO;
import com.hjw.eoj.service.QuestionService;
import com.hjw.eoj.service.QuestionSubmitService;
import com.hjw.eoj.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/question")
@Slf4j
@RequiredArgsConstructor
public class QuestionController
{

    private final QuestionService questionService;
    private final UserService userService;
    private final HttpServletRequest request;
    private final QuestionSubmitService questionSubmitService;

    /**
     * 根据 id 获取题目信息（脱敏）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public R<QuestionVO> getQuestionVOById(Long id)
    {
        questionSubmitService.getById(null);
        if (id == null || id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Question question = questionService.getById(id);
        if (question == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        QuestionVO questionVO = questionService.getQuestionVO(question);

        return R.success(questionVO);
    }

    @PostMapping("/add")
    public R<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest)
    {
        if (questionAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long questionId = questionService.addQuestion(questionAddRequest);
        return R.success(questionId);
    }


    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    public R<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest)
    {
        // 获取分页数据
        long pageNum = questionQueryRequest.getCurrent();
        long pageSize = questionQueryRequest.getPageSize();

        // 进行分页查询 题目信息
        Page<Question> questionPage = questionService.page(new Page<>(pageNum, pageSize),
                questionService.getQueryWrapper(questionQueryRequest)
        );
        // 转化题目类 成包装类

        List<QuestionVO> records = questionPage.getRecords().stream().map(questionService::getQuestionVO)
                                               .collect(Collectors.toList());
        // 获取并填充分页对象
        Page<QuestionVO> page = new Page<>(pageNum, pageSize, questionPage.getTotal());
        page.setRecords(records);
        return R.success(page);
    }


    /**
     * 删除（仅管理员或本人）
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R<Boolean> deleteQuestion(@PathVariable("id") Long id)
    {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.NOT_FOUND_ERROR);

        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);

        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 判断是否是 管理员 或者 本人
        if (!userService.isAdmin(request) && !oldQuestion.getUserId().equals(userId))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        boolean result = questionService.removeById(id);

        return R.success(result);
    }


    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public R<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest)
    {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        // 设置其他信息
        question.setTags(JSONUtil.toJsonStr(questionUpdateRequest.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(questionUpdateRequest.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(questionUpdateRequest.getJudgeConfig()));

        long questionId = questionUpdateRequest.getId();

        // 判断是否存在
        Question oldQuestion = questionService.getById(questionId);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);

        boolean result = questionService.updateById(question);
        return R.success(result);
    }

    /**
     * 根据 id 获取（未脱敏） 仅本人和管理员使用
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public R<Question> getQuestionById(long id)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        // 不是 本人或管理员，则不能直接获取所有信息
        if (!question.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return R.success(question);
    }

    /**
     * 分页获取题目（未脱敏） 仅 管理员使用
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public R<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest)
    {
        if (questionQueryRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<Question> questionPage = new Page<>(questionQueryRequest.getCurrent(), questionQueryRequest.getPageSize());

        LambdaQueryWrapper<Question> queryWrapper = questionService.getQueryWrapper(questionQueryRequest);
        questionService.page(questionPage, queryWrapper);

        return R.success(questionPage);
    }


    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public R<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest)
    {
        if (questionQueryRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        // 分页
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest)
        );
        return R.success(questionService.getQuestionVOPage(questionPage));
    }


    /**
     * 编辑题目信息（用户）
     *
     * @param questionEditRequest
     * @return
     */
    @PostMapping("/edit")
    public R<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest)
    {
        if (questionEditRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean isEdit = questionService.editQuestion(questionEditRequest);
        return R.success(isEdit);

    }




    // /**
    //  * 获取所有标签
    //  * @return
    //  */
    // @GetMapping("/tags")
    // public R<List<String>> getQuestionTags() {
    //     List<String> tags = questionService.getQuestionTags();
    //     return R.ok(tags);
    // }
    //
    // /**
    //  * 根据 id 获取（脱敏）
    //  * @param id
    //  * @return
    //  */
    // @GetMapping("/{id}/vo/safe")
    // public R<SafeQuestionVo> getSafeQuestionVoById(@PathVariable("id") @Min(1) Long id) {
    //     Question question = questionService.getById(id);
    //     if (question == null) {
    //         throw new BusinessException(AppHttpCodeEnum.NOT_EXIST);
    //     }
    //     UserInfoVo currentUser = UserUtils.getCurrentUser(userFeignService.getCurrentUser());
    //     SafeQuestionVo vo = questionService.objToVo(question, currentUser.getUid());
    //     return R.ok(vo);
    // }
    //
    // /**
    //  * 分页获取列表（封装类）
    //  * @param questionQueryRequest
    //  * @return
    //  */
    // @PostMapping("/page/vo/safe")
    // public R<Page<SafeQuestionVo>> listSafeQuestionVoByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
    //     long pageNum = questionQueryRequest.getPageNum();
    //     long pageSize = questionQueryRequest.getPageSize();
    //     // 限制爬虫
    //     ThrowUtils.throwIf(pageSize > 20, AppHttpCodeEnum.PARAMS_ERROR);
    //
    //     Wrapper<Question> queryWrapper = questionService.getQueryWrapper(questionQueryRequest);
    //     if(queryWrapper != null){
    //         Page<Question> questionPage = questionService.page(new Page<>(pageNum, pageSize), queryWrapper);
    //
    //         UserInfoVo currentUser = UserUtils.getCurrentUser(userFeignService.getCurrentUser());
    //         List<SafeQuestionVo> records = questionPage.getRecords().stream()
    //                                                  .map(question -> questionService.objToVo(question, currentUser
    //                                                  .getUid()))
    //                                                  .collect(Collectors.toList());
    //         Page<SafeQuestionVo> page = new Page<>(pageNum, pageSize, questionPage.getTotal());
    //         page.setRecords(records);
    //         return R.ok(page);
    //     } else {
    //         Page<SafeQuestionVo> page = new Page<>(pageNum, pageSize, 0);
    //         return R.ok(page);
    //     }
    // }
    //
    // /**
    //  * 编辑（用户）
    //  * @param questionEditRequest
    //  * @param request
    //  * @return
    //  */
    // @PostMapping("/edit")
    // public R editQuestion(@RequestBody @NotNull @Valid QuestionEditRequest questionEditRequest,
    //                       HttpServletRequest request) {
    //     Question question = new Question();
    //     BeanUtils.copyProperties(questionEditRequest, question);
    //
    //     // 判断是否存在
    //     long id = questionEditRequest.getId();
    //     Question oldQuestion = questionService.getById(id);
    //     ThrowUtils.throwIf(oldQuestion == null, AppHttpCodeEnum.NOT_EXIST);
    //
    //     UserInfoVo currentUser = UserUtils.getCurrentUser(userFeignService.getCurrentUser());
    //
    //     // 仅本人或管理员可编辑
    //     if (!oldQuestion.getUserId().equals(currentUser.getUid()) && !UserUtils.isAdmin(currentUser)) {
    //         throw new BusinessException(AppHttpCodeEnum.NO_AUTH);
    //     }
    //     questionService.updateById(question);
    //     return R.ok();
    // }


}
