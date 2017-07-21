package com.softserve.academy.spaced.repetition.repository;

import com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManager;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardRatingRepository extends JpaRepository<CardRating, Long> {

//    final static String updateCardRating =
//            "UPDATE  card c" +
//                    "        CROSS JOIN" +
//                    "        (" +
//                    "            SELECT  AVG(rating) c_r " +
//                    "            FROM    card_rating " +
//                    "            WHERE   deck_id = ?1 " +
//                    "            AND card_id=?2 " +
//                    "        ) b " +
//                    "SET     c.rating = b.c_r " +
//                    "WHERE   c.card_id = ?3 ";
//    @Modifying()
//    @Query(value = updateCardRating,nativeQuery = true)
//    public void updateCardRating(int deck_id,int card_id,long card_idd);

    public List<CardRating> findAllByCardId(long cardId);

    public List<CardRating> findAllByDeckId(long deckId);

    public CardRating findAllByAccountEmail(String accountEmail);
}
