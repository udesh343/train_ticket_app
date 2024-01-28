package org.apache.maven.train_ticket_app.controllers;

import org.apache.maven.train_ticket_app.Model.Ticket;
import org.apache.maven.train_ticket_app.Model.TicketRequest;
import org.apache.maven.train_ticket_app.service.TrainTicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ticket")
public class TrainTicketController {

    private final TrainTicketService trainTicketService;

    public TrainTicketController(TrainTicketService trainTicketService) {
        this.trainTicketService = trainTicketService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> purchaseTicket(@RequestBody TicketRequest ticketRequest) {
        Ticket ticket = trainTicketService.purchaseTicket(ticketRequest);
        if (ticket != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("ticketId", ticket.getId().toString());
            response.put("message", "Ticket purchased successfully. Please find the receipt below:");
            response.put("receipt", ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/receipt/{ticketId}")
    public ResponseEntity<Ticket> getReceipt(@PathVariable UUID ticketId) {
        Ticket ticket = trainTicketService.getReceipt(ticketId);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/{section}")
    public ResponseEntity<Object> getUsersBySection(@PathVariable String section) {
        List<Map<String, Object>> seatsInSection = trainTicketService.getUsersBySection(section);
        Map<String, Object> response = new HashMap<>();

        if (!seatsInSection.isEmpty()) {
            response.put("section", section);
            response.put("seats", seatsInSection);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "No users found for section " + section);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    @DeleteMapping("/remove/{ticketId}")
    public ResponseEntity<Object> removeUserFromTrain(@PathVariable UUID ticketId) {
        boolean removed = trainTicketService.removeUserFromTrain(ticketId);
        if (removed) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User successfully removed from the train.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User not found or could not be removed from the train.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/modify/{ticketId}")
    public ResponseEntity<Object> modifyUserSeat(@PathVariable UUID ticketId,
            @RequestBody Map<String, String> requestBody) {
        String newSeat = requestBody.get("newSeat");
        Ticket modifiedTicket = trainTicketService.modifyUserSeat(ticketId, newSeat);
        if (modifiedTicket != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User seat modified successfully.");
            response.put("modifiedTicket", modifiedTicket); // Assuming ticket object contains modified seat details
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ticket not found or seat could not be modified.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}