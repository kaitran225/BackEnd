package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.entity.Programs;
import com.healthy.BackEnd.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {

    @Autowired
    private ProgramRepository programRepository;

    public List<Programs> getProgramsByUserId(String userId) {
        return programRepository.findByManagedByStaffID(userId);
    }
}
