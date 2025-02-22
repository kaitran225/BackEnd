package com.healthy.backend.dto.survey;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder


@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionOption1 {
    private int value;
    private String label;
    private String answerID;
}
