package com.softserve.academy.spaced.repetition.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Audit;
import com.softserve.academy.spaced.repetition.service.AuditService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class AuditControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private AuditController auditController;

    @Mock
    private AuditService auditService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auditController)
                .setControllerAdvice(new ExceptionHandlerController())
                .alwaysDo(print())
                .build();
    }

    @Test
    public void getAuditByPage() throws Exception {
        int numberPage =1 ;
        String sortBy = "id";
        boolean ascending = true;
        when(auditService.getAuditByPage(numberPage,sortBy,ascending)).thenReturn(createAudit());
        mockMvc.perform(get("/api/admin/audit?p=1&sortBy=id&asc=true")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"time\":\"Sun Apr 09 18:49:17 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_TOP_CATEGORIES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:49:17 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_TOP_COURSES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:01 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_CATEGORIES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:02 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_TOP_CATEGORIES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:04 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_TOP_COURSES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:24 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_CATEGORIES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:25 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_DECKS_ADMIN\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:35 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_USERS_ADMIN\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:51:14 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_USERS_ADMIN\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:51:18 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_USERS_ADMIN\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]}],\"totalPages\":1,\"totalElements\":10,\"last\":true,\"size\":0,\"number\":0,\"numberOfElements\":10,\"sort\":null,\"first\":true}"));
    }

    private Audit createObjectAudit(String accountEmail, String role, String ipAddress, AuditingAction auditingAction,
                                    String dateAndTime) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

        Audit audit = new Audit();
        audit.setAccountEmail(accountEmail);
        audit.setRole(role);
        audit.setIpAddress(ipAddress);
        audit.setAction(auditingAction);
        Date date = dateFormat.parse(dateAndTime);
        audit.setTime(date);
        return audit;
    }

    private Page<Audit> createAudit() throws ParseException {
        List<Audit> auditList = new ArrayList<>();
        Audit audit = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_TOP_CATEGORIES,"2017-10-13 18:49:17.0");

        Audit audit2 = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_TOP_COURSES,
                "2017-10-13 18:49:17.0");

        Audit audit3 = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_ALL_CATEGORIES,
                "2017-10-13 18:50:01.0");

        Audit audit4 = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_TOP_CATEGORIES,
                "2017-10-13 18:50:02.0");

        Audit audit5 = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_TOP_COURSES,
                "2017-10-13 18:50:04.0");

        Audit audit6 = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_ALL_CATEGORIES,
                "2017-10-13 18:50:24.0");

        Audit audit7 = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_DECKS_ADMIN,
                "2017-10-13 18:50:25.0");

        Audit audit8 = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_ALL_USERS_ADMIN,
                "2017-10-13 18:50:35.0");

        Audit audit9 = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_ALL_USERS_ADMIN,
                "2017-10-13 18:51:14.0");

        Audit audit10 = createObjectAudit("admin@gmail.com","ROLE_ADMIN",
                "0:0:0:0:0:0:0:1", AuditingAction.VIEW_ALL_USERS_ADMIN,
                "2017-10-13 18:51:18.0");

        auditList.add(audit);
        auditList.add(audit2);
        auditList.add(audit3);
        auditList.add(audit4);
        auditList.add(audit5);
        auditList.add(audit6);
        auditList.add(audit7);
        auditList.add(audit8);
        auditList.add(audit9);
        auditList.add(audit10);

        Page<Audit> auditPage = new PageImpl<>(auditList);
        return auditPage;
    }
}
