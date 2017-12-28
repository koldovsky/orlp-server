package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Authority;
import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{
    Authority findAuthorityByName(AuthorityName authorityName);
}
