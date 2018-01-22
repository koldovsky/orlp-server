package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findImageById(Long imageId);

    @Query("SELECT id FROM Image")
    List<Long> getIdList();

    @Query("SELECT i.id FROM Image i")
    List<Image> getImagesWithoutContent();

    @Query("SELECT i.id, i.isImageUsed FROM Image i JOIN i.createdBy u WHERE u.id = ?1")
    List<Image> getImagesWithoutContentById(Long userId);

    @Query("SELECT i.id, i.createdBy FROM Image i WHERE i.id = ?1")
    Image getImageWithoutContentById(Long imageId);

    @Query("SELECT SUM(i.size) FROM Image i WHERE user_id = ?1")
    Long getSumOfImagesSizesOfUserById(Long userId);

}



