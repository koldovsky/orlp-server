package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query(value = "select c.card_id, c.title, c.question, c.answer, c.rating " +
            "from card c " +
            "where c.deck_id = :deckId and (c.card_id, c.title, c.question, c.answer, c.rating) not in(" +
            "select c.card_id, c.title, c.question, c.answer, c.rating " +
            "from card c left join user_card_queue u " +
            "on c.card_id=u.card_id " +
            "where u.user_id = :userId) limit :limitNumber", nativeQuery = true)
    List<Card> cardsForLearningWithOutStatus(@Param("userId") Long userId, @Param("deckId") Long deckId,
                                             @Param("limitNumber") int limitNumber);

    @Query(value =
            "select c.card_id, c.title, c.question, c.answer, c.rating " +
            "from card c inner join user_card_queue u on c.card_id=u.card_id " +
            "where u.deck_id = :deckId and u.user_id = :userId " +
            "order by case u.status " +
            "when 'BAD' then 1 " +
            "when 'NORMAL' then 2 " +
            "when 'GOOD' then 3 " +
            "end, u.card_data limit :limitNumber", nativeQuery = true)
    List<Card> cardsQueueForLearningWithStatus(@Param("userId") Long userId, @Param("deckId") Long deckId,
                                               @Param("limitNumber") int limitNumber);

    List <Card> findAllByQuestion(String question);

    @Query(value = "SELECT * FROM card WHERE deck_id = :deckId AND card_id NOT IN " +
            "(SELECT card_id FROM user_card_queue WHERE deck_id=:deckId AND user_id = :userId) limit :limit",
            nativeQuery = true)
    List<Card> getNewCards(@Param("deckId") Long deckId, @Param("userId") Long userId, @Param("limit") int limit);

    @Query(value = "SELECT c.* FROM card c INNER JOIN user_card_queue u on c.card_id = u.card_id WHERE c.deck_id = " +
            ":deckId AND u.user_id = :userId AND date_to_repeat <= :now limit :limit",
            nativeQuery = true)
    List<Card> getCardsThatNeedRepeating(@Param("deckId") Long deckId, @Param("now") Date now,
                                         @Param("userId") Long userId, @Param("limit") int limit);

    List<Card> findAllByDeckId(Long deckId);

    @Query(value = "SELECT c.* FROM card c INNER JOIN user_card_queue u on c.card_id = u.card_id WHERE c.deck_id = " +
            ":deckId AND u.user_id = :userId AND date_to_repeat > :now limit :limit",
            nativeQuery = true)
    List<Card> getPostponedCards(@Param("deckId") Long deckId, @Param("now") Date now,
                                 @Param("userId") Long userId, @Param("limit") int limit);
}
