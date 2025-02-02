package com.healthy.BackEnd.DTO.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.BackEnd.DTO.PsychologistDTO;
import com.healthy.BackEnd.DTO.StudentDTO;
import com.healthy.BackEnd.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersRequest {
    private String userId;
    private String username;
    private String email;
}