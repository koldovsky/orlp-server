package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query(value = "SELECT c FROM Deck d INNER JOIN d.cards AS c WHERE d.id = :deck_id and c.id = :card_id")
    List<Card> hasAccessToCard(@Param("deck_id") Long deck_id, @Param("card_id") Long card_id);

    @Query(value =
            "select c.card_id, c.title, c.question, c.answer, c.rating " +
            "from card c inner join deck_cards d on c.card_id=d.card_id " +
            "where deck_id = :deckId and (c.card_id, c.title, c.question, c.answer, c.rating) not in(" +
            "select c.card_id, c.title, c.question, c.answer, c.rating " +
            "from card c left join user_card_queue u " +
            "on c.card_id=u.card_id " +
            "where u.account_email = :accountEmail) limit :limitNumber", nativeQuery = true)
    List<Card> cardsForLearningWithOutStatus(@Param("accountEmail") String accountEmail, @Param("deckId") long deckId,
                                             @Param("limitNumber") long limitNumber);

    @Query(value =
            "select c.card_id, c.title, c.question, c.answer, c.rating " +
            "from card c inner join user_card_queue u on c.card_id=u.card_id " +
            "where deck_id = :deckId and u.account_email = :accountEmail " +
            "order by case u.status " +
            "when 'BAD' then 1 " +
            "when 'NORMAL' then 2 " +
            "when 'GOOD' then 3 " +
            "end, u.card_data limit :limitNumber", nativeQuery = true)
    List<Card> cardsQueueForLearningWithStatus(@Param("accountEmail") String accountEmail, @Param("deckId") long deckId,
                                               @Param("limitNumber") long limitNumber);

    List <Card> findAllByQuestion(String question);
}
