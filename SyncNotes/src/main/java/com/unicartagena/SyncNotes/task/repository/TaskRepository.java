package com.unicartagena.SyncNotes.task.repository;

import com.unicartagena.SyncNotes.task.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByRoomId(String roomId);
    List<Task> findByRoomIdAndCompleted(String roomId, boolean completed);
}
