package com.softserve.academy.spaced.repetition.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingCountService {

    public double countAvarageRating(List<Integer> ratings) {
        double totalRating = 0;
        int numbOfNotZeroRatings = 0;

        for(int rating:ratings){
            totalRating+=rating;
            if(rating>0){
                numbOfNotZeroRatings++;
            }
        }
        totalRating = totalRating / numbOfNotZeroRatings;
        return totalRating;
    }

    public int countZeroRating(List<Integer> ratings){
        int numbOfZeroRating = 0;
        for(int rating:ratings){
            if(rating==0)
                numbOfZeroRating++;
        }
        return numbOfZeroRating;
    }
}
