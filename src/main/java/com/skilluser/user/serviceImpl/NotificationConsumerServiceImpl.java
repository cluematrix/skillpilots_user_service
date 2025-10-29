package com.skilluser.user.serviceImpl;

import com.skilluser.user.model.Notification;
import com.skilluser.user.model.NotificationEvent;
import com.skilluser.user.model.User;
import com.skilluser.user.repository.NotificationRepository;
import com.skilluser.user.repository.UserRepository;
import com.skilluser.user.service.NotificationConsumerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// 22/10/2025 - Ashvin Chopkar - Dynamic notification receiver & dispatcher
@Service
public class NotificationConsumerServiceImpl implements NotificationConsumerService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    @RabbitListener(queues = "notification_queue")
    public void receiveNotification(NotificationEvent event) {

        System.out.println(" Received notification: " + event);

        if (event.getTargetRoles() == null || event.getTargetRoles().isEmpty()) {
            System.out.println("⚠ No target roles provided for notification.");
            return;
        }

        List<User> targetUsers;

        //  Filter dynamically based on context
        if (event.getCollegeId() != null) {
            targetUsers = userRepository.findByRoles_NameInAndCollegeId(event.getTargetRoles(), event.getCollegeId());
        }
        else if (event.getCompanyId() != null) {
            targetUsers = userRepository.findByRoles_NameInAndCompanyId(event.getTargetRoles(), event.getCompanyId());
            //System.out.println("users>>>zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+targetUsers);
        }
        else {
            targetUsers = userRepository.findByRoles_NameIn(event.getTargetRoles());
        }


        for(User user:targetUsers) {
            Notification notification = new Notification();
            notification.setMessage(event.getMessage());
            notification.setCollegeId(event.getCollegeId());
            notification.setCompanyId(event.getCompanyId());
            notification.setType(event.getType());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setReceiverId(user.getId());

            notificationRepository.save(notification);
        }


        System.out.println("taregetUsers>>>>>>>"+targetUsers);
        //  Send real-time notification via WebSocket to each target user
        for (User user : targetUsers) {
            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + user.getId(),
                    event
            );
        }

        System.out.println(" Sent notification to " + targetUsers.size() + " users.");
        System.out.println("Target Roles: " + event.getTargetRoles());
        System.out.println("CompanyId: " + event.getCompanyId());
        System.out.println("CollegeId: " + event.getCollegeId());
        System.out.println("Total target users found: " + targetUsers.size());
        targetUsers.forEach(u -> System.out.println(" → User: " + u.getId() + " (" + u.getRoles() + ")"));

    }
}
