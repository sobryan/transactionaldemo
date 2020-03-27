package com.example.transactionaldemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
class AppRunner {
//implements CommandLineRunner {

	private final static Logger logger = LoggerFactory.getLogger(AppRunner.class);

	private final BookingService bookingService;
	private final NotifyingService notifyingService;

	public AppRunner(BookingService bookingService, NotifyingService notifyingService) {
		this.bookingService = bookingService;
		this.notifyingService = notifyingService;
	}

	@Scheduled(fixedRateString = "2000")
//	public void run(String... args) throws Exception {
	public void run() throws Exception {
		bookingService.deleteAll();
		notifyingService.deleteAll();
		
		
		bookingService.book("Alice", "Bob", "Carol");
		Assert.isTrue(bookingService.findAllBookings().size() == 3, "First booking should work with no problem");
		logger.info("Alice, Bob and Carol have been booked");

		notifyingService.notify("Alice", "Bob", "Carol");
		Assert.isTrue(notifyingService.findAllNotifications().size() == 3,
				"First notifying should work with no problem");
		logger.info("Alice, Bob and Carol have been notified");

		try {
			bookingService.book("Chris", "Samuel");
			notifyingService.notify("Chris", "Samuel");
		} catch (RuntimeException e) {
			logger.info("v--- The following exception is expect because 'Samuel' is too " + "big for the DB ---v");
			logger.error(e.getMessage());
		}

		for (String person : bookingService.findAllBookings()) {
			logger.info("So far, " + person + " is booked.");
		}
		logger.info("You shouldn't see Chris or Samuel. Samuel violated DB constraints, "
				+ "and Chris was rolled back in the same TX");
		Assert.isTrue(bookingService.findAllBookings().size() == 3, "'Samuel' should have triggered a rollback");

		for (String person : notifyingService.findAllNotifications()) {
			logger.info("So far, " + person + " is notified.");
		}
		logger.info("You shouldn't see Chris or Samuel. Samuel violated DB constraints, "
				+ "and Chris was rolled back in the same TX");
		Assert.isTrue(notifyingService.findAllNotifications().size() == 3, "'Samuel' should have triggered a rollback");

		try {
			bookingService.book("Buddy", null);
			notifyingService.notify("Buddy", null);
		} catch (RuntimeException e) {
			logger.info("v--- The following exception is expect because null is not " + "valid for the DB ---v");
			logger.error(e.getMessage());
		}

		for (String person : bookingService.findAllBookings()) {
			logger.info("So far, " + person + " is booked.");
		}
		logger.info("You shouldn't see Buddy or null. null violated DB constraints, and "
				+ "Buddy was rolled back in the same TX");
		Assert.isTrue(bookingService.findAllBookings().size() == 3, "'null' should have triggered a rollback");

		for (String person : notifyingService.findAllNotifications()) {
			logger.info("So far, " + person + " is booked.");
		}
		logger.info("You shouldn't see Buddy or null. null violated DB constraints, and "
				+ "Buddy was rolled back in the same TX");
		Assert.isTrue(notifyingService.findAllNotifications().size() == 3, "'null' should have triggered a rollback");
	}

}