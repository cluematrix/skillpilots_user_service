package com.skilluser.user.service;

import com.skilluser.user.model.Domain;

import java.util.List;

public interface DomainService {

    Domain createDomain(Domain domain);
    List<Domain> getDomains();
}
