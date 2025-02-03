package com.healthy.BackEnd.Controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.healthy.BackEnd.Service.PsychologistService;
import com.healthy.BackEnd.DTO.Psychologist.PsychologistResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class PsychologistController {
    @Autowired
    public PsychologistService psychologistService;

    @SecurityRequirement(name="Bearer Authentication")
        @GetMapping("/psychologists")
    public ResponseEntity<List<PsychologistResponse>> getAllPsychologist() {
        List<PsychologistResponse> psychologistResponse = psychologistService.getAllPsychologistDTO();
        if (!psychologistResponse.isEmpty()) {
            return ResponseEntity.ok(psychologistResponse);
        }
        return ResponseEntity.noContent().build(); 
    }

     @SecurityRequirement(name="Bearer Authentication")
     @GetMapping("/psychologist/{id}")
     public ResponseEntity<PsychologistResponse> getPsychologistById (
             @Valid @PathVariable String id) {
         PsychologistResponse psychologistResponse = psychologistService.getPsychologistById(id);
         return ResponseEntity.ok(psychologistResponse);
     }
}



