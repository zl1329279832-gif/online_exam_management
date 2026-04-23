package com.exam.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.context.UserContext;
import com.exam.entity.*;
import com.exam.exception.BusinessException;
import com.exam.mapper.*;
import com.exam.vo.ExamRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private SysUserMapper sysUserMapper;

    public Page<ExamRecordVO> page(Integer pageNum, Integer pageSize, Long examId, Long userId) {
        Page<ExamRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        if (examId != null) {
            wrapper.eq(ExamRecord::getExamId, examId);
        }
        if (userId != null) {
            wrapper.eq(ExamRecord::getUserId, userId);
        }
        wrapper.orderByDesc(ExamRecord::getCreateTime);
        Page<ExamRecord> recordPage = examRecordMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<ExamRecordVO> voPage = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        if (recordPage.getRecords().isEmpty()) {
            return voPage;
        }

        // 获取关联的考试和用户数据
        Set<Long> examIds = recordPage.getRecords().stream().map(ExamRecord::getExamId).collect(Collectors.toSet());
        Set<Long> userIds = recordPage.getRecords().stream().map(ExamRecord::getUserId).collect(Collectors.toSet());

        Map<Long, Exam> examMap = examMapper.selectBatchIds(examIds).stream()
                .collect(Collectors.toMap(Exam::getId, e -> e));
        Map<Long, SysUser> userMap = sysUserMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(SysUser::getId, u -> u));

        List<ExamRecordVO> voList = recordPage.getRecords().stream().map(record -> {
            ExamRecordVO vo = new ExamRecordVO();
            BeanUtils.copyProperties(record, vo);
            Exam exam = examMap.get(record.getExamId());
            if (exam != null) {
                vo.setExamName(exam.getExamName());
            }
            SysUser user = userMap.get(record.getUserId());
            if (user != null) {
                vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            }
            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
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
