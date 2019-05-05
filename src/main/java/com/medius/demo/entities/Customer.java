package com.medius.demo.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String telephoneNumber;
    private String email;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "customer_meeting",
    joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "meeting_id", referencedColumnName = "meetingId"))
    private Set<Meeting> meetings;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String telephoneNumber, String email, Meeting... meetings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.meetings = Stream.of(meetings).collect(Collectors.toSet());
        this.meetings.forEach(x -> x.getCustomers().add(this));
    }


    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%d, firstName='%s', lastName='%s', telephoneNumber='%s', email='%s']", id,
                firstName, lastName, telephoneNumber, email);
    }

}
