package com.medius.demo;

import com.medius.demo.entities.Meeting;
import com.medius.demo.entities.MeetingResult;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

import com.medius.demo.entities.Customer;
import com.medius.demo.repositories.*;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private MeetingRepository meetingRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class);
	}

	@Override
	@Transactional
	public void run(String... strings) throws Exception {
		// Generate some test users and meetings

		Meeting meeting1 = new Meeting("Ljubljana", MeetingResult.Bill,
				LocalDate.parse("2019-03-03"), LocalTime.parse("10:00"),
				LocalDate.parse("2019-03-03"), LocalTime.parse("11:00"));
		Meeting meeting2 = new Meeting("Maribor", MeetingResult.Contract,
				LocalDate.parse("2019-03-04"), LocalTime.parse("15:00"),
				LocalDate.parse("2019-03-04"), LocalTime.parse("17:00"));
		Customer customer1 = new Customer("Jack", "Bauer", "0410001111", "test@test.com", meeting1, meeting2);
		Customer customer2 = new Customer("Chloe", "O'Brian", "0410001111", "test@test.com", meeting2);
		Customer customer3 = new Customer("Kim", "Bauer", "0410001111", "test@test.com", meeting1);
		Customer customer4 = new Customer("David", "Palmer", "0410001111", "test@test.com", meeting2);
		Customer customer5 = new Customer("Michelle", "Dessler", "0410001111", "test@test.com", meeting2);


		customerRepository.save(customer1);
		customerRepository.save(customer2);
		customerRepository.save(customer3);
		customerRepository.save(customer4);
		customerRepository.save(customer5);
		meetingRepository.save(meeting1);
		meetingRepository.save(meeting2);
	};
}
