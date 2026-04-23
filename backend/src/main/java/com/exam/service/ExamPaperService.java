package com.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.context.UserContext;
import com.exam.entity.ExamPaper;
import com.exam.entity.ExamPaperQuestion;
import com.exam.entity.Question;
import com.exam.mapper.ExamPaperMapper;
import com.exam.mapper.ExamPaperQuestionMapper;
import com.exam.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamPaperService {

    @Autowired
    private ExamPaperMapper examPaperMapper;

    @Autowired
    private ExamPaperQuestionMapper examPaperQuestionMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public Page<ExamPaper> page(Integer pageNum, Integer pageSize, String keyword) {
        Page<ExamPaper> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ExamPaper> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(ExamPaper::getPaperName, keyword);
        }
        wrapper.orderByDesc(ExamPaper::getCreateTime);
        return examPaperMapper.selectPage(page, wrapper);
    }

    public ExamPaper getById(Long id) {
        return examPaperMapper.selectById(id);
    }

    @Transactional
    public void save(ExamPaper examPaper) {
        examPaper.setCreateUserId(UserContext.getUserId());
        examPaperMapper.insert(examPaper);
    }

    public void update(ExamPaper examPaper) {
        examPaperMapper.updateById(examPaper);
    }

    @Transactional
    public void delete(Long id) {
        examPaperMapper.deleteById(id);
        LambdaQueryWrapper<ExamPaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamPaperQuestion::getPaperId, id);
        examPaperQuestionMapper.delete(wrapper);
    }

    public List<ExamPaperQuestion> getQuestions(Long paperId) {
        LambdaQueryWrapper<ExamPaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamPaperQuestion::getPaperId, paperId);
        wrapper.orderByAsc(ExamPaperQuestion::getQuestionOrder);
        return examPaperQuestionMapper.selectList(wrapper);
    }

    @Transactional
    public void addQuestion(Long paperId, List<ExamPaperQuestion> question) {
        question.setPaperId(paperId);
        examPaperQuestionMapper.insert(question);
    }

    @Transactional
    public void removeQuestion(Long id) {
        examPaperQuestionMapper.deleteById(id);
    }
}
