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
    private List<Audit> audits;

    @Before
    public void setUp() {
        final long AUDIT_ID = 1L;
        final String EMAIL = "account@test.com";
        final String IP = "1.1.1.1";
        final String ROLE = "ROLE_USER";
        audits = createAuditList();
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
    public void testGetFullAuditList() {
        when(auditRepository.findAll()).thenReturn(audits);

        List<Audit> result = auditService.getFullAuditList();
        verify(auditRepository).findAll();
        assertEquals(audits, result);
    }

    @Test
    public void testGetAuditListSortedByAccountEmailAsc() {
        when(auditRepository.findAllByOrderByAccountEmail()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByAccountEmailAsc();
        verify(auditRepository).findAllByOrderByAccountEmail();
        assertEquals(audits, result);

    }

    @Test
    public void testGetAuditListSortedByAccountEmailDesc() {
        when(auditRepository.findAllByOrderByAccountEmailDesc()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByAccountEmailDesc();
        verify(auditRepository).findAllByOrderByAccountEmailDesc();
        assertEquals(audits, result);
    }

    @Test
    public void testGetAuditListSortedByActionAsc() {
        when(auditRepository.findAllByOrderByAction()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByActionAsc();
        verify(auditRepository).findAllByOrderByAction();
        assertEquals(audits, result);
    }

    @Test
    public void testGetAuditListSortedByActionAscDesc() {
        when(auditRepository.findAllByOrderByActionDesc()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByActionAscDesc();
        verify(auditRepository).findAllByOrderByActionDesc();
        assertEquals(audits, result);
    }

    @Test
    public void testGetAuditListSortedByIpAddressAsc() {
        when(auditRepository.findAllByOrderByIpAddress()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByIpAddressAsc();
        verify(auditRepository).findAllByOrderByIpAddress();
        assertEquals(audits, result);
    }

    @Test
    public void testGetAuditListSortedByIpAddressDesc() {
        when(auditRepository.findAllByOrderByIpAddressDesc()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByIpAddressDesc();
        verify(auditRepository).findAllByOrderByIpAddressDesc();
        assertEquals(audits, result);
    }

    @Test
    public void testGetAuditListSortedByTimeAsc() {
        when(auditRepository.findAllByOrderByTime()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByTimeAsc();
        verify(auditRepository).findAllByOrderByTime();
        assertEquals(audits, result);
    }

    @Test
    public void testGetAuditListSortedByTimeDesc() {
        when(auditRepository.findAllByOrderByTimeDesc()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByTimeDesc();
        verify(auditRepository).findAllByOrderByTimeDesc();
        assertEquals(audits, result);
    }

    @Test
    public void testGetAuditListSortedByRoleAsc() {
        when(auditRepository.findAllByOrderByRole()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByRoleAsc();
        verify(auditRepository).findAllByOrderByRole();
        assertEquals(audits, result);
    }

    @Test
    public void testGetAuditListSortedByRoleDesc() {
        when(auditRepository.findAllByOrderByRoleDesc()).thenReturn(audits);

        List<Audit> result = auditService.getAuditListSortedByRoleDesc();
        verify(auditRepository).findAllByOrderByRoleDesc();
        assertEquals(audits, result);
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
