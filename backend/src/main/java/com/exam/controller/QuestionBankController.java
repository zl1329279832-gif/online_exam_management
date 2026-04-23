package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.Result;
import com.exam.entity.QuestionBank;
import com.exam.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questionBank")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    @GetMapping("/page")
    public Result<Page<QuestionBank>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(questionBankService.page(pageNum, pageSize, keyword));
    }

    @GetMapping("/{id}")
    public Result<QuestionBank> getById(@PathVariable Long id) {
        return Result.success(questionBankService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody QuestionBank questionBank) {
        questionBankService.save(questionBank);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody QuestionBank questionBank) {
        questionBankService.update(questionBank);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        questionBankService.delete(id);
        return Result.success();
    }
}
