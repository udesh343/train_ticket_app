package org.apache.maven.train_ticket_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.maven.train_ticket_app.Model.Ticket;
import org.apache.maven.train_ticket_app.Model.TicketRequest;
import org.apache.maven.train_ticket_app.Model.User;
import org.apache.maven.train_ticket_app.controllers.TrainTicketController;
import org.apache.maven.train_ticket_app.service.TrainTicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class TrainTicketAppApplicationTests {

	@Autowired
	private TrainTicketController trainTicketController;


	@Test
	public void testPurchaseTicket() {
		// Create ticket request
		TicketRequest ticketRequest = new TicketRequest("A", new User("Udesh", "Chaudhary", "udesh@test.com"));

		// Purchase ticket
		ResponseEntity<Object> responseEntity = trainTicketController.purchaseTicket(ticketRequest);

		Object responseBody = responseEntity.getBody();
		String message = "";

		if (responseBody instanceof Map) {
			message = ((Map<?, ?>) responseBody).get("message").toString();
		}

		// Verify response
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Ticket purchased successfully. Please find the receipt below:", message);

	}

	@Test
	public void testGetReceipt() {
		ResponseEntity<Object> responseEntity = trainTicketController
				.purchaseTicket(new TicketRequest("A", new User("Udesh", "Chaudhary", "udesh@test.com")));
		assertNotNull(responseEntity.getBody());
		String ticketId = (String) ((Map<String, Object>) responseEntity.getBody()).get("ticketId");

		// Get receipt
		ResponseEntity<Ticket> receiptResponseEntity = trainTicketController.getReceipt(UUID.fromString(ticketId));

		// Verify receipt
		assertEquals(HttpStatus.OK, receiptResponseEntity.getStatusCode());
		assertNotNull(receiptResponseEntity.getBody());
		assertEquals("London", receiptResponseEntity.getBody().getFrom());
		assertEquals("France", receiptResponseEntity.getBody().getTo());
		assertEquals("Udesh", receiptResponseEntity.getBody().getUser().getFirstName());
		assertEquals("Chaudhary", receiptResponseEntity.getBody().getUser().getLastName());
		assertEquals("udesh@test.com", receiptResponseEntity.getBody().getUser().getEmail());
	}

	@Test
	public void testGetUsersBySection() {
		// Purchase tickets for different sections
		ResponseEntity<Object> responseEntityA = trainTicketController
				.purchaseTicket(new TicketRequest("A", new User("Udesh", "Chaudhary", "udesh@test.com")));
		ResponseEntity<Object> responseEntityB = trainTicketController
				.purchaseTicket(new TicketRequest("B", new User("Avinesh", "Chaudhary", "avinesh@test.com")));

		// Retrieve users in section A
		ResponseEntity<Object> usersResponseEntityA = trainTicketController.getUsersBySection("A");
		Object usersInSectionA = usersResponseEntityA.getBody();

		assertEquals(HttpStatus.OK, usersResponseEntityA.getStatusCode());
		assertNotNull(usersInSectionA);

		// Retrieve users in section B
		ResponseEntity<Object> usersResponseEntityB = trainTicketController.getUsersBySection("B");
		Object usersInSectionB = usersResponseEntityB.getBody();

		assertEquals(HttpStatus.OK, usersResponseEntityB.getStatusCode());
		assertNotNull(usersInSectionB);
		assertEquals(1, ((List<?>) ((Map<?, ?>) usersInSectionB).get("seats")).size());
	}

	@Test
	public void testRemoveUserFromTrain() {
		ResponseEntity<Object> responseEntity = trainTicketController
				.purchaseTicket(new TicketRequest("A", new User("Udesh", "Chaudhary", "udesh@test.com")));
		assertNotNull(responseEntity.getBody());
		String ticketId = (String) ((Map<String, Object>) responseEntity.getBody()).get("ticketId");

		// Remove user from train
		ResponseEntity<Object> removeResponseEntity = trainTicketController
				.removeUserFromTrain(UUID.fromString(ticketId));

		// Verify removal
		assertEquals(HttpStatus.OK, removeResponseEntity.getStatusCode());

		// Try to get receipt for removed ticket
		ResponseEntity<Ticket> receiptResponseEntity = trainTicketController.getReceipt(UUID.fromString(ticketId));
		assertEquals(HttpStatus.NOT_FOUND, receiptResponseEntity.getStatusCode());
	}

	@Test
	public void testModifyUserSeat() {
		// Purchase ticket
		TicketRequest ticketRequest = new TicketRequest("A", new User("udesh", "Chaudhary", "udesh@test.com"));
		ResponseEntity<Object> responseEntity = trainTicketController.purchaseTicket(ticketRequest);
		String ticketId = (String) ((Map<String, Object>) responseEntity.getBody()).get("ticketId");

		// Modify user seat
		String newSeat = "AD2";
		ResponseEntity<Object> modifyResponseEntity = trainTicketController.modifyUserSeat(UUID.fromString(ticketId),
				Map.of("newSeat", newSeat));

		// Verify modification
		assertEquals(HttpStatus.OK, modifyResponseEntity.getStatusCode());
		assertNotNull(modifyResponseEntity.getBody());
		assertEquals("User seat modified successfully.", ((Map<?, ?>) modifyResponseEntity.getBody()).get("message"));
	}

}
