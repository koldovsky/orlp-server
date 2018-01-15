package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.CardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardImageRepository extends JpaRepository<CardImage,Long>{
}
