package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.AuditPublicDTO;
import com.softserve.academy.spaced.repetition.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("api/admin/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    /**
     * Get list of audit with self-link
     *
     * @return - list of audit
     */
    @PreAuthorize("hasPermission('AUDIT','READ')")
    @GetMapping
    public ResponseEntity<Page<AuditPublicDTO>> getFullAuditList(@RequestParam(name = "p", defaultValue = "1")
                                                                         int pageNumber,
                                                                 @RequestParam(name = "sortBy") String sortBy,
                                                                 @RequestParam(name = "asc") boolean ascending) {
        Page<AuditPublicDTO> auditPublicDTOS = auditService.getAuditByPage(pageNumber, sortBy, ascending).map(audit -> {
            Link selfLink = linkTo(methodOn(AuditController.class)
                    .getFullAuditList(pageNumber, sortBy, ascending)).withSelfRel();
            return DTOBuilder.buildDtoForEntity(audit, AuditPublicDTO.class, selfLink);
        });

        return new ResponseEntity<>(auditPublicDTOS, HttpStatus.OK);
    }
}
