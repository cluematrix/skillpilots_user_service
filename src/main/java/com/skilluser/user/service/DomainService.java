package com.skilluser.user.service;

import com.skilluser.user.dto.DomainDto;
import com.skilluser.user.model.Domain;

import java.util.List;

public interface DomainService {

    public Domain createDomain(Domain domain);
    public List<Domain> getDomains();

    List<DomainDto> getDomainsDto();


}
