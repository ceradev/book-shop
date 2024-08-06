package com.backend.library.backend;

// import java.io.UnsupportedEncodingException;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.context.event.ApplicationReadyEvent;
// import org.springframework.context.event.EventListener;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.JavaMailSenderImpl;
// import com.backend.library.backend.services.implementations.EmailServiceImpl;
// import jakarta.mail.MessagingException;

@SpringBootApplication
public class BackendApplication {

	// @Autowired
	// private EmailServiceImpl emailService;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

	}

	// @EventListener(ApplicationReadyEvent.class)
	// public void sendEmail() {
	// // Create an instance of JavaMailSender
	// // JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

	// // Create an instance of EmailSender
	// // EmailServiceImpl emailSender = new EmailServiceImpl(mailSender);

	// // Call the sendEmail method to send an email
	// String recipientEmail = "khalifa.boulbayem.external@eviden.com";
	// String subject = "Hello from Spring Boot";
	// String content = "<p>Hello,</p><p>This is a test email sent from Spring
	// Boot.</p>";

	// try {
	// emailService.sendEmail(recipientEmail, subject, content);
	// System.out.println("Email sent successfully.");
	// } catch (MessagingException | UnsupportedEncodingException e) {
	// System.out.println("Failed to send email. Error: " + e.getMessage());
	// }

	// }
}
