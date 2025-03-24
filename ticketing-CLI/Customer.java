import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Customer implements Runnable {
    private final int customerId;
    private final TicketPool ticketPool;
    private final int ticketsRequested;
    private final int retrievalRate;
    private final boolean isVIP;
    private final CountDownLatch vipLatch;

    public Customer(int customerId, TicketPool ticketPool, int ticketsRequested, int retrievalRate, boolean isVIP, CountDownLatch vipLatch) {
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
            while (ticketsRetrieved < ticketsRequested) {
                // If vendors are done and pool is empty, breaking the loop
                if (Main.isVendorsOver.get() && ticketPool.isEmpty()) {
                    break;
                }

                // Try to take a ticket with a timeout
                Ticket ticket = ticketPool.takeTicketWithTimeout(100, TimeUnit.MILLISECONDS);

                // If ticket is null, check if we should exit
                if (ticket == null) {
                    if (Main.isVendorsOver.get() && ticketPool.isEmpty()) {
                        break;
                    }
                    continue;
                }

                // Process the ticket
                System.out.println("Customer ID: " + customerId + " (VIP: " + isVIP + ") retrieved: " + ticket);
                ticketsRetrieved++;
                Thread.sleep(retrievalRate);
            }

            if (isVIP && vipLatch != null) {
                vipLatch.countDown();
                System.out.println("VIP Customer ID: " + customerId + " has completed ticket retrieval.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}