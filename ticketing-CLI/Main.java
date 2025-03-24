import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static final AtomicBoolean isVendorsOver = new AtomicBoolean(false);
    public static final AtomicBoolean isSimulationOver = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        // Ask if the user wants to load customer data from a file
        System.out.print("Do you want to load customer data from a file? (yes/no): ");
        String loadFromFile = scanner.next();

        // Initialize variables
        int maxCapacity, numVendors, numCustomers, numVIPCustomers, ticketsPerVendor;
        int vendorReleaseRate, customerRetrievalRate, ticketsPerCustomer;
        TicketPool ticketPool;
        TicketingSystem ticketingSystem;

        // If the user wants to load customer data from a file
        if (loadFromFile.equalsIgnoreCase("yes")) {
            String filePath = "ticketing_system_details.txt";
            displayCustomerDataFromFile(filePath);
        }

        // Get inputs from the user
        System.out.print("Enter the maximum capacity of the ticket pool: ");
        maxCapacity = scanner.nextInt();

        System.out.print("Enter the number of vendors: ");
        numVendors = scanner.nextInt();

        System.out.print("Enter the total number of customers: ");
        numCustomers = scanner.nextInt();

        System.out.print("Enter the number of VIP customers: ");
        numVIPCustomers = scanner.nextInt();

        System.out.print("Enter the total number of tickets each vendor can sell: ");
        ticketsPerVendor = scanner.nextInt();

        System.out.print("Enter the ticket release rate (in milliseconds) for vendors: ");
        vendorReleaseRate = scanner.nextInt();

        System.out.print("Enter the ticket retrieval rate (in milliseconds) for customers: ");
        customerRetrievalRate = scanner.nextInt();

        System.out.print("Enter the number of tickets each customer wants to buy: ");
        ticketsPerCustomer = scanner.nextInt();

        // Initialize the ticket pool and ticketing system
        ticketPool = new TicketPool(maxCapacity);
        ticketingSystem = new TicketingSystem(maxCapacity, numVendors * ticketsPerVendor);

        BufferedWriter writer = new BufferedWriter(new FileWriter("ticketing_system_details.txt", true));

        // Writing configuration details to the file
        writer.write("Configuration Details:\n");
        writer.write("Maximum Capacity of Ticket Pool: " + maxCapacity + "\n");
        writer.write("Number of Vendors: " + numVendors + "\n");
        writer.write("Total Number of Customers: " + numCustomers + "\n");
        writer.write("Number of VIP Customers: " + numVIPCustomers + "\n");
        writer.write("Tickets per Vendor: " + ticketsPerVendor + "\n");
        writer.write("Ticket Release Rate (ms) for Vendors: " + vendorReleaseRate + "\n");
        writer.write("Ticket Retrieval Rate (ms) for Customers: " + customerRetrievalRate + "\n");
        writer.write("Tickets per Customer: " + ticketsPerCustomer + "\n");
        writer.write("\n");

        // VIP customers latch
        CountDownLatch vipLatch = new CountDownLatch(numVIPCustomers);

        // Start vendor threads
        List<Thread> vendorThreads = new ArrayList<>();
        for (int i = 0; i < numVendors; i++) {
            Vendor vendor = new Vendor(i+1, ticketPool, ticketsPerVendor, vendorReleaseRate);
            Thread vendorThread = new Thread(vendor);
            ticketingSystem.addVendor(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Create and start VIP customer threads
        List<Thread> customerThreads = new ArrayList<>();
        writer.write("\nVIP Customer Details:\n");
        for (int i = 0; i < numVIPCustomers; i++) {
            Customer vipCustomer = new Customer(i+1, ticketPool, ticketsPerCustomer, customerRetrievalRate, true, vipLatch);
            ticketingSystem.addCustomer(vipCustomer);
            Thread vipCustomerThread = new Thread(vipCustomer);
            customerThreads.add(vipCustomerThread);
            writer.write("Customer ID: " + i + ", Tickets Requested: " + ticketsPerCustomer + ", VIP: Yes\n");
            vipCustomerThread.start();
        }

        // Wait for all VIP customers to finish
        vipLatch.await();

        // Create and start regular customer threads
        writer.write("\nRegular Customer Details:\n");
        for (int i = numVIPCustomers; i < numCustomers; i++) {
            Customer regularCustomer = new Customer(i+1, ticketPool, ticketsPerCustomer, customerRetrievalRate, false, null);
            ticketingSystem.addCustomer(regularCustomer);
            Thread regularCustomerThread = new Thread(regularCustomer);
            customerThreads.add(regularCustomerThread);
            writer.write("Customer ID: " + i + ", Tickets Requested: " + ticketsPerCustomer + ", VIP: No\n");
            regularCustomerThread.start();
        }

        // Wait for all vendors to finish adding tickets
        for (Thread vendorThread : vendorThreads) {
            vendorThread.join();
        }

        // Signal that vendors are done adding tickets
        isVendorsOver.set(true);

        // Wait for all customer threads to finish
        for (Thread customerThread : customerThreads) {
            customerThread.join();
        }

        // Signal that simulation is over
        isSimulationOver.set(true);

        System.out.println("Ticketing System has been successfully executed.");

        // Finalize and close the file writer
        writer.write("\nTicket Pool and System Information:\n");
        writer.write("Total Capacity: " + maxCapacity + ", Total Vendors: " + numVendors + ", Total Customers: " + numCustomers + "\n");
        writer.write("Ticketing System has been successfully executed.\n\n");
        writer.close();

        // Close scanner
        scanner.close();
    }

    private static void displayCustomerDataFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}