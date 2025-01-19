package com.healthy.BackEnd;

import com.healthy.BackEnd.init.DataInitializer;
import com.healthy.BackEnd.repository.UserRepository;
import com.healthy.BackEnd.repository.StudentRepository;
import com.healthy.BackEnd.repository.BlogRepository;
import com.healthy.BackEnd.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackEndApplication {

	@Autowired
	private static UserRepository userRepository;

	@Autowired
	private static StudentRepository studentRepository;

	@Autowired
	private static BlogRepository blogRepository;

	@Autowired
	private static NotificationRepository notificationRepository;

	private static boolean isDatabaseEmpty() {
		return userRepository == null &&
			   studentRepository == null &&
			   blogRepository == null &&
			   notificationRepository == null;
	}

	private static void initDatabase() {
		if (isDatabaseEmpty()) {
			new DataInitializer().initialize();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
		initDatabase();
	}
}
