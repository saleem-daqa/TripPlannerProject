package com.example.tripplanningmobileapplication;

public class Trip {
    private String destination;
    private String startDate;
    private String endDate;
    private String budget;
    private String notes;
    private boolean completed;
    private String transport;
    private String payment;

    public Trip(String destination, String startDate, String endDate, String budget, String notes, boolean completed, String transport, String payment) {

        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.notes = notes;
        this.completed = completed;
        this.transport = transport;
        this.payment = payment;
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}