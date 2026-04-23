package com.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("exam_record")
public class ExamRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long examId;

    private Long userId;

    private Long paperId;

    private LocalDateTime startTime;

    private LocalDateTime submitTime;

    private BigDecimal score;

    private Integer status;

    private String answers;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
