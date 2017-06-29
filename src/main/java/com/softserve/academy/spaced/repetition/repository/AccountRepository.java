package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jarki on 6/28/2017.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
}
