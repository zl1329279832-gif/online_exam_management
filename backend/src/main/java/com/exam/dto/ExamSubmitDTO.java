package com.exam.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ExamSubmitDTO {
    private Map<String, String> answers;
}
