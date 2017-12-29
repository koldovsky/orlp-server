package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                .build();
    }

    @Test
    public void getAuditByPage() throws Exception {
//        int numberPage =1 ;
//        String sortBy = "id";
//        boolean ascending = true;
//        when(auditService.getAuditByPage(numberPage,sortBy,ascending)).thenReturn(createAudit());
//        mockMvc.perform(get("/api/admin/audit?p=1&sortBy=id&asc=true")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{\"content\":[{\"time\":\"Sun Apr 09 18:49:17 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_TOP_CATEGORIES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:49:17 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_TOP_COURSES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:01 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_CATEGORIES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:02 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_TOP_CATEGORIES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:04 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_TOP_COURSES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:24 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_CATEGORIES\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:25 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_DECKS_ADMIN\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:50:35 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_USERS_ADMIN\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:51:14 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_USERS_ADMIN\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]},{\"time\":\"Sun Apr 09 18:51:18 EET 19\",\"accountEmail\":\"admin@gmail.com\",\"action\":\"VIEW_ALL_USERS_ADMIN\",\"ipAddress\":\"0:0:0:0:0:0:0:1\",\"role\":\"ROLE_ADMIN\",\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/audit?pageNumber=1&sortBy=id&ascending=true\"}]}],\"totalPages\":1,\"totalElements\":10,\"last\":true,\"size\":0,\"number\":0,\"numberOfElements\":10,\"sort\":null,\"first\":true}"));
    }

    private Page<Audit> createAudit() throws ParseException {
        List<Audit> auditList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

        Audit audit = new Audit();
        audit.setAccountEmail("admin@gmail.com");
        audit.setRole("ROLE_ADMIN");
        audit.setIpAddress("0:0:0:0:0:0:0:1");
        audit.setAction(AuditingAction.VIEW_TOP_CATEGORIES);
        String dateStr = "2017-10-13 18:49:17.0";
        Date date = dateFormat.parse(dateStr);
        audit.setTime(date);

        Audit audit2 = new Audit();
        audit2.setAccountEmail("admin@gmail.com");
        audit2.setRole("ROLE_ADMIN");
        audit2.setIpAddress("0:0:0:0:0:0:0:1");
        audit2.setAction(AuditingAction.VIEW_TOP_COURSES);
        String dateStr2 = "2017-10-13 18:49:17.0";
        Date date2 = dateFormat.parse(dateStr2);
        audit2.setTime(date2);

        Audit audit3 = new Audit();
        audit3.setAccountEmail("admin@gmail.com");
        audit3.setRole("ROLE_ADMIN");
        audit3.setIpAddress("0:0:0:0:0:0:0:1");
        audit3.setAction(AuditingAction.VIEW_ALL_CATEGORIES);
        String dateStr3 = "2017-10-13 18:50:01.0";
        Date date3 = dateFormat.parse(dateStr3);
        audit3.setTime(date3);

        Audit audit4 = new Audit();
        audit4.setAccountEmail("admin@gmail.com");
        audit4.setRole("ROLE_ADMIN");
        audit4.setIpAddress("0:0:0:0:0:0:0:1");
        audit4.setAction(AuditingAction.VIEW_TOP_CATEGORIES);
        String dateStr4 = "2017-10-13 18:50:02.0";
        Date date4 = dateFormat.parse(dateStr4);
        audit4.setTime(date4);

        Audit audit5 = new Audit();
        audit5.setAccountEmail("admin@gmail.com");
        audit5.setRole("ROLE_ADMIN");
        audit5.setIpAddress("0:0:0:0:0:0:0:1");
        audit5.setAction(AuditingAction.VIEW_TOP_COURSES);
        String dateStr5 = "2017-10-13 18:50:04.0";
        Date date5 = dateFormat.parse(dateStr5);
        audit5.setTime(date5);

        Audit audit6 = new Audit();
        audit6.setAccountEmail("admin@gmail.com");
        audit6.setRole("ROLE_ADMIN");
        audit6.setIpAddress("0:0:0:0:0:0:0:1");
        audit6.setAction(AuditingAction.VIEW_ALL_CATEGORIES);
        String dateStr6 = "2017-10-13 18:50:24.0";
        Date date6 = dateFormat.parse(dateStr6);
        audit6.setTime(date6);

        Audit audit7 = new Audit();
        audit7.setAccountEmail("admin@gmail.com");
        audit7.setRole("ROLE_ADMIN");
        audit7.setIpAddress("0:0:0:0:0:0:0:1");
        audit7.setAction(AuditingAction.VIEW_DECKS_ADMIN);
        String dateStr7 = "2017-10-13 18:50:25.0";
        Date date7 = dateFormat.parse(dateStr7);
        audit7.setTime(date7);

        Audit audit8 = new Audit();
        audit8.setAccountEmail("admin@gmail.com");
        audit8.setRole("ROLE_ADMIN");
        audit8.setIpAddress("0:0:0:0:0:0:0:1");
        audit8.setAction(AuditingAction.VIEW_ALL_USERS_ADMIN);
        audit8.setTime(date);
        String dateStr8 = "2017-10-13 18:50:35.0";
        Date date8 = dateFormat.parse(dateStr8);
        audit8.setTime(date8);

        Audit audit9 = new Audit();
        audit9.setAccountEmail("admin@gmail.com");
        audit9.setRole("ROLE_ADMIN");
        audit9.setIpAddress("0:0:0:0:0:0:0:1");
        audit9.setAction(AuditingAction.VIEW_ALL_USERS_ADMIN);
        String dateStr9 = "2017-10-13 18:51:14.0";
        Date date9 = dateFormat.parse(dateStr9);
        audit9.setTime(date9);

        Audit audit10 = new Audit();
        audit10.setAccountEmail("admin@gmail.com");
        audit10.setRole("ROLE_ADMIN");
        audit10.setIpAddress("0:0:0:0:0:0:0:1");
        audit10.setAction(AuditingAction.VIEW_ALL_USERS_ADMIN);
        String dateStr10 = "2017-10-13 18:51:18.0";
        Date date10 = dateFormat.parse(dateStr10);
        audit10.setTime(date10);

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
