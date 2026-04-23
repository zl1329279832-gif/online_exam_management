package com.exam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exam_paper")
public class ExamPaper extends BaseEntity {

    private String paperName;

    private String description;

    private BigDecimal totalScore;

    private BigDecimal passScore;

    private Integer duration;

    private Long createUserId;
}
