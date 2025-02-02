package com.healthy.BackEnd.Controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.healthy.BackEnd.Service.PsychologistService;
import com.healthy.BackEnd.dto.PsychologistDTO;
import com.healthy.BackEnd.exception.GlobalExceptionHandel;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class PsychologistController {
    @Autowired
    public PsychologistService psychologistService;

    @SecurityRequirement(name="Bearer Authentication")
    @GetMapping("/psychologists/users/appointment")
    public ResponseEntity<?> getAllPsychologist() {
        List<PsychologistDTO> psychologistDTO = psychologistService.getAllPsychologistDTO();
        if (!psychologistDTO.isEmpty()) {
            return ResponseEntity.ok(psychologistDTO); 
        }
        return ResponseEntity.noContent().build(); 
    }

    // @SecurityRequirement(name="Bearer Authentication")
    // @GetMapping("/psychologist/{id}")
    // public ResponseEntity<?> getPsychologistById (@PathVariable String id) {
    //     PsychologistDTO psychologistDTO = psychologistService.getPsychologistById(id);
    //     return ResponseEntity.ok(psychologistDTO);
    // }

    

}



