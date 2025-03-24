package com.ticketingSystem.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_configuration")
public class ConfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for auto-incrementing
    private Long id;

    @Column(name = "number_of_vendors")
    private int numberOfVendors;

    @Column(name = "total_tickets")
    private int totalTickets;

    @Column(name = "ticket_release_rate")
    private int ticketReleaseRate;

    @Column(name = "number_of_customers")
    private int numberOfCustomers;

    @Column(name = "customer_retrieval_rate")
    private int customerRetrievalRate;

    @Column(name = "max_ticket_capacity")
    private int maxTicketCapacity;

    @Column(name = "number_of_vip_customers")
    private int numberOfVIPCustomers;

    @Column(name = "tickets_per_vendor")
    private int ticketsPerVendor;

    @Column(name = "tickets_per_customer")
    private int ticketsPerCustomer;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfVendors() {
        return numberOfVendors;
    }

    public void setNumberOfVendors(int numberOfVendors) {
        this.numberOfVendors = numberOfVendors;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getNumberOfVIPCustomers() {
        return numberOfVIPCustomers;
    }

    public void setNumberOfVIPCustomers(int numberOfVIPCustomers) {
        this.numberOfVIPCustomers = numberOfVIPCustomers;
    }

    public int getTicketsPerVendor() {
        return ticketsPerVendor;
    }

    public void setTicketsPerVendor(int ticketsPerVendor) {
        this.ticketsPerVendor = ticketsPerVendor;
    }

    public int getTicketsPerCustomer() {
        return ticketsPerCustomer;
    }

    public void setTicketsPerCustomer(int ticketsPerCustomer) {
        this.ticketsPerCustomer = ticketsPerCustomer;
    }
}
