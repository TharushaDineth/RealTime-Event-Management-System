import java.util.ArrayList;
import java.util.List;

public class TicketingSystem {
    private final int maxCapacity;
    private final int totalTickets;
    private final List<Vendor> vendors;
    private final List<Customer> customers;

    public TicketingSystem(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.vendors = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public void addVendor(Vendor vendor) {
        vendors.add(vendor);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
}