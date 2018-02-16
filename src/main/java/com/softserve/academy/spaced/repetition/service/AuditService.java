package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Audit;
import org.springframework.data.domain.Page;

/**
 * Works with data of audit.
 */
public interface AuditService {

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
