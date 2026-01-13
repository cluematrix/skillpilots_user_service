package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.CollegeList;
import com.skilluser.user.dto.CollegeRequestDTO;
import com.skilluser.user.dto.CollegeResponseDTO;
import com.skilluser.user.model.*;
import com.skilluser.user.repository.*;
import com.skilluser.user.service.CollegeMasterService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CollegeMasterServiceImpl implements CollegeMasterService
{

    private final CollegeMasterRepository collegeRepo;
    private final StateMasterRepository stateRepo;
    private final UniversityMasterRepository universityRepo;

    @Override
    public CollegeResponseDTO saveCollege(CollegeRequestDTO dto)
    {
        StateMaster state = stateRepo.findById(dto.getStateId())
                .orElseThrow(() -> new RuntimeException("Invalid State ID"));
        UniversityMaster university = universityRepo.findById(dto.getUniversityId())
                .orElseThrow(() -> new RuntimeException("Invalid University ID"));

        CollegeMaster college = new CollegeMaster();
        college.setCollegeName(dto.getCollegeName());
        college.setCity(dto.getCity());
        college.setDistrict(dto.getDistrict());
        college.setType(dto.getType());
        college.setState(state);
        college.setUniversity(university);
        college.setStatus("Active");

        CollegeMaster saved = collegeRepo.save(college);

        return new CollegeResponseDTO(
                saved.getCollegeId(),
                saved.getCollegeName(),
                saved.getCity(),
                saved.getDistrict(),
                saved.getType(),
                saved.getStatus(),
                state.getStateId(),
                state.getStateName(),
                university.getUniversityId(),
                university.getUniversityName()
        );
    }

    @Override
    public CollegeResponseDTO updateCollege(CollegeRequestDTO dto)
    {
        CollegeMaster college = collegeRepo.findById(dto.getCollegeId())
                .orElseThrow(() -> new RuntimeException("College not found with ID: " + dto.getCollegeId()));

        if (dto.getCollegeName() != null)
        {
            college.setCollegeName(dto.getCollegeName());
        }
        if (dto.getCity() != null)
        {
            college.setCity(dto.getCity());
        }
        if (dto.getDistrict() != null)
        {
            college.setDistrict(dto.getDistrict());
        }
        if (dto.getType() != null)
        {
            college.setType(dto.getType());
        }
        if (dto.getStateId() != null)
        {
            StateMaster state = stateRepo.findById(dto.getStateId())
                    .orElseThrow(() -> new RuntimeException("Invalid State ID"));
            college.setState(state);
        }

        if (dto.getUniversityId() != null)
        {
            UniversityMaster university = universityRepo.findById(dto.getUniversityId())
                    .orElseThrow(() -> new RuntimeException("Invalid University ID"));
            college.setUniversity(university);
        }

        CollegeMaster updated = collegeRepo.save(college);

        return new CollegeResponseDTO(
                updated.getCollegeId(),
                updated.getCollegeName(),
                updated.getCity(),
                updated.getDistrict(),
                updated.getType(),
                updated.getStatus(),
                updated.getState().getStateId(),
                updated.getState().getStateName(),
                updated.getUniversity().getUniversityId(),
                updated.getUniversity().getUniversityName()
        );
    }

    @Override
    public List<CollegeResponseDTO> getAllColleges()
    {
        return collegeRepo.findAll().stream().map(college -> new CollegeResponseDTO(
                college.getCollegeId(),
                college.getCollegeName(),
                college.getCity(),
                college.getDistrict(),
                college.getType(),
                college.getStatus(),
                college.getState() != null ? college.getState().getStateId() : null,
                college.getState() != null ? college.getState().getStateName() : null,
                college.getUniversity() != null ? college.getUniversity().getUniversityId() : null,
                college.getUniversity() != null ? college.getUniversity().getUniversityName() : null
        )).toList();
    }

    @Override
    public List<CollegeResponseDTO> getCollegesByState(Long stateId)
    {
        List<CollegeMaster> colleges = collegeRepo.findByState_StateId(stateId);

        if (colleges.isEmpty())
        {
            throw new RuntimeException("No colleges found for state ID: " + stateId);
        }

        return colleges.stream().map(college -> new CollegeResponseDTO(
                college.getCollegeId(),
                college.getCollegeName(),
                college.getCity(),
                college.getDistrict(),
                college.getType(),
                college.getStatus(),
                college.getState() != null ? college.getState().getStateId() : null,
                college.getState() != null ? college.getState().getStateName() : null,
                college.getUniversity() != null ? college.getUniversity().getUniversityId() : null,
                college.getUniversity() != null ? college.getUniversity().getUniversityName() : null
        )).toList();
    }

    @Override
    public List<CollegeResponseDTO> getCollegesByUniversity(Long universityId)
    {
        List<CollegeMaster> colleges = collegeRepo.findByUniversity_UniversityId(universityId);

        if (colleges.isEmpty())
        {
            throw new RuntimeException("No colleges found for university ID: " + universityId);
        }

        return colleges.stream().map(college -> new CollegeResponseDTO(
                college.getCollegeId(),
                college.getCollegeName(),
                college.getCity(),
                college.getDistrict(),
                college.getType(),
                college.getStatus(),
                college.getState().getStateId(),
                college.getState().getStateName(),
                college.getUniversity().getUniversityId(),
                college.getUniversity().getUniversityName()
        )).toList();
    }

    @Override
    @Transactional
    public Map<String, Object> uploadMasterData(MultipartFile file)
    {
        Map<String, Object> response = new HashMap<>();

        try (InputStream input = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(input))
        {

            DataFormatter formatter = new DataFormatter();

            Sheet stateSheet = workbook.getSheet("States");
            if (stateSheet != null)
            {
                for (int i = 1; i <= stateSheet.getLastRowNum(); i++)
                {
                    Row row = stateSheet.getRow(i);
                    if (row == null || row.getCell(0) == null) continue;

                    String stateName = formatter.formatCellValue(row.getCell(0)).trim();
                    String country = formatter.formatCellValue(row.getCell(1)).trim();

                    if (stateRepo.findByStateNameIgnoreCase(stateName).isEmpty())
                    {
                        StateMaster state = new StateMaster();
                        state.setStateName(stateName);
                        state.setCountry(country.isEmpty() ? "India" : country);
                        stateRepo.save(state);
                    }
                }
            }

            Sheet uniSheet = workbook.getSheet("Universities");
            if (uniSheet != null)
            {
                for (int i = 1; i <= uniSheet.getLastRowNum(); i++)
                {
                    Row row = uniSheet.getRow(i);
                    if (row == null || row.getCell(0) == null) continue;

                    String uniName = formatter.formatCellValue(row.getCell(0)).trim();
                    String address = formatter.formatCellValue(row.getCell(1)).trim();

                    if (universityRepo.findByUniversityNameIgnoreCase(uniName).isEmpty()) {
                        UniversityMaster uni = new UniversityMaster();
                        uni.setUniversityName(uniName);
                        uni.setAddress(address);
                        universityRepo.save(uni);
                    }
                }
            }

            Sheet collegeSheet = workbook.getSheet("Colleges");
            if (collegeSheet != null)
            {
                for (int i = 1; i <= collegeSheet.getLastRowNum(); i++)
                {
                    Row row = collegeSheet.getRow(i);
                    if (row == null || row.getCell(0) == null) continue;

                    String collegeName = formatter.formatCellValue(row.getCell(0)).trim();
                    String city = formatter.formatCellValue(row.getCell(1)).trim();
                    String district = formatter.formatCellValue(row.getCell(2)).trim();
                    String type = formatter.formatCellValue(row.getCell(3)).trim();
                    String stateName = formatter.formatCellValue(row.getCell(4)).trim();
                    String universityName = formatter.formatCellValue(row.getCell(5)).trim();

                    StateMaster state = stateRepo.findByStateNameIgnoreCase(stateName)
                            .orElseThrow(() -> new RuntimeException("Invalid State: " + stateName));

                    UniversityMaster university = universityRepo.findByUniversityNameIgnoreCase(universityName)
                            .orElseThrow(() -> new RuntimeException("Invalid University: " + universityName));

                    if (collegeRepo.findAll().stream()
                            .noneMatch(c -> c.getCollegeName().equalsIgnoreCase(collegeName))) {

                        CollegeMaster college = new CollegeMaster();
                        college.setCollegeName(collegeName);
                        college.setCity(city);
                        college.setDistrict(district);
                        college.setType(type);
                        college.setState(state);
                        college.setUniversity(university);
                        college.setStatus("Active");
                        collegeRepo.save(college);
                    }
                }
            }

            response.put("message", "Excel uploaded successfully!");
            response.put("status", "OK");
            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.put("message", "Error processing Excel: " + e.getMessage());
            response.put("status", "ERROR");
            return response;
        }
    }
    private String getCellValueAsString(Cell cell)
    {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }


    @Override
    public List<CollegeList> getCollegeListByUniversity(Long universityId)
    {
        List<CollegeMaster> colleges =
                collegeRepo
                        .findByUniversity_UniversityId(universityId);

        return colleges.stream()
                .map(college -> new CollegeList(
                        college.getCollegeId(),
                        college.getCollegeName(),
                        college.getType(),
                        college.getIsRegistered()
                ))
                .toList();
    }

}
