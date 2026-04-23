package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.Result;
import com.exam.entity.Question;
import com.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/page")
    public Result<Page<Question>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long bankId,
            @RequestParam(required = false) Integer questionType) {
        return Result.success(questionService.page(pageNum, pageSize, bankId, questionType));
    }

    @GetMapping("/{id}")
    public Result<Question> getById(@PathVariable Long id) {
        return Result.success(questionService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Question question) {
        questionService.save(question);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Question question) {
        questionService.update(question);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return Result.success();
    }
}
