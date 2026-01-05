package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.DomainDto;
import com.skilluser.user.model.Domain;
import com.skilluser.user.repository.DomainRepository;
import com.skilluser.user.service.DomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class DomainServiceImpl implements DomainService {

    private final DomainRepository domainRepository;

    @Override
    public Domain createDomain(Domain domain) {
        return domainRepository.save(domain);
    }

    @Override
    public List<Domain> getDomains() {
        List<Domain> all = domainRepository.findAll();
        Collections.reverse(all);
        return all;
    }



    @Override
    public List<DomainDto> getDomainsDto() {

        List<Domain> domains = domainRepository.findAll();

        // reverse order (latest first)
        Collections.reverse(domains);

        return domains.stream()
                .map(domain -> {
                    DomainDto dto = new DomainDto();
                    dto.setMainDomainId(domain.getId());
                    dto.setName(domain.getName());
                    dto.setCreatedAt(domain.getCreatedAt());
                    return dto;
                })
                .toList();
    }



}
