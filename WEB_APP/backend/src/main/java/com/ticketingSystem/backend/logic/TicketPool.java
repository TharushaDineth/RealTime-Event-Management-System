package com.ticketingSystem.backend.logic;

import org.springframework.stereotype.Component;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

@Component
public class TicketPool {
    private final Queue<Ticket> ticketQueue;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    private int capacity;

    public TicketPool() {
        this.ticketQueue = new LinkedList<>();
        this.lock = new ReentrantLock(true);
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addTicket(Ticket ticket) throws InterruptedException {
        lock.lock();
        try {
            while (ticketQueue.size() >= capacity) {
                notFull.await();
            }
            ticketQueue.add(ticket);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Ticket takeTicketWithTimeout(long timeout, TimeUnit unit) throws InterruptedException {
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            while (ticketQueue.isEmpty()) {
                if (nanos <= 0) return null;
                nanos = notEmpty.awaitNanos(nanos);
            }
            Ticket ticket = ticketQueue.poll();
            notFull.signalAll();
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