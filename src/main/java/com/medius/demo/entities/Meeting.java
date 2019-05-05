package com.medius.demo.entities;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Meeting {
    @Id
    @GeneratedValue
    private Long meetingId;
    private String location;
    private MeetingResult meetingResult;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;

    @ManyToMany(mappedBy = "meetings")
    private Set<Customer> customers = new HashSet<>();

    protected Meeting() {

    }

    public Meeting(String location, MeetingResult meetingResult, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        this.location = location;
        this.meetingResult = meetingResult;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    public Long getId() {
        return meetingId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public MeetingResult getMeetingResult() {
        return meetingResult;
    }

    public void setMeetingResult(MeetingResult meetingResult) {
        this.meetingResult = meetingResult;
    }

    @Override
    public String toString() {
        return String.format("Meeting[id=%d, location='%s', startDate='%s', startTime='%s', endDate='%s', endTime='%s']", meetingId,
                location, startDate, startTime, endDate, endTime);
    }
}
