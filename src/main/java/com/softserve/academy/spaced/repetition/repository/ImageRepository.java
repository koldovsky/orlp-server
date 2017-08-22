package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    public Image findImageById(Long id);

    @Query("SELECT id FROM Image")
    public List<Long> getIdList();

    @Query("SELECT new com.softserve.academy.spaced.repetition.domain.Image(i.id) FROM Image i")
    public List<Image> getImagesWithoutContent();

    @Query("SELECT new com.softserve.academy.spaced.repetition.domain.Image(i.id) FROM Image i JOIN i.createdBy u WHERE u.id = ?1")
    public List<Image> getImagesWithoutContentbyId(Long id);

    @Query("SELECT new com.softserve.academy.spaced.repetition.domain.Image(i.id, i.createdBy) FROM Image i WHERE i.id = ?1")
    public Image getImageWithoutBase64(Long id);

    @Query("SELECT SUM(size) FROM Image WHERE user_id = ?1")
    public Long getSumOfImagesSizesOfUserById(Long id);

}



