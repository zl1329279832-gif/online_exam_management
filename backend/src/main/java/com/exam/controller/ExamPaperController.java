package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.Result;
import com.exam.entity.ExamPaper;
import com.exam.entity.ExamPaperQuestion;
import com.exam.service.ExamPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examPaper")
public class ExamPaperController {

    @Autowired
    private ExamPaperService examPaperService;

    @GetMapping("/page")
    public Result<Page<ExamPaper>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(examPaperService.page(pageNum, pageSize, keyword));
    }

    @GetMapping("/{id}")
    public Result<ExamPaper> getById(@PathVariable Long id) {
        return Result.success(examPaperService.getById(id));
    }

    @GetMapping("/{id}/questions")
    public Result<List<ExamPaperQuestion>> getQuestions(@PathVariable Long id) {
        return Result.success(examPaperService.getQuestions(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody ExamPaper examPaper) {
        examPaperService.save(examPaper);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody ExamPaper examPaper) {
        examPaperService.update(examPaper);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        examPaperService.delete(id);
        return Result.success();
    }

    @PostMapping("/{paperId}/addQuestion")
    public Result<Void> addQuestion(@PathVariable Long paperId, @RequestBody ExamPaperQuestion question) {
        examPaperService.addQuestion(paperId, question);
        return Result.success();
    }

    @DeleteMapping("/question/{id}")
    public Result<Void> removeQuestion(@PathVariable Long id) {
        examPaperService.removeQuestion(id);
        return Result.success();
    }
}
