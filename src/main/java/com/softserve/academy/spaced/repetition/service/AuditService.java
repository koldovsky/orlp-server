package com.softserve.academy.spaced.repetition.service;


import com.softserve.academy.spaced.repetition.domain.Audit;
import com.softserve.academy.spaced.repetition.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public final static int QUANTITY_AUDIT_IN_PAGE = 10;

    public List<Audit> getFullAuditList() {
        return auditRepository.findAll();
    }

    public List<Audit> getAuditListSortedByAccountEmailAsc() {
        return auditRepository.findAllByOrderByAccountEmail();
    }

    public List<Audit> getAuditListSortedByAccountEmailDesc() {
        return auditRepository.findAllByOrderByAccountEmailDesc();
    }

    public List<Audit> getAuditListSortedByActionAsc() {
        return auditRepository.findAllByOrderByAction();
    }

    public List<Audit> getAuditListSortedByActionAscDesc() {
        return auditRepository.findAllByOrderByActionDesc();
    }

    public List<Audit> getAuditListSortedByIpAddressAsc() {
        return auditRepository.findAllByOrderByIpAddress();
    }

    public List<Audit> getAuditListSortedByIpAddressDesc() {
        return auditRepository.findAllByOrderByIpAddressDesc();
    }

    public List<Audit> getAuditListSortedByTimeAsc() {
        return auditRepository.findAllByOrderByTime();
    }

    public List<Audit> getAuditListSortedByTimeDesc() {
        return auditRepository.findAllByOrderByTimeDesc();
    }

    public List<Audit> getAuditListSortedByRoleAsc() {
        return auditRepository.findAllByOrderByRole();
    }

    public List<Audit> getAuditListSortedByRoleDesc() {
        return auditRepository.findAllByOrderByRoleDesc();
    }

    public Page<Audit> getAuditByPage(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1,
                QUANTITY_AUDIT_IN_PAGE, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return auditRepository.findAll(request);
    }
}
