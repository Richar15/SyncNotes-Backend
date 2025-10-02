package com.unicartagena.SyncNotes.room.repository;

import com.unicartagena.SyncNotes.room.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    List<Room> findByIsPublicTrue();
    List<Room> findByCreatorId(String creatorId);
    List<Room> findByMembers_UserId(String userId);
}
