package com.ticketingSystem.backend.logic;

import com.ticketingSystem.backend.model.ConfigurationEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TicketingSystem {
    private static final AtomicBoolean isSimulationRunning = new AtomicBoolean(false);
    private static ExecutorService executorService;
    private final TicketPool ticketPool;
    private final List<Vendor> vendors = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();

    public TicketingSystem(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public void startSystem(ConfigurationEntity config) {
        if (!isSimulationRunning.compareAndSet(false, true)) {
            throw new IllegalStateException("System is already running!");
        }

        ticketPool.setCapacity(config.getMaxTicketCapacity());

        executorService = Executors.newFixedThreadPool(
                config.getNumberOfVendors() + config.getNumberOfCustomers()
        );

        CountDownLatch vipLatch = new CountDownLatch(config.getNumberOfVIPCustomers());

        // Start vendors
        for (int i = 1; i <= config.getNumberOfVendors(); i++) {
            Vendor vendor = new Vendor(i, ticketPool,
                    config.getTicketsPerVendor(), config.getTicketReleaseRate());
            vendors.add(vendor);
            executorService.submit(vendor);
        }

        // Start VIP customers
        for (int i = 1; i <= config.getNumberOfVIPCustomers(); i++) {
            Customer customer = new Customer(i, ticketPool,
                    config.getTicketsPerCustomer(), config.getCustomerRetrievalRate(),
                    true, vipLatch);
            customers.add(customer);
            executorService.submit(customer);
        }

        // Start regular customers
        for (int i = config.getNumberOfVIPCustomers() + 1;
             i <= config.getNumberOfCustomers(); i++) {
            Customer customer = new Customer(i, ticketPool,
                    config.getTicketsPerCustomer(), config.getCustomerRetrievalRate(),
                    false, null);
            customers.add(customer);
            executorService.submit(customer);
        }
    }

    public static void stopSimulation() {
        if (isSimulationRunning.compareAndSet(true, false)) {
            if (executorService != null) {
                executorService.shutdownNow();
            }
        }
    }

    public static boolean isSimulationRunning() {
        return isSimulationRunning.get();
    }
}