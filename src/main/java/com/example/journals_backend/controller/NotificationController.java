package com.example.journals_backend.controller;

import com.example.journals_backend.entity.Notification;
import com.example.journals_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/get_notifications/{email}")
    public ResponseEntity<List<Notification>> getNotificationByEmail(@PathVariable String email){
       List<Notification> notifications=notificationService.getNotificationByUser(email);
        return ResponseEntity.ok(notifications);

    }
    @GetMapping("/get_all")
    public ResponseEntity<List<Notification>> getNotificationAll(){
       List<Notification> notifications=notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);

    }
    @PutMapping("/mark_all_read/{email}")
    public ResponseEntity<String> markAllAsRead(@PathVariable String email) {
        int updatedCount = notificationService.setNotificationtrue(email);
        return ResponseEntity.ok(updatedCount + " notifications marked as read");
    }

    @GetMapping("/count_unread/{email}")
    public ResponseEntity<Long> countUnread(@PathVariable String email) {
        long count = notificationService.countUnreadNotifications(email);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/add/")
    public ResponseEntity<Notification> addNotifications(@RequestParam("title") String title,
                                                               @RequestParam("message") String message,
                                                               @RequestParam("email") String email,
                                                               @RequestParam("status") String status,
                                                               @RequestParam("created") String created
                                                               ){
       Notification notifications=notificationService.addNotification(title,email,status,message,created);
        return ResponseEntity.ok(notifications);

    }

}
