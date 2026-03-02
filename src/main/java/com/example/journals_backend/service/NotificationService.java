package com.example.journals_backend.service;


import com.example.journals_backend.entity.Notification;
import com.example.journals_backend.repo.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    public List<Notification> getNotificationByUser(String email){
        return notificationRepo.findByEmail(email);
    }
     public List<Notification> getAllNotifications(){
        return notificationRepo.findAll();
    }

    public Notification addNotification(String title, String email, String status, String message,String created) {
        Notification notification = new Notification();
        notification.setEmail(email);
        notification.setTitle(title);
        notification.setStatus(status);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setDateCreated(created);
        return notificationRepo.save(notification);
    }


    public int setNotificationtrue(String email){
     return   notificationRepo.markAllAsReadByEmail(email);

    }

    public long countUnreadNotifications(String email) {
        return notificationRepo.countUnreadByEmail(email);
    }



}
