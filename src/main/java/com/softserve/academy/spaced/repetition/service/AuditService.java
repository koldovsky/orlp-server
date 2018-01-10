package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Audit;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Works with data of audit.
 */
public interface AuditService {
    /**
     * Return all audits.
     *
     * @return list of audits
     */
    List<Audit> getFullAuditList();

    /**
     * Return all audits ordered by email in ASC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByAccountEmailAsc();

    /**
     * Return all audits ordered by email in DESC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByAccountEmailDesc();

    /**
     * Return all audits ordered by action in ASC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByActionAsc();

    /**
     * Return all audits ordered by action in DESC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByActionAscDesc();

    /**
     * Return all audits ordered by IP address in ASC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByIpAddressAsc();

    /**
     * Return all audits ordered by IP address in DESC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByIpAddressDesc();

    /**
     * Return all audits ordered by time in ASC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByTimeAsc();

    /**
     * Return all audits ordered by time in DESC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByTimeDesc();

    /**
     * Return all audits ordered by role in ASC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByRoleAsc();

    /**
     * Return all audits ordered by role in DESC direction.
     *
     * @return sorted list of audits
     */
    List<Audit> getAuditListSortedByRoleDesc();

    /**
     * Return sorted audits on each page.
     *
     * @param pageNumber zero-based page index.
     * @param sortBy     the properties to sort by, must not be null or empty.
     * @param ascending  the direction of sorting, if true sort by ASC otherwise DESC
     * @return sorted course on each page (by default 10 audits on each page)
     */
    Page<Audit> getAuditByPage(int pageNumber, String sortBy, boolean ascending);

}
