package com.example.journals_backend.repo;

import com.example.journals_backend.entity.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification,Long> {

     List<Notification> findByEmail(String email);

     @Modifying
     @Transactional
     @Query("UPDATE Notification n SET n.isRead = true WHERE n.email = :email AND n.isRead = false")
     int markAllAsReadByEmail(@Param("email") String email);


     @Query("SELECT COUNT(n) FROM Notification n WHERE n.email = :email AND n.isRead = false")
     long countUnreadByEmail(@Param("email") String email);
}
