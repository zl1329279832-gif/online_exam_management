package com.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.context.UserContext;
import com.exam.entity.Exam;
import com.exam.mapper.ExamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamService {

    @Autowired
    private ExamMapper examMapper;

    public Page<Exam> page(Integer pageNum, Integer pageSize, Integer status) {
        Page<Exam> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Exam::getStatus, status);
        }
        wrapper.orderByDesc(Exam::getCreateTime);
        return examMapper.selectPage(page, wrapper);
    }

    public Exam getById(Long id) {
        return examMapper.selectById(id);
    }

    public void save(Exam exam) {
        exam.setCreateUserId(UserContext.getUserId());
        examMapper.insert(exam);
    }

    public void update(Exam exam) {
        examMapper.updateById(exam);
    }

    public void delete(Long id) {
        examMapper.deleteById(id);
    }
}
