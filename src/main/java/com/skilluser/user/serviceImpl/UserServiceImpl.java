package com.skilluser.user.serviceImpl;

import com.skilluser.user.fiegnclient.StudentEmploymentClient;
import com.skilluser.user.model.*;
import com.skilluser.user.repository.*;


import com.skilluser.user.model.User;

import com.skilluser.user.service.ModuleService;
import com.skilluser.user.service.OtpService;
import com.skilluser.user.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final String SUPPORT_EMAIL = "support@skillpilots.com";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PaymentHistoryRepo paymentHistoryRepo;
    @Autowired
    private PaymentStatusRepo paymentStatusRepo;
    @Autowired
    private PlanDetailsRepository planDetailsRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private BusinessUserRepository businessUserRepository;
    @Autowired
    private AssessmentCheckRepository assessmentCheckRepository;
    @Autowired
    @Lazy
    private ModuleService moduleService;
    @Autowired
    private StudentEmploymentClient studentEmploymentClient;
    @Autowired
    private OtpService otpService;
    @Autowired
    private  StudentWorkStatusService workStatusService;
    @Autowired
    private DepartmentRepository departmentRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found " + id));
    }


    @Override
    public List<User> findUsersByRoleAndDepartment(Long roleId, Long departmentId) {
        return userRepository.findUsersByRoleAndDepartment(roleId, departmentId);
    }

    @Override
    public List<User> findUsersByRoles_IdAndCollegeId(Long roleId, Long collegeId) {
        return userRepository.findUsersByRoles_IdAndCollegeId(roleId, collegeId);
    }

    @Override
    public List<User> findHodByDepartment(Long roleId, Long departmentId) {
        return userRepository.findHodByDepartment(roleId, departmentId);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> exist = userRepository.findById(userId);
        if (exist.isEmpty()) {
            return false;
        }
        User user = exist.get();
        boolean passwordMatches = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!passwordMatches) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPlainPassword(newPassword);
        userRepository.save(user);
        return true;
    }


    @Override
    public User forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("SubAdmin Not Found with email: " + email);
        }

        String prefix = (user.getName() != null && user.getName().length() >= 2)
                ? user.getName().substring(0, 2)
                : "SP";
        String newPassword = generateOTP() + prefix;
        user.setPlainPassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        // Save into otp table

        String body = "<div style=\"max-width: 500px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; "
                + "font-family: Arial, sans-serif; background: #f9f9f9; padding: 20px; border-radius: 10px; "
                + "box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">"
                + "<h1 style=\"text-align:center; color:#4A90E2;\">SkillPilots - Password Reset</h1>"
                + "<p style=\"font-size:16px; color:#333; text-align:center;\">We have generated a temporary password for your account.</p>"
                + "<p style=\"font-size:16px; color:#333; text-align:center;\">Use the password below to log in:</p>"
                + "<h2 style=\"text-align:center; color:#e74c3c;\">" + newPassword + "</h2>"
                + "<p style=\"text-align:center; font-size:14px; color:#666;\">After logging in, you must create a new password immediately for security reasons.</p>"
                + "<div style=\"margin-top:20px; text-align:center;\">"
                + "<small style=\"color:#aaa;\">If you did not request this, please ignore this email or contact our support team.</small>"
                + "</div>"
                + "</div>";


        otpService.sendVerificationEmail(user.getEmail(), "Password Reset Request", body);

        return user; // just return user, don’t save otp inside user
    }

    @Override
    public User getAllDataByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    @Override
    public Map<String, Object> checkPayment(Long userId) {

        Map<String, Object> response = new HashMap<>();

        // 🔹 Fetch User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        boolean hasPaid = false;
        String role = user.getRoles().getName(); // INT_STUDENT / EXT_STUDENT

        // ================= INTERNAL STUDENT =================
        if ("INT_STUDENT".equalsIgnoreCase(role)) {

            PaymentStatus paymentStatus =
                    paymentStatusRepo.findByCollegeId(user.getCollegeId());

            if (paymentStatus != null
                    && "PAID".equalsIgnoreCase(paymentStatus.getStatus())) {

                // ✅ College has paid → student auto paid
                hasPaid = true;

            } else {
                // College not paid → check student payment
                PaymentHistory payment =
                        paymentHistoryRepo.findTopByUserIdAndStatusOrderByPaymentDateDesc(
                                userId, "CHARGED"
                        );
                hasPaid = (payment != null);
            }
        }

        // ================= EXTERNAL STUDENT =================
        else if ("EXT_STUDENT".equalsIgnoreCase(role)) {

            PaymentHistory payment =
                    paymentHistoryRepo.findTopByUserIdAndStatusOrderByPaymentDateDesc(
                            userId, "CHARGED"
                    );

            hasPaid = (payment != null);
        }

        // ================= RESPONSE =================
        if (!hasPaid) {
            response.put("status", "payment_required");
            response.put("message", "Payment required before applying");
            response.put("hasPaid", false);
            return response;
        }

        response.put("status", "success");
        response.put("hasPaid", true);
        return response;
    }

    @Override
    public Map<String, Object> getPlanAmount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        boolean hasPaid = false;
        String role = user.getRoles().getName();
        Map<String, Object> response = new HashMap<>();

        PlanDetails plan = null;

        //  EXTERNAL → BASIC ONLY
        if ("EXT_STUDENT".equalsIgnoreCase(role)) {
            plan = planDetailsRepository.findByCollegeId(0L).orElse(null);
        }

        //  INTERNAL
        else if ("INT_STUDENT".equalsIgnoreCase(role)) {
            Long collegeId = (long) user.getCollegeId();
            plan = planDetailsRepository.findByCollegeId(collegeId).orElse(null);

            // fallback to BASIC
            if (plan == null) {
                plan = planDetailsRepository.findByCollegeId(0L).orElse(null);
            }
        }

        //  FINAL SAFE RESPONSE
        if (plan == null) {
            response.put("amount", 0);
            response.put("totalAmt", 0);
        } else {
            response.put("amount", plan.getAmount());
            response.put("totalAmt", plan.getTotalAmt());
        }

        return response;
    }

    @Override
    @Transactional
    public void processContact(ContactRequest req) {
        contactRepository.save(req);
        sendMailToSupport(req);
        sendConfirmationToUser(req);

    }

    @Override
    public BusinessUser createUser(BusinessUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return businessUserRepository.save(user);
    }

    @Override
    public boolean hasStudentGivenTest(Long studentId) {
        return assessmentCheckRepository.hasGivenAnyAssessment(studentId);

    }


    private void sendMailToSupport(ContactRequest req) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(SUPPORT_EMAIL);
        message.setSubject("New Contact Form: " + req.getType());

        message.setText(
                "Name: " + req.getFullName() + "\n" +
                        "Email: " + req.getEmail() + "\n" +
                        "Contact: " + req.getContactNumber() + "\n" +
                        "Designation: " + req.getDesignation() + "\n" +
                        "Type: " + req.getType() + "\n\n" +
                        "Message:\n" + req.getComment()
        );

        javaMailSender.send(message);
    }

    private void sendConfirmationToUser(ContactRequest req) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(req.getEmail());
        message.setSubject("We received your message");

        message.setText(
                "Hi " + req.getFullName() + ",\n\n" +
                        "Thanks for contacting us. Our team will reach out to you shortly.\n\n" +
                        "Regards,\nSupport Team"
        );

        javaMailSender.send(message);
    }

    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

//    public Map<String, Object> buildProfile(Long userId, Claims claims) {
//
//        Map<String, Object> workStatus =workStatusService. getWorkStatus(userId);
//        String username = userRepository.findById(userId)
//                .map(User::getName)
//                .orElse("NA");
//
//        Map<String, Object> permissions = moduleService.getPermissionsForUser(userId);
//        boolean given = assessmentCheckRepository.hasGivenAnyAssessment(userId);
//
//        return Map.of(
//                "valid", true,
//                "user", claims,
//                "permission", permissions,
//                "name", username,
//                "work_status", workStatus,
//                "hasGivenSkillAssessment", given
//        );
//    }


    public Map<String, Object> buildProfile(Long userId, Claims claims) {

        Map<String, Object> workStatus = workStatusService.getWorkStatus(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String username = user.getName();

        Map<String, Object> permissions = moduleService.getPermissionsForUser(userId);
        boolean given = assessmentCheckRepository.hasGivenAnyAssessment(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
       // response.put("user", claims);
        response.put("permission", permissions);
        response.put("name", username);
        response.put("work_status", workStatus);
        response.put("hasGivenSkillAssessment", given);

        // ==================================================
        // ⭐ MODIFY USER OBJECT
        // ==================================================
        Map<String, Object> userMap = new HashMap<>(claims);

        if (user.getRoles() != null &&
                "HOD".equalsIgnoreCase(user.getRoles().getName())) {

            Long deptId = user.getDepartment();

            String deptName = Optional.ofNullable(
                    departmentRepository.getDepartmentNameById(deptId)
            ).orElse("N/A");

            userMap.put("departmentName", deptName);
        }

        response.put("user", userMap);

        return response;
    }


}
