package com.reddot.app.service;

import com.reddot.app.entity.Membership;
import com.reddot.app.entity.User;
import com.reddot.app.entity.UserOnDelete;
import com.reddot.app.repository.UserRepository;
import com.reddot.app.repository.userDeleteRepository;
import com.reddot.app.service.email.MailSenderManager;
import com.reddot.app.service.user.UserServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TaskSchedulerImp {

    public static final int DELETE_DAYS = 15;
    public static final int WARNING_DAYS = 13;
    private final String logMsg = "SCHEDULED TASK";
    private final userDeleteRepository userDeleteRepository;
    private final LocalDateTime cutoffDate = LocalDateTime.now().minusDays(DELETE_DAYS);
    private final LocalDateTime warningDate = LocalDateTime.now().minusDays(WARNING_DAYS);
    private final UserRepository userRepository;
    private final MailSenderManager mailSenderManager;
    private final UserServiceManager userServiceManager;
    Set<UserOnDelete> onDeletes = new HashSet<>();

    public TaskSchedulerImp(userDeleteRepository userDeleteRepository, UserRepository userRepository, MailSenderManager mailSenderManager, UserServiceManager userServiceManager) {
        this.userDeleteRepository = userDeleteRepository;
        this.userRepository = userRepository;
        this.mailSenderManager = mailSenderManager;
        this.userServiceManager = userServiceManager;
    }

    // TODO: DOCS ME
    // Run at 00:00 every day to delete users
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    protected void userDeleteExecutor() {
        log.info(logMsg + " - USER EXECUTE");
        onDeletes = getOnDeletesBefore(cutoffDate, true);
        onDeletes.forEach(deleteRequest -> {
            userRepository.deleteById(deleteRequest.getUserId());
            userDeleteRepository.delete(deleteRequest);
            log.warn("USER DELETED - ID: {}", deleteRequest.getUserId());
        });
    }

    // TODO: DOCS ME
    // Run at 00:00 every day to send alert emails
    @Scheduled(cron = "0 0 0 * * *")
    protected void userDeleteSendAlertMail() {
        log.info(logMsg + " - DELETE ALERT");
        String subject = "Account Deletion Alert";
        String emailBody = "Your Reddot account will be deleted in " + (DELETE_DAYS - WARNING_DAYS) * 24 + " hours. Please carefully consider this action.";
        try {
            Set<UserOnDelete> deleteRequestSet = getOnDeletesBefore(warningDate, false);
            if (deleteRequestSet.isEmpty()) {
                return;
            }
            deleteRequestSet.forEach(request -> {
                log.info(request.toString());
                User user = getUser(request.getUserId());
                if (user == null) {
                    userDeleteRepository.delete(request);
                } else {
                    mailSenderManager.sendEmail(user.getEmail(), subject, emailBody);
                    request.setIsNoticed(true);
                    userDeleteRepository.save(request);
                    log.info("ALERT EMAIL SENT - ID: {}", request.getUserId());
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    protected void handleMembershipDowngrades() {
        List<User> users = userRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (User user : users) {
            Membership membership = user.getMembership();

            if (membership.isActive() && membership.getEndDate() != null && membership.getEndDate().isBefore(now)) {
                userServiceManager.membershipDowngrade(user);
            }
        }
    }

    private User getUser(Integer id) {
        Assert.notNull(id, "ID CANNOT BE NULL");
        return userRepository.findById(id).orElse(null);
    }

    /**
     * @param cutoffDate     : date to compare
     * @param includeNoticed : to avoid send multiple emails to same user
     */
    private Set<UserOnDelete> getOnDeletesBefore(LocalDateTime cutoffDate, boolean includeNoticed) {
        if (includeNoticed) {
            return new HashSet<>(userDeleteRepository.findAllByCreatedAtBefore(cutoffDate));
        }
        return new HashSet<>(userDeleteRepository.findAllByCreatedAtBeforeAndIsNoticedIsFalse(cutoffDate));
    }
}