package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

}
