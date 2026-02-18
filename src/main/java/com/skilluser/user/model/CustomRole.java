package com.skilluser.user.model;

import com.skilluser.user.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Entity
@Data
public class CustomRole
{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private ServiceType serviceType;
        private Long collegeId;  // mandatory if COLLEGE_SERVICE
        private Long companyId;  // mandatory if COMPANY_SERVICE
        private String createdAt;

        @PrePersist
        public void createdAt(){
            this.createdAt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
}
