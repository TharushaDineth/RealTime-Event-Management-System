package com.ticketingSystem.backend.logic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Customer implements Runnable {
    private final int customerId;
    private final TicketPool ticketPool;
    private final int ticketsRequested;
    private final int retrievalRate;
    private final boolean isVIP;
    private final CountDownLatch vipLatch;
    private volatile boolean running = true;

    public Customer(int customerId, TicketPool ticketPool, int ticketsRequested,
                    int retrievalRate, boolean isVIP, CountDownLatch vipLatch) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.ticketsRequested = ticketsRequested;
        this.retrievalRate = retrievalRate;
        this.isVIP = isVIP;
        this.vipLatch = vipLatch;
    }

    @Override
    public void run() {
        try {
            int ticketsRetrieved = 0;
            while (running && ticketsRetrieved < ticketsRequested) {
                Ticket ticket = ticketPool.takeTicketWithTimeout(100, TimeUnit.MILLISECONDS);

                if (ticket != null) {
                    System.out.println("Customer " + customerId +
                            " (VIP: " + isVIP + ") retrieved: " + ticket);
                    ticketsRetrieved++;
                    Thread.sleep(retrievalRate);
                }
            }

            if (isVIP && vipLatch != null) {
                vipLatch.countDown();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
    }
}
