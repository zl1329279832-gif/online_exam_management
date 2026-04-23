package com.exam.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("question")
public class Question extends BaseEntity {

    private Long bankId;

    private Integer questionType;

    private Integer difficulty;

    private String questionContent;

    @TableField("options")
    private String options;

    private String answer;

    private String analysis;

    private BigDecimal score;

    private Long createUserId;
}
