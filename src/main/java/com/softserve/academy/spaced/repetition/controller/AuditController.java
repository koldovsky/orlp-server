package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.AuditPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Audit;
import com.softserve.academy.spaced.repetition.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class AuditController {

    @Autowired
    private AuditService auditService;

    /**
     * Get list of audit with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit")
    public ResponseEntity<Page<AuditPublicDTO>> getFullAuditList(@RequestParam(name = "p", defaultValue = "1")
                                                                         int pageNumber,
                                                                 @RequestParam(name = "sortBy") String sortBy,
                                                                 @RequestParam(name = "asc") boolean ascending) {
        Page<AuditPublicDTO> auditPublicDTOS = auditService.getAuditByPage(pageNumber, sortBy, ascending).map((audit) -> {
            Link selfLink = linkTo(methodOn(AuditController.class)
                    .getFullAuditList(pageNumber, sortBy, ascending)).withSelfRel();
            return DTOBuilder.buildDtoForEntity(audit, AuditPublicDTO.class, selfLink);
        });
        return new ResponseEntity<>(auditPublicDTOS, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by account email on growth with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/email/rise")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByAccountEmailAsc() {
        List<Audit> auditList = auditService.getAuditListSortedByAccountEmailAsc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByAccountEmailAsc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by account email in descending order with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/email/fall")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByAccountEmailDesc() {
        List<Audit> auditList = auditService.getAuditListSortedByAccountEmailDesc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByAccountEmailDesc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by action on growth with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/action/rise")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByActionAsc() {
        List<Audit> auditList = auditService.getAuditListSortedByActionAsc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByActionAsc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by action in descending order with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/action/fall")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByActionAscDesc() {
        List<Audit> auditList = auditService.getAuditListSortedByActionAscDesc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByActionAscDesc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by ip-address on growth with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/ip/rise")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByIpAddressAsc() {
        List<Audit> auditList = auditService.getAuditListSortedByIpAddressAsc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByIpAddressAsc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by ip-address in descending order with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/ip/fall")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByIpAddressDesc() {
        List<Audit> auditList = auditService.getAuditListSortedByIpAddressDesc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByIpAddressDesc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by time on growth with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/time/rise")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByTimeAsc() {
        List<Audit> auditList = auditService.getAuditListSortedByTimeAsc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByTimeAsc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by time in descending order with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/time/fall")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByTimeDesc() {
        List<Audit> auditList = auditService.getAuditListSortedByTimeDesc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByTimeDesc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by role on growth with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/role/rise")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByRoleAsc() {
        List<Audit> auditList = auditService.getAuditListSortedByRoleAsc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByRoleAsc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }

    /**
     * Get list of audit sorted by role in descending order with self-link
     *
     * @return - list of audit
     */
    @GetMapping("api/admin/audit/sort/role/fall")
    public ResponseEntity<List<AuditPublicDTO>> getAuditListSortedByRoleDesc() {
        List<Audit> auditList = auditService.getAuditListSortedByRoleDesc();
        Link link = linkTo(methodOn(AuditController.class).getAuditListSortedByRoleDesc()).withSelfRel();
        List<AuditPublicDTO> auditDTOList = DTOBuilder.buildDtoListForCollection(auditList, AuditPublicDTO.class, link);
        return new ResponseEntity<>(auditDTOList, HttpStatus.OK);
    }
}