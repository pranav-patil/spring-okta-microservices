package com.emprovise.service.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;

public class LoggedInUserDetails extends User {

    private static final long serialVersionUID = 3218489133299330245L;
    private String firstName;
    private String lastName;
    private String accountNumber;
    private String address;
    private String country;
    private String email;
    private String phone;

    public LoggedInUserDetails(String username, String password,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "LoggedInUserDetails{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoggedInUserDetails)) return false;
        if (!super.equals(o)) return false;
        LoggedInUserDetails that = (LoggedInUserDetails) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(accountNumber, that.accountNumber) &&
                Objects.equals(address, that.address) &&
                Objects.equals(country, that.country) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, accountNumber, address, country, email, phone);
    }
}
