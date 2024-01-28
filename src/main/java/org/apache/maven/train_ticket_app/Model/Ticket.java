package org.apache.maven.train_ticket_app.Model;
import java.util.UUID;
// Model
public class Ticket {
    private final UUID id;
    private final String from;
    private final String to;
    private final User user;
    private final double price;
    private String seat;
    private String section;

    public Ticket(TicketRequest ticketRequest) {
        this.id = UUID.randomUUID();
        this.from = ticketRequest.getFrom();
        this.to = ticketRequest.getTo();
        this.user = ticketRequest.getUser();
        this.price = 20; 
        this.section = ticketRequest.getSection();
        if (section.equalsIgnoreCase("A")) {
            this.seat = "A" + (char)('A' + (int)(Math.random() * 4)) + (int)(Math.random() * 10);
        } else {
            this.seat = "B" + (char)('A' + (int)(Math.random() * 4)) + (int)(Math.random() * 10);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public User getUser() {
        return user;
    }

    public double getPrice() {
        return price;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getSection() {
        return section;
    }
}