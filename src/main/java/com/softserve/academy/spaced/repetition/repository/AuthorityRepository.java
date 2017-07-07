package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jarki on 7/6/2017.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long>{
}
