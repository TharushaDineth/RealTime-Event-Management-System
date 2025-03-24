import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class TicketPool {
    private final int capacity;
    private final Queue<Ticket> ticketQueue;
    private final ReentrantLock lock; // Fair lock for strict FIFO
    private final Condition notEmpty; // Condition for waiting when the pool is empty
    private final Condition notFull;  // Condition for waiting when the pool is full

    public TicketPool(int capacity) {
        this.capacity = capacity;
        this.ticketQueue = new LinkedList<>();
        this.lock = new ReentrantLock(true); // Fair lock ensures FIFO access to threads
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }

    public void addTicket(Ticket ticket) throws InterruptedException {
        lock.lock();
        try {
            // Wait if the pool is full
            while (ticketQueue.size() >= capacity) {
                notFull.await();
            }
            ticketQueue.add(ticket); // Add ticket to the queue
            notEmpty.signalAll(); // Notify threads waiting to retrieve
        } finally {
            lock.unlock();
        }
    }

    public Ticket takeTicket() throws InterruptedException {
        lock.lock();
        try {
            // Wait if the pool is empty
            while (ticketQueue.isEmpty()) {
                notEmpty.await();
            }
            Ticket ticket = ticketQueue.poll(); // Retrieve the ticket in FIFO order
            notFull.signalAll(); // Notify threads waiting to add
            return ticket;
        } finally {
            lock.unlock();
        }
    }

    public Ticket takeTicketWithTimeout(long timeout, TimeUnit unit) throws InterruptedException {
        lock.lock();
        try {
            // Wait if the pool is empty, with a timeout
            long nanos = unit.toNanos(timeout);
            while (ticketQueue.isEmpty()) {
                if (nanos <= 0) {
                    return null;
                }
                nanos = notEmpty.awaitNanos(nanos);
            }
            Ticket ticket = ticketQueue.poll(); // Retrieve the ticket in FIFO order
            notFull.signalAll(); // Notify threads waiting to add
            return ticket;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return ticketQueue.isEmpty();
        } finally {
            lock.unlock();
        }
    }
}