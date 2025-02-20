package com.healthy.backend.service;

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

    private String getResetPasswordMailBody(String resetLink) {
        return "Subject: Password Reset Request\n\n" +
                "Dear User,\n\n" +
                "We have received a request to reset your account password. To proceed, please click the link below:\n\n" +
                resetLink + "\n\n" +
                "This link will remain valid for the next **12 hours**. If you do not reset your password within this timeframe, you will need to submit a new request.\n\n" +
                "If you did not initiate this request, please disregard this email. No further action is required on your part.\n\n" +
                "For any assistance, please contact our support team.\n\n" +
                "Best regards,\n" +
                "Mental Health Care Support Team";
    }

    private String getVerificationMailBody(String verificationLink) {
        return "Subject: Email Verification Required\n\n" +
                "Dear User,\n\n" +
                "Thank you for registering with **Mental Health Care**. " +
                "To complete your registration, please verify your email address by clicking the link below:\n\n" +
                verificationLink + "\n\n" +
                "This verification link is valid for the next **12 hours**. If you do not complete the verification within this period, you may need to request a new link.\n\n" +
                "If you did not initiate this request, please ignore this email.\n\n" +
                "For any questions or assistance, please reach out to our support team.\n\n" +
                "Best regards,\n" +
                "Mental Health Care Support Team";
    }

    private String getForgotPasswordMailBody(String verificationLink) {
        return "Subject: Password Reset Request\n\n" +
                "Dear User,\n\n" +
                "We have received a request to reset your account password. To proceed, please click the link below:\n\n" +
                verificationLink + "\n\n" +
                "This link will remain valid for the next **12 hours**. If you do not reset your password within this timeframe, you will need to submit a new request.\n\n" +
                "If you did not initiate this request, please disregard this email. No further action is required on your part.\n\n" +
                "For any assistance, please contact our support team.\n\n" +
                "Best regards,\n" +
                "Mental Health Care Support Team";
    }

    public String getAppointmentTransferredMailBody(String psychologistName,
                                                     Students student,
                                                     String ID,
                                                     TimeSlots timeSlots) {
        return "Subject: Appointment Transferred\n\n" +
                "Dear " + psychologistName + ",\n\n" +
                "Please be informed that an appointment has been transferred.\n\n" +
                "The details of the transferred appointment are as follows:\n\n" +
                "**Student:** " + student.getUser().getFullName() + "\n" +
                "**Class:**" + student.getGrade() + student.getClassName() + "\n" +
                "**Appointment ID:** " + ID + "\n" +
                "**Date:** " + timeSlots.getSlotDate() + "\n" +
                "**Time:** " + timeSlots.getStartTime() + " - " + timeSlots.getEndTime() + "\n\n" +
                "Please log in to the system for further details.\n\n" +
                "If you have any questions, feel free to contact our support team.\n\n" +
                "Best regards,\n" +
                "Mental Health Care Support Team";
    }

    public String getNewAppointmentMailBody(String psychologistName,
                                             Students student,
                                             String ID,
                                             TimeSlots timeSlots) {
        return "Subject: New Appointment Scheduled\n\n" +
                "Dear " + psychologistName + ",\n\n" +
                "We are pleased to inform you that a new appointment has been scheduled.\n\n" +
                "You have a new appointment scheduled with the following details:\n" +
                "**Student:** " + student.getUser().getFullName() + "\n" +
                "**Class:**" + student.getGrade() + student.getClassName() + "\n" +
                "**Appointment ID:** " + ID + "\n" +
                "**Date:** " + timeSlots.getSlotDate() + "\n" +
                "**Time:** " + timeSlots.getStartTime() + " - " + timeSlots.getEndTime() + "\n\n" +
                "Please log in to the system to view more details or manage your appointments.\n\n" +
                "If you have any questions or need assistance, please contact our support team.\n\n" +
                "Best regards,\n" +
                "Mental Health Care Support Team";
    }


    @Async
    public void sendVerificationEmail(String email, String verificationLink, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mail);
            message.setTo(email);
            message.setSubject(subject);
            message.setText(getVerificationMailBody(verificationLink));

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }

    }

    @Async
    public void sendPasswordResetEmail(String email, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mail);
            message.setTo(email);
            message.setSubject("Password Reset Request");
            message.setText(getResetPasswordMailBody(resetLink));

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending password reset email: " + e.getMessage());
        }
    }

    @Async
    public void sendForgotPasswordEmail(String email, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mail);
            message.setTo(email);
            message.setSubject("Forgot Password Request");
            message.setText(getForgotPasswordMailBody(resetLink));

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending forgot password email: " + e.getMessage());
        }
    }

    @Async
    public void sendNotificationEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
