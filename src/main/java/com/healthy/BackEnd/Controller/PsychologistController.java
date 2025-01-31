
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.healthy.BackEnd.Service.PsychologistService;
import com.healthy.BackEnd.dto.PsychologistDTO;
import com.healthy.BackEnd.exception.GlobalExceptionHandel;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/api/psychologist")

public class PsychologistController {
    @Autowired
    PsychologistService psychologistService;


    @GetMapping
    public ResponseEntity<?> getAllPsychologist() {
        List<PsychologistDTO> psychologistDTO = psychologistService.getAllPsychologistDTO();
        if (!psychologistDTO.isEmpty()) {
            return ResponseEntity.ok(psychologistDTO); 
        }
        return ResponseEntity.noContent().build(); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPsychologistById (@PathVariable String id) {
        PsychologistDTO psychologistDTO = psychologistService.getPsychogistById(id);
        return ResponseEntity.ok(psychologistDTO);
    }

}



