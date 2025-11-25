package com.skilluser.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skilluser.user.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String createdAt;
    private String path;

    private ServiceType serviceType;
    private boolean isActive;
    @JsonProperty("isSidebar")
    private boolean isSidebar= false;

    @PrePersist
    public void createdAt(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdAt  = LocalDate.now().format(formatter);
    }
}
