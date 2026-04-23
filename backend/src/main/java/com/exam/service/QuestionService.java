package com.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.context.UserContext;
import com.exam.entity.Question;
import com.exam.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public Page<Question> page(Integer pageNum, Integer pageSize, Long bankId, Integer questionType) {
        Page<Question> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        if (bankId != null) {
            wrapper.eq(Question::getBankId, bankId);
        }
        if (questionType != null) {
            wrapper.eq(Question::getQuestionType, questionType);
        }
        wrapper.orderByDesc(Question::getCreateTime);
        return questionMapper.selectPage(page, wrapper);
    }

    public Question getById(Long id) {
        return questionMapper.selectById(id);
    }

    public void save(Question question) {
        question.setCreateUserId(UserContext.getUserId());
        questionMapper.insert(question);
    }

    public void update(Question question) {
        questionMapper.updateById(question);
    }

    public void delete(Long id) {
        questionMapper.deleteById(id);
    }
}
