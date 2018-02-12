package com.softserve.academy.spaced.repetition.service.impl;


import com.softserve.academy.spaced.repetition.domain.Audit;
import com.softserve.academy.spaced.repetition.repository.AuditRepository;
import com.softserve.academy.spaced.repetition.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements AuditService {

    private final static int QUANTITY_AUDIT_IN_PAGE = 10;
    @Autowired
    private AuditRepository auditRepository;

    @Override
    public Page<Audit> getAuditByPage(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_AUDIT_IN_PAGE,
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return auditRepository.findAll(request);
    }
}
