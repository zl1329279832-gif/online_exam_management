package com.exam.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExamRecordVO {
    private Long id;
    private Long examId;
    private String examName;
    private Long userId;
    private String userName;
    private Long paperId;
    private LocalDateTime startTime;
    private LocalDateTime submitTime;
    private BigDecimal score;
    private Integer status;
    private LocalDateTime createTime;
}
