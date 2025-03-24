package com.ticketingSystem.backend.logic;

import java.util.concurrent.Semaphore;

public class Vendor implements Runnable {
    private static final Semaphore vendorSemaphore = new Semaphore(1);
    private final int vendorId;
    private final TicketPool ticketPool;
    private final int ticketsToAdd;
    private final int releaseRate;
    private volatile boolean running = true;

    public Vendor(int vendorId, TicketPool ticketPool, int ticketsToAdd, int releaseRate) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.ticketsToAdd = ticketsToAdd;
        this.releaseRate = releaseRate;
    }

    @Override
    public void run() {
        try {
            int addedTickets = 0;
            while (running && addedTickets < ticketsToAdd) {
                vendorSemaphore.acquire();
                try {
                    if (!running) break;

                    Ticket ticket = new Ticket();
                    ticketPool.addTicket(ticket);
                    addedTickets++;
                    System.out.println("Vendor " + vendorId + " added: " + ticket);
                } finally {
                    vendorSemaphore.release();
                }
                Thread.sleep(releaseRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
    }
}
