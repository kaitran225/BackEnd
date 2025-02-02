package com.healthy.BackEnd.Controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthy.BackEnd.Service.PsychologistService;
import com.healthy.BackEnd.DTO.PsychologistDTO;



@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class PsychologistController {
    @Autowired
    PsychologistService psychologistService;

    @SecurityRequirement(name="Bearer Authentication")
    @GetMapping("/psychologists")
    public ResponseEntity<?> getAllPsychologist() {
        List<PsychologistDTO> psychologistDTO = psychologistService.getAllPsychologistDTO();
        if (!psychologistDTO.isEmpty()) {
            return ResponseEntity.ok(psychologistDTO); 
        }
        return ResponseEntity.noContent().build(); 
    }

    @SecurityRequirement(name="Bearer Authentication")
    @GetMapping("/psychologist/{id}")
    public ResponseEntity<?> getPsychologistById (@PathVariable String id) {
        PsychologistDTO psychologistDTO = psychologistService.getPsychogistById(id);
        return ResponseEntity.ok(psychologistDTO);
    }

}



