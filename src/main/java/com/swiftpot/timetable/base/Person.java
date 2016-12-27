package com.swiftpot.timetable.base;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 6:48 PM
 */
public class Person {

    public String firstName;

    public String surName;

    public String otherNames;

    public String phoneNumber;

    public String emailAddress;

    public Person() {
    }



    public String getFirstName() {
        return firstName;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getSurName() {
        return surName;
    }

    public Person setSurName(String surName) {
        this.surName = surName;
        return this;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public Person setOtherNames(String otherNames) {
        this.otherNames = otherNames;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Person setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Person setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }
}
