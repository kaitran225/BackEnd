package com.healthy.backend.controller;

import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.service.PsychologistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Psychologist", description = "Psychologist related APIs")
public class PsychologistController {
    @Autowired
    public PsychologistService psychologistService;

    @Operation(
            summary = "Get all psychologists",
            description = "Returns a list of all registered psychologists."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/psychologists")
    public ResponseEntity<List<PsychologistResponse>> getAllPsychologist() {
        List<PsychologistResponse> psychologistResponse = psychologistService.getAllPsychologistDTO();
        if (!psychologistResponse.isEmpty()) {
            return ResponseEntity.ok(psychologistResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get psychologist by ID",
            description = "Returns the psychologist with the specified ID."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/psychologist/{id}")
    public ResponseEntity<PsychologistResponse> getPsychologistById(@Valid @PathVariable String id) {
        PsychologistResponse psychologistResponse = psychologistService.getPsychologistById(id);
        if (psychologistResponse != null) {
            return ResponseEntity.ok(psychologistResponse);
        }
        return ResponseEntity.noContent().build();
    }
}



