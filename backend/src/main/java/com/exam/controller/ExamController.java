package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.Result;
import com.exam.entity.Exam;
import com.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/page")
    public Result<Page<Exam>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        return Result.success(examService.page(pageNum, pageSize, status));
    }

    @GetMapping("/{id}")
    public Result<Exam> getById(@PathVariable Long id) {
        return Result.success(examService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Exam exam) {
        examService.save(exam);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Exam exam) {
        examService.update(exam);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        examService.delete(id);
        return Result.success();
    }
}
