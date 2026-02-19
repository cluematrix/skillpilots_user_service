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
import java.util.ArrayList;
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

        List<User> targetUsers = new ArrayList<>();

        for (String role : event.getTargetRoles()) {
            List<User> usersForRole = new ArrayList<>();
         //   System.out.println("zzzzzzz");
            if (event.getCollegeId() != null && event.getDeptId() != null) {
              //  System.out.println("collegeIdzZzzzzzzzzzzzz"+event.getCollegeId()+"deptIdZZZZZZZZZ"+event.getDeptId()+" "+"role: "+role);
                usersForRole = userRepository.findByRoles_NameInAndCollegeIdAndDepartment(
                        List.of(role),
                        event.getCollegeId(),
                        event.getDeptId()
                );
                System.out.println(" Found HOD users: " + usersForRole.size());
                targetUsers.addAll(usersForRole);
            }
            if (event.getCompanyId() != null) {
                usersForRole = userRepository.findByRoles_NameInAndCompanyId(
                        List.of(role),
                        event.getCompanyId()
                );
                System.out.println("  Found company users: " + usersForRole.size());
            }
            else if (event.getCollegeId() != null) {
                usersForRole = userRepository.findByRoles_NameInAndCollegeId(
                        List.of(role),
                        event.getCollegeId()
                );
            }
            else {
                usersForRole = userRepository.findByRoles_NameIn(List.of(role));
            }

            targetUsers.addAll(usersForRole);
        }




//  Additionally send to the specific student if studentId is present
        if (event.getStudentId() != null) {
            userRepository.findById(event.getStudentId())
                    .ifPresent(targetUsers::add);
        }

//  Remove duplicate users if any (same user may have multiple roles)
        targetUsers = targetUsers.stream().distinct().toList();




        // save notifiction in database according to users
        for(User user:targetUsers) {
            Notification notification = new Notification();
            notification.setMessage(event.getMessage());
            Long newCollegeId = Long.valueOf(user.getCollegeId());
            notification.setCollegeId(newCollegeId);
            notification.setCompanyId(user.getCompanyId());
            notification.setType(event.getType());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setReceiverId(user.getId());
            notification.setDeptId(user.getDepartment());


            notificationRepository.save(notification);

            // ⭐ create new event for each user
            NotificationEvent userEvent = new NotificationEvent(
                    event.getType(),
                    event.getMessage(),
                    event.getTargetRoles(),
                    event.getCollegeId(),
                    event.getDeptId(),
                    event.getStudentId(),
                    event.getCompanyId(),
                    event.getPath(),
                    notification.getNotificationId()   // correct id
            );

            //  Send real-time notification via WebSocket to each target user
            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + user.getId(),
                    userEvent
            );
        }


        System.out.println("taregetUsers>>>>>>>"+targetUsers);
        //  Send real-time notification via WebSocket to each target user
//        for (User user : targetUsers) {
//            messagingTemplate.convertAndSend(
//                    "/topic/notifications/" + user.getId(),
//                    event
//            );
//        }

        System.out.println(" Sent notification to " + targetUsers.size() + " users.");
        System.out.println("Target Roles: " + event.getTargetRoles());
        System.out.println("CompanyId: " + event.getCompanyId());
        System.out.println("CollegeId: " + event.getCollegeId());
        System.out.println("Total target users found: " + targetUsers.size());
        targetUsers.forEach(u -> System.out.println(" → User: " + u.getId() + " (" + u.getRoles() + ")"));

    }
}
