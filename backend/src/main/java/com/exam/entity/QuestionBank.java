package com.exam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("question_bank")
public class QuestionBank extends BaseEntity {

    private String bankName;

    private String description;

    private Long createUserId;
}
