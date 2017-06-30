package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by askol on 6/30/2017.
 */
@Repository
public interface FolderRepository extends JpaRepository<Account, Long> {
}
