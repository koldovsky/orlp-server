package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    String selectAllDeckIdWithFolder = "SELECT decks_deck_id FROM orlp.folder_decks where folder_folder_id = ?1;";

    @Query(value = "SELECT d.id FROM Folder f INNER JOIN f.decks as d where f.id = :folder_id")
    List<Long> selectAllDeckIdWithFolder(@Param("folder_id") Long folderId);

    @Query(value = "SELECT d from Folder f inner join f.decks as d where d.id = :deck_id and  f.id = :folder_id")
    List<Deck> getAccessToDeckFromFolder(@Param("folder_id") Long folderId, @Param("deck_id") Long deckId);

    @Query(value = "SELECT f from Folder f inner join f.decks as d where d.id = :deck_id")
    List<Folder> getAllFolderWhereIdDecksEquals(@Param("deck_id") Long deckId);

}
