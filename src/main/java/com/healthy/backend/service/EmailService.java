package com.healthy.backend.service;

import com.healthy.backend.entity.ProgramSchedule;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.TimeSlots;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mail;

    private String getEmailBody(String subject, String content, String name) {
        return EmailTemplate.getEmailBody(subject, content, name);
    }

    @Async
    public void sendProgramCancellationEmail(String email, Programs programs, String name) {
        String subject = "Program Registration Cancellation for " + programs.getProgramName();

        String content =
                "We regret to inform you that your registration for the **" + programs.getProgramName() + "** program has been canceled.\n\n" +
                "We understand this may be disappointing, and we apologize for any inconvenience caused. If you would like to inquire about re-registering or if you have any questions regarding this cancellation, please feel free to contact us.\n\n" +
                "Here are the details of the program you were registered for:\n\n" +
                "Program Name: **" + programs.getProgramName() + "**\n" +
                "Scheduled Start Date: **" + programs.getStartDate() + "**\n" +
                "If you wish to re-register or need further assistance, please don’t hesitate to get in touch with us. " +
                "We are here to help and support you.\n\n";

        sendEmail(email, subject, content, name);
    }


    @Async
    public void sendRegistrationConfirmationEmail(String email, Programs programs, ProgramSchedule schedule, String name) {
        String subject = "Confirmation: Registration Successful for " + programs.getProgramName();

        String content =
                "We are pleased to confirm your registration for the **" + programs.getProgramName() + "** program.\n\n" +
                "The program is scheduled to take place on **" + programs.getStartDate() + "** at **" + schedule.getStartTime() + "**. " +
                "The program duration is **" + programs.getDuration() + " hours**, and will conclude at **" + schedule.getEndTime() + "**.\n\n" +
                "Please make sure you are well-prepared for the program to maximize your participation.\n\n" +
                "To join the program, please use the following meeting link: **" + programs.getMeetingLink() + "**.\n\n" +
                "Additionally, you can also find the meeting link in your calendar on our website. Simply log in to your account and check your program schedule for easy access to the link.\n\n" +
                "Should you have any questions or require additional information, please do not hesitate to reach out to us. We are here to assist you.\n\n" +
                "We are excited about your participation in the program and look forward to your involvement.\n\n";
        sendEmail(email, subject, content, name);
    }

    @Async
    public void sendProgramParticipationReminder(String email, Programs programs, ProgramSchedule schedule, String name, boolean isToday) {
        String subject = "Reminder: Upcoming Program";
        String date = isToday ? "today." : "scheduled for tomorrow.";
        String content =
                "We hope this message finds you well. This is a courtesy reminder regarding your registration for the **" + programs.getProgramName() + "** program " + date + "\n\n" +
                "The program is scheduled to take place on **" + programs.getStartDate() + "** at **" + schedule.getStartTime() + "**. " +
                "Please note that the program duration is **" + programs.getDuration() + " hours, ending at " + schedule.getEndTime() + "**.\n\n" +
                "Please ensure that you are well-prepared and arrive on time to maximize your participation.\n\n" +
                "To join the program, please use the following meeting link: **" + programs.getMeetingLink() + "**.\n\n" +
                "Additionally, you can also find the meeting link in your calendar on our website. Simply log in to your account and check your program schedule for easy access to the link.\n\n" +
                "Should you have any questions or require additional information, please do not hesitate to reach out to us. We are here to assist you.\n\n" +
                "We look forward to your participation in the program and are confident it will be a valuable experience.\n\n";
        sendEmail(email, subject, content, name);
    }

    @Async
    public void sendNewAppointmentEmail(String email, String psychologistName, Students student, String ID, TimeSlots timeSlots, String subject) {
        String content = subject.toLowerCase().contains("transfer") ? "We are pleased to informed that an appointment of yours has been transferred.\n\n" +
                "The details of the transferred appointment are as follows:\n\n" +
                "**Student:** " + student.getUser().getFullName() + "\n" +
                "**Class:** " + student.getGrade() + student.getClassName() + "\n" +
                "**Appointment ID:** " + ID + "\n" +
                "**Date:** " + timeSlots.getSlotDate() + "\n" +
                "**Time:** " + timeSlots.getStartTime() + " - " + timeSlots.getEndTime() + "\n\n" +
                "Please log in to the system for further details." :
                // If the subject doesn't contain "transfer", use the original content
                "We are pleased to inform you that a new appointment has been scheduled.\n\n" +
                        "You have a new appointment scheduled with the following details:\n" +
                        "**Student:** " + student.getUser().getFullName() + "\n" +
                        "**Class:**" + student.getGrade() + student.getClassName() + "\n" +
                        "**Appointment ID:** " + ID + "\n" +
                        "**Date:** " + timeSlots.getSlotDate() + "\n" +
                        "**Time:** " + timeSlots.getStartTime() + " - " + timeSlots.getEndTime() + "\n\n" +
                        "Please log in to the system to view more details or manage your appointments.\n\n";
        sendEmail(email, subject, content, psychologistName);
    }

    @Async
    public void sendPasswordResetEmail(String email, String resetLink, String name) {
        String subject = "Password Reset Request";
        String content = "We have received a request to reset your account password. To proceed, please click the link below:\n\n" +
                "Click the link to reset your password: " + resetLink + "\n\n" +
                "This link will remain valid for the next **12 hours**. If you do not reset your password within this timeframe, you will need to submit a new request.\n\n" +
                "If you did not initiate this request, please disregard this email.";

        sendEmail(email, subject, content, name);
    }

    @Async
    public void sendVerificationEmail(String email, String verificationLink, String name) {
        String subject = "Email Verification Required";
        String content = "Thank you for registering with **Mental Health Care**. " +
                "To complete your registration, please verify your email address by clicking the link below:\n\n" +
                "Click the link to verify your account: " + verificationLink + "\n\n" +
                "This verification link is valid for the next **12 hours**. If you do not complete the verification within this period, you may need to request a new link.\n\n" +
                "If you did not initiate this request, please ignore this email.";

        sendEmail(email, subject, content, name);
    }

    @Async
    public void sendForgotPasswordEmail(String email, String resetLink, String name) {
        String subject = "Forgot Password Request";
        String content = "We have received a request to reset your account password. To proceed, please click the link below:\n\n" +
                "Click the link to reset your password: " + resetLink + "\n\n" +
                "This link will remain valid for the next **12 hours**. If you do not reset your password within this timeframe, you will need to submit a new request.\n\n" +
                "If you did not initiate this request, please disregard this email.";

        sendEmail(email, subject, content, name);
    }

    private void sendEmail(String toEmail, String subject, String body, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(getEmailBody(subject, body, name));
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}

class EmailTemplate {
    public static String getEmailBody(String subject, String bodyContent, String name) {
        return "Subject: " + subject + "\n\n" +
                "Dear " + name + ",\n\n" +
                bodyContent + "\n\n" +
                "For any questions or assistance, please reach out to our support team.\n\n" +
                "Best regards,\n" +
                "Mental Health Care Support Team";
    }
}
