package com.unicartagena.SyncNotes.history.repository;

import com.unicartagena.SyncNotes.history.entity.ChangeHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeHistoryRepository extends MongoRepository<ChangeHistory, String> {
    List<ChangeHistory> findByRoomIdOrderByTimestampDesc(String roomId);
}
