package com.skilluser.user.controller;

import com.skilluser.user.dto.DomainDto;
import com.skilluser.user.model.Domain;
import com.skilluser.user.service.DomainService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users/domain")
public class DomainController
{
    // shrunkhal 9-10-25  add domain
    private final DomainService domainService;
    @PostMapping
    public ResponseEntity<Domain> createDomain(@RequestBody Domain domain)
    {
        Domain savedDomain = domainService.createDomain(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDomain);
    }

    @GetMapping    // shrunkhal 9-10-25 get domain
    public ResponseEntity<?> getDomains()
    {
        return ResponseEntity.status(HttpStatus.OK).body(domainService.getDomains());
    }


    @GetMapping("/{companyId}")
    public  ResponseEntity<?> getDomainsByCompany(@PathVariable Long companyId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(domainService.getDomainByCompany(companyId));
    }

    @GetMapping("/dto")   // Ashvin 5-01-26 get domain in Dto responce
    public ResponseEntity<List<DomainDto>> getDomainsDto() {
        return ResponseEntity.ok(domainService.getDomainsDto());
    }

}
