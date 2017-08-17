package com.softserve.academy.spaced.repetition.service;


import com.softserve.academy.spaced.repetition.domain.Audit;
import com.softserve.academy.spaced.repetition.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public List<Audit> getFullAuditList() {
        List<Audit> auditList = auditRepository.findAll();
        return auditList;
    }

    public List<Audit> getAuditListSortedByAccountEmailAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByAccountEmail();
        return auditList;
    }

    public List<Audit> getAuditListSortedByAccountEmailDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByAccountEmailDesc();
        return auditList;
    }

    public List<Audit> getAuditListSortedByActionAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByAction();
        return auditList;
    }

    public List<Audit> getAuditListSortedByActionAscDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByActionDesc();
        return auditList;
    }

    public List<Audit> getAuditListSortedByIpAddressAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByIpAddress();
        return auditList;
    }

    public List<Audit> getAuditListSortedByIpAddressDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByIpAddressDesc();
        return auditList;
    }

    public List<Audit> getAuditListSortedByTimeAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByTime();
        return auditList;
    }

    public List<Audit> getAuditListSortedByTimeDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByTimeDesc();
        return auditList;
    }

    public List<Audit> getAuditListSortedByRoleAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByRole();
        return auditList;
    }

    public List<Audit> getAuditListSortedByRoleDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByRoleDesc();
        return auditList;
    }
}
