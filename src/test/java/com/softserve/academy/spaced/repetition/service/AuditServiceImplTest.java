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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class AuditServiceImplTest {

    @InjectMocks
    private AuditServiceImpl auditService;

    @Mock
    private AuditRepository auditRepository;

    private Audit audit;

    @Before
    public void setUp(){
        final long AUDIT_ID = 1L;
        final String EMAIL = "account@test.com";
        final String IP = "1.1.1.1";
        final String ROLE = "ROLE_USER";
        AuditingAction action;
        action = AuditingAction.VIEW_ALL_COMMENTS_FOR_COURSE;
        audit = DomainFactory.createAudit(AUDIT_ID,EMAIL,action,new Date(),IP,ROLE);
    }
    private List<Audit> createAuditList(){
        List<Audit> auditList = new ArrayList<>();
        auditList.add(audit);
        return auditList;
    }
    @Test
    public void testGetFullAuditList() {
        when(auditRepository.findAll()).thenReturn(createAuditList());

        List<Audit> result = auditService.getFullAuditList();
        verify(auditRepository).findAll();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditListSortedByAccountEmailAsc() {
        when(auditRepository.findAllByOrderByAccountEmail()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByAccountEmailAsc();
        verify(auditRepository).findAllByOrderByAccountEmail();
        assertEquals(result,createAuditList());

    }

    @Test
    public void testGetAuditListSortedByAccountEmailDesc() {
        when(auditRepository.findAllByOrderByAccountEmailDesc()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByAccountEmailDesc();
        verify(auditRepository).findAllByOrderByAccountEmailDesc();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditListSortedByActionAsc() {
        when(auditRepository.findAllByOrderByAction()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByActionAsc();
        verify(auditRepository).findAllByOrderByAction();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditListSortedByActionAscDesc() {
        when(auditRepository.findAllByOrderByActionDesc()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByActionAscDesc();
        verify(auditRepository).findAllByOrderByActionDesc();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditListSortedByIpAddressAsc() {
        when(auditRepository.findAllByOrderByIpAddress()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByIpAddressAsc();
        verify(auditRepository).findAllByOrderByIpAddress();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditListSortedByIpAddressDesc() {
        when(auditRepository.findAllByOrderByIpAddressDesc()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByIpAddressDesc();
        verify(auditRepository).findAllByOrderByIpAddressDesc();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditListSortedByTimeAsc() {
        when(auditRepository.findAllByOrderByTime()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByTimeAsc();
        verify(auditRepository).findAllByOrderByTime();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditListSortedByTimeDesc() {
        when(auditRepository.findAllByOrderByTimeDesc()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByTimeDesc();
        verify(auditRepository).findAllByOrderByTimeDesc();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditListSortedByRoleAsc() {
        when(auditRepository.findAllByOrderByRole()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByRoleAsc();
        verify(auditRepository).findAllByOrderByRole();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditListSortedByRoleDesc() {
        when(auditRepository.findAllByOrderByRoleDesc()).thenReturn(createAuditList());

        List<Audit> result = auditService.getAuditListSortedByRoleDesc();
        verify(auditRepository).findAllByOrderByRoleDesc();
        assertEquals(result,createAuditList());
    }

    @Test
    public void testGetAuditByPage() {

    }
}