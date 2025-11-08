package com.skilluser.user.controller;

import com.skilluser.user.dto.CollegeRequestDTO;
import com.skilluser.user.dto.CollegeResponseDTO;
import com.skilluser.user.dto.StateResponseDTO;
import com.skilluser.user.dto.UniversityResponseDTO;
import com.skilluser.user.model.CollegeMaster;
import com.skilluser.user.model.StateMaster;
import com.skilluser.user.model.UniversityMaster;
import com.skilluser.user.service.CollegeMasterService;
import com.skilluser.user.service.StateMasterService;
import com.skilluser.user.service.UniversityMasterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/college")
@AllArgsConstructor
public class CollegeMasterController
{
    private final UniversityMasterService universityMasterService;
    private final StateMasterService stateMasterService;
    private final CollegeMasterService collegeMasterService;

    // Add university - Ajay -06-11-2025
    @PostMapping("/university")
    public ResponseEntity<?> saveUniversity(@RequestBody UniversityMaster universityMaster)
    {
        UniversityMaster university = universityMasterService.saveUniversity(universityMaster);
        return ResponseEntity.ok(Map.of(
                "message", "University added successfully!",
                "date",university
        ));
    }

    //Update University - Ajay - 06-11-2025
    @PutMapping("/university")
    public ResponseEntity<?> updateUniversity(@RequestBody UniversityMaster universityMaster)
    {
        try
        {
            UniversityResponseDTO updated =
                    universityMasterService.updateUniversity(universityMaster);
            return ResponseEntity.ok(Map.of(
                    "message", "University updated successfully!",
                    "data", updated
            ));
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }

    // Get all university
    @GetMapping("/university")
    public ResponseEntity<?> getAllUniversity()
    {
        try
        {
            List<UniversityResponseDTO> allUniversity = universityMasterService.getAllUniversities();
            return ResponseEntity.ok(Map.of(
                    "message", "University Fetched successfully!",
                    "data", allUniversity
            ));
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }


    // Get all university by states
    @GetMapping("/university/{stateId}")
    public ResponseEntity<?> getUniversitiesByState(@PathVariable Long stateId)
    {
        try
        {
            List<UniversityResponseDTO> universities = universityMasterService.getUniversitiesByState(stateId);
            return ResponseEntity.ok(Map.of(
                    "message", "Universities fetched successfully!",
                    "data", universities
            ));
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(404).body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }


    // Add state - Ajay -06-11-2025
    @PostMapping("/state")
    public ResponseEntity<?> saveState(@RequestBody StateMaster stateMaster)
    {
        StateMaster state = stateMasterService.saveState(stateMaster);
        return ResponseEntity.ok(Map.of(
                "message", "State added successfully!",
                "date",state
        ));
    }

    // Update State
    @PutMapping("/state")
    public ResponseEntity<?> updateState(@RequestBody StateMaster stateMaster)
    {
        try
        {
            StateResponseDTO updated =
                    stateMasterService.updateState(stateMaster);
            return ResponseEntity.ok(Map.of(
                    "message", "University updated successfully!",
                    "data", updated
            ));
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/state")
    public ResponseEntity<?> getAllState()
    {
        try
        {
            List<StateResponseDTO> allState = stateMasterService.getAllStates();
            return ResponseEntity.ok(Map.of(
                    "message", "University Fetched successfully!",
                    "data", allState
            ));
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }


    // Add college - Ajay -06-11-2025
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> saveCollege(@RequestBody CollegeRequestDTO dto)
    {
        CollegeResponseDTO college = collegeMasterService.saveCollege(dto);
        return ResponseEntity.ok(Map.of(
                "message", "College added successfully!",
                "data", college
        ));
    }

    // Update college
    @PutMapping("/update")
    public ResponseEntity<?> updateCollege(@RequestBody CollegeRequestDTO dto)
    {
        try
        {
            CollegeResponseDTO updated = collegeMasterService.updateCollege(dto);
            return ResponseEntity.ok(Map.of(
                    "message", "College updated successfully!",
                    "data", updated
            ));
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(404).body(Map.of(
                    "message", e.getMessage()
            ));
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "An error occurred while updating the college"
            ));
        }
    }

    // Get all colleges
    @GetMapping("/all")
    public ResponseEntity<?> getAllColleges()
    {
        List<CollegeResponseDTO> colleges = collegeMasterService.getAllColleges();
        return ResponseEntity.ok(Map.of(
                "message", "All colleges fetched successfully!",
                "data", colleges
        ));
    }

    // Get colleges by state
    @GetMapping("/{stateId}")
    public ResponseEntity<?> getCollegesByState(@PathVariable Long stateId)
    {
        try
        {
            List<CollegeResponseDTO> colleges = collegeMasterService.getCollegesByState(stateId);
            return ResponseEntity.ok(Map.of(
                    "message", "Colleges fetched successfully by state ID: " + stateId,
                    "data", colleges
            ));
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(404).body(Map.of(
                    "message", e.getMessage()
            ));
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "An error occurred while fetching colleges by state"
            ));
        }
    }

    // Get College by university
    @GetMapping("/college/{universityId}")
    public ResponseEntity<?> getCollegesByUniversity(@PathVariable Long universityId)
    {
        try
        {
            List<CollegeResponseDTO> colleges =
                    collegeMasterService.getCollegesByUniversity(universityId);
            return ResponseEntity.ok(Map.of(
                    "message", "Colleges fetched successfully!",
                    "data", colleges
            ));
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(404).body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }

}
