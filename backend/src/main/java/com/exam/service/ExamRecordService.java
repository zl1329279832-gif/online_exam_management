package com.exam.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.context.UserContext;
import com.exam.entity.*;
import com.exam.exception.BusinessException;
import com.exam.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ExamRecordService {

    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ExamPaperQuestionMapper examPaperQuestionMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public Page<ExamRecord> page(Integer pageNum, Integer pageSize, Long examId) {
        Page<ExamRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        if (examId != null) {
            wrapper.eq(ExamRecord::getExamId, examId);
        }
        wrapper.orderByDesc(ExamRecord::getCreateTime);
        return examRecordMapper.selectPage(page, wrapper);
    }

    public Page<ExamRecord> myRecords(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();
        Page<ExamRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getUserId, userId);
        wrapper.orderByDesc(ExamRecord::getCreateTime);
        return examRecordMapper.selectPage(page, wrapper);
    }

    @Transactional
    public Long startExam(Long examId) {
        Long userId = UserContext.getUserId();
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new BusinessException("考试不存在");
        }
        if (exam.getStatus() != 1) {
            throw new BusinessException("考试未开始或已结束");
        }
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getExamId, examId);
        wrapper.eq(ExamRecord::getUserId, userId);
        ExamRecord existRecord = examRecordMapper.selectOne(wrapper);
        if (existRecord != null) {
            if (existRecord.getStatus() >= 2) {
                throw new BusinessException("您已提交过考试");
            }
            return existRecord.getId();
        }
        ExamRecord record = new ExamRecord();
        record.setExamId(examId);
        record.setUserId(userId);
        record.setPaperId(exam.getPaperId());
        record.setStartTime(LocalDateTime.now());
        record.setStatus(1);
        examRecordMapper.insert(record);
        return record.getId();
    }

    @Transactional
    public void submitExam(Long recordId, String answers) {
        ExamRecord record = examRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException("记录不存在");
        }
        if (record.getStatus() >= 2) {
            throw new BusinessException("已提交过考试");
        }
        BigDecimal score = autoGrade(record.getPaperId(), answers);
        record.setAnswers(answers);
        record.setScore(score);
        record.setSubmitTime(LocalDateTime.now());
        record.setStatus(3);
        examRecordMapper.updateById(record);
    }

    private BigDecimal autoGrade(Long paperId, String answersJson) {
        BigDecimal totalScore = BigDecimal.ZERO;
        LambdaQueryWrapper<ExamPaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamPaperQuestion::getPaperId, paperId);
        List<ExamPaperQuestion> paperQuestions = examPaperQuestionMapper.selectList(wrapper);
        JSONObject answers = JSONUtil.parseObj(answersJson);
        for (ExamPaperQuestion pq : paperQuestions) {
            Question question = questionMapper.selectById(pq.getQuestionId());
            String userAnswer = answers.getStr(String.valueOf(question.getId()));
            if (userAnswer != null && userAnswer.equals(question.getAnswer())) {
                totalScore = totalScore.add(pq.getQuestionScore());
            }
        }
        return totalScore;
    }

    public ExamRecord getById(Long id) {
        return examRecordMapper.selectById(id);
    }
}
