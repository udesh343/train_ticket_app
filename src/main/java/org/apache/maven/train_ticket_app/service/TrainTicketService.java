package org.apache.maven.train_ticket_app.service;

import org.apache.maven.train_ticket_app.Model.Ticket;
import org.apache.maven.train_ticket_app.Model.TicketRequest;
// Service
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TrainTicketService {

    private final Map<UUID, Ticket> ticketMap = new HashMap<>();

    public Ticket purchaseTicket(TicketRequest ticketRequest) {
        Ticket ticket = new Ticket(ticketRequest);
        ticketMap.put(ticket.getId(), ticket);
        return ticket;
    }

    public Ticket getReceipt(UUID ticketId) {
        return ticketMap.get(ticketId);
    }

    public List<Map<String, Object>> getUsersBySection(String section) {
        List<Map<String, Object>> seatsInSection = new ArrayList<>();
        for (Ticket ticket : ticketMap.values()) {
            if (ticket.getSection().equalsIgnoreCase(section)) {
                Map<String, Object> seatData = new HashMap<>();
                Map<String, Object> userData = new HashMap<>();
                userData.put("firstName", ticket.getUser().getFirstName());
                userData.put("lastName", ticket.getUser().getLastName());
                userData.put("email", ticket.getUser().getEmail());
                seatData.put("user", userData);
                seatData.put("seatNumber", ticket.getSeat());
                seatsInSection.add(seatData);          }
        }
        return seatsInSection;
    }

    public boolean removeUserFromTrain(UUID ticketId) {
        Ticket removedTicket = ticketMap.remove(ticketId);
        return removedTicket != null;
    }

    public Ticket modifyUserSeat(UUID ticketId, String newSeat) {
        System.out.println("newSeat*************8" + newSeat);
        Ticket ticket = ticketMap.get(ticketId);
        if (ticket != null) {
            ticket.setSeat(newSeat);
            return ticket;
        }
        return null;
    }
}