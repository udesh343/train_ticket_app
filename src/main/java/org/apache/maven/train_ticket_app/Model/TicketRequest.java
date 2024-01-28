package org.apache.maven.train_ticket_app.Model;
// Request Model
public class TicketRequest {
    private final String from = "London";
    private final String to = "France";
    private String section;
    private User user;

    // Constructor
    public TicketRequest(String section, User user) {
        this.section = section;
        this.user = user;
    }

    public String getFrom() {
        return from;
    }

    // public void setFrom(String from) {
    //     this.from = from;
    // }

    public String getTo() {
        return to;
    }

    // public void setTo(String to) {
    //     this.to = to;
    // }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}