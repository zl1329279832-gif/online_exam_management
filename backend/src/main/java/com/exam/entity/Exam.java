package com.exam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exam")
public class Exam extends BaseEntity {

    private String examName;

    private Long paperId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private String description;

    private Long createUserId;
}
