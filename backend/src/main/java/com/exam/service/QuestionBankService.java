package com.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.context.UserContext;
import com.exam.entity.QuestionBank;
import com.exam.mapper.QuestionBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionBankService {

    @Autowired
    private QuestionBankMapper questionBankMapper;

    public Page<QuestionBank> page(Integer pageNum, Integer pageSize, String keyword) {
        Page<QuestionBank> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<QuestionBank> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(QuestionBank::getBankName, keyword);
        }
        wrapper.orderByDesc(QuestionBank::getCreateTime);
        return questionBankMapper.selectPage(page, wrapper);
    }

    public QuestionBank getById(Long id) {
        return questionBankMapper.selectById(id);
    }

    public void save(QuestionBank questionBank) {
        questionBank.setCreateUserId(UserContext.getUserId());
        questionBankMapper.insert(questionBank);
    }

    public void update(QuestionBank questionBank) {
        questionBankMapper.updateById(questionBank);
    }

    public void delete(Long id) {
        questionBankMapper.deleteById(id);
    }
}
