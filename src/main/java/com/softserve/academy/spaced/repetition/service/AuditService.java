package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Audit;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuditService {
    List<Audit> getFullAuditList();

    List<Audit> getAuditListSortedByAccountEmailAsc();

    List<Audit> getAuditListSortedByAccountEmailDesc();

    List<Audit> getAuditListSortedByActionAsc();

    List<Audit> getAuditListSortedByActionAscDesc();

    List<Audit> getAuditListSortedByIpAddressAsc();

    List<Audit> getAuditListSortedByIpAddressDesc();

    List<Audit> getAuditListSortedByTimeAsc();

    List<Audit> getAuditListSortedByTimeDesc();

    List<Audit> getAuditListSortedByRoleAsc();

    List<Audit> getAuditListSortedByRoleDesc();

    Page<Audit> getAuditByPage(int pageNumber, String sortBy, boolean ascending);

}
