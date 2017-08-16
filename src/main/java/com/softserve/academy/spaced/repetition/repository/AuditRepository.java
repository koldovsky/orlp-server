package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, Long> {
    public List<Audit> findAll();

    public List<Audit> findAllByOrderByAccountEmail();

    public List<Audit> findAllByOrderByAccountEmailDesc();

    public List<Audit> findAllByOrderByAction();

    public List<Audit> findAllByOrderByActionDesc();

    public List<Audit> findAllByOrderByIpAddress();

    public List<Audit> findAllByOrderByIpAddressDesc();

    public List<Audit> findAllByOrderByTime();

    public List<Audit> findAllByOrderByTimeDesc();

    public List<Audit> findAllByOrderByRole();

    public List<Audit> findAllByOrderByRoleDesc();
}
