package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    public Image findImageById(Long id);

    @Query("SELECT hash FROM Image")
    public List<Integer> getHashsList();

    @Query("SELECT id FROM Image")
    public List<Long> getIdList();

    @Query("SELECT new com.softserve.academy.spaced.repetition.domain.Image(i.id) FROM Image i")
    public List<Image> getImagesWithoutBase64();

    @Query("SELECT new com.softserve.academy.spaced.repetition.domain.Image(i.id) FROM Image i WHERE i.id = ?1")
    public Image getImageWithoutBase64(Long id);

    @Query("SELECT id FROM Image i WHERE i.hash = ?1")
    public Long getIdByHash(Integer hash);


}



