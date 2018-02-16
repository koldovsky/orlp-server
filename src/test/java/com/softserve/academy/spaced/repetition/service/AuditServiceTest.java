package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Audit;
import com.softserve.academy.spaced.repetition.repository.AuditRepository;
import com.softserve.academy.spaced.repetition.service.impl.AuditServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class AuditServiceTest {

    @InjectMocks
    private AuditServiceImpl auditService;
    @Mock
    private AuditRepository auditRepository;
    private Audit audit;

    @Before
    public void setUp() {
        final long AUDIT_ID = 1L;
        final String EMAIL = "account@test.com";
        final String IP = "1.1.1.1";
        final String ROLE = "ROLE_USER";
        AuditingAction action;
        action = AuditingAction.VIEW_ALL_COMMENTS_FOR_COURSE;
        audit = DomainFactory.createAudit(AUDIT_ID, EMAIL, action, new Date(), IP, ROLE);
    }

    private List<Audit> createAuditList() {
        List<Audit> auditList = new ArrayList<>();
        auditList.add(audit);
        return auditList;
    }

    @Test
    public void testGetAuditByPage() {
        final int PAGE_NUMBER = 1;
        final boolean PAGE_ASCENDING_ORDER = true;
        final String PAGE_SORT_BY = "field";

        when(auditRepository.findAll(any(PageRequest.class))).thenReturn(null);

        Page<Audit> result = auditService.getAuditByPage(PAGE_NUMBER, PAGE_SORT_BY, PAGE_ASCENDING_ORDER);
        verify(auditRepository).findAll(any(PageRequest.class));
        assertNull(result);
    }
}
