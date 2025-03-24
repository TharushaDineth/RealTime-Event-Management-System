package com.ticketingSystem.backend.logic;

import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {
    private static final AtomicInteger globalTicketCounter = new AtomicInteger(0);
    private final int ticketId;

    public Ticket() {
        this.ticketId = globalTicketCounter.incrementAndGet();
    }

    public int getTicketId() {
        return ticketId;
    }

    @Override
    public String toString() {
        return "Ticket{ticketId=" + ticketId + '}';
    }
}