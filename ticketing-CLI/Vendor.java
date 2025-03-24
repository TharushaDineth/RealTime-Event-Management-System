import java.util.concurrent.Semaphore;

public class Vendor implements Runnable {
    private static final Semaphore vendorSemaphore = new Semaphore(1);
    private final int vendorId;
    private final TicketPool ticketPool;
    private final int ticketsToAdd;
    private final int releaseRate;

    public Vendor(int vendorId, TicketPool ticketPool, int ticketsToAdd, int releaseRate) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.ticketsToAdd = ticketsToAdd;
        this.releaseRate = releaseRate;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < ticketsToAdd; i++) {
                // Acquire the semaphore to ensure synchronized ticket addition
                vendorSemaphore.acquire();
                try {
                    Ticket ticket = new Ticket();
                    synchronized (ticketPool) {
                        ticketPool.addTicket(ticket);
                    }
                    System.out.println("Vendor ID: " + vendorId + " added: " + ticket);
                } finally {
                    // Always release the semaphore
                    vendorSemaphore.release();
                }
                Thread.sleep(releaseRate);
            }
            System.out.println("Vendor ID: " + vendorId + " has finished adding tickets.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}