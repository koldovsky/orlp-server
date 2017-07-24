package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    public Image findImageById(Long id);

    public Image findImageByName(String name);

    @Query("SELECT hash FROM Image")
    public List<Integer> getHashList();

    @Query("SELECT name FROM Image")
    public List<String> getNameList();

}



