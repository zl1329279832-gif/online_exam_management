package com.exam.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.Result;
import com.exam.dto.ExamSubmitDTO;
import com.exam.entity.ExamRecord;
import com.exam.service.ExamRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/examRecord")
public class ExamRecordController {

    @Autowired
    private ExamRecordService examRecordService;

    @GetMapping("/page")
    public Result<Page<ExamRecord>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long examId) {
        return Result.success(examRecordService.page(pageNum, pageSize, examId));
    }

    @GetMapping("/my")
    public Result<Page<ExamRecord>> myRecords(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(examRecordService.myRecords(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<ExamRecord> getById(@PathVariable Long id) {
        return Result.success(examRecordService.getById(id));
    }

    @PostMapping("/start/{examId}")
    public Result<Long> startExam(@PathVariable Long examId) {
        return Result.success(examRecordService.startExam(examId));
    }

    @PostMapping("/submit/{recordId}")
    public Result<Void> submitExam(@PathVariable Long recordId, @RequestBody ExamSubmitDTO examSubmitDTO) {
        String answersJson = JSONUtil.toJsonStr(examSubmitDTO.getAnswers());
        examRecordService.submitExam(recordId, answersJson);
        return Result.success();
    }
}
