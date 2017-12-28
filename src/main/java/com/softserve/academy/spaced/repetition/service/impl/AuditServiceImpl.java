package com.softserve.academy.spaced.repetition.service.impl;


import com.softserve.academy.spaced.repetition.domain.Audit;
import com.softserve.academy.spaced.repetition.repository.AuditRepository;
import com.softserve.academy.spaced.repetition.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditRepository auditRepository;
    public final static int QUANTITY_AUDIT_IN_PAGE = 10;

    @Override
    public List<Audit> getFullAuditList() {
        List<Audit> auditList = auditRepository.findAll();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByAccountEmailAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByAccountEmail();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByAccountEmailDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByAccountEmailDesc();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByActionAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByAction();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByActionAscDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByActionDesc();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByIpAddressAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByIpAddress();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByIpAddressDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByIpAddressDesc();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByTimeAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByTime();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByTimeDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByTimeDesc();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByRoleAsc() {
        List<Audit> auditList = auditRepository.findAllByOrderByRole();
        return auditList;
    }

    @Override
    public List<Audit> getAuditListSortedByRoleDesc() {
        List<Audit> auditList = auditRepository.findAllByOrderByRoleDesc();
        return auditList;
    }

    @Override
    public Page<Audit> getAuditByPage(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_AUDIT_IN_PAGE,
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return auditRepository.findAll(request);
    }
}
