/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services.servicemodels;

/**
 * @author Ace Programmer Rbk
 * <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 * 06-Mar-17 @ 11:12 PM
 */

import com.swiftpot.timetable.model.PeriodOrLecture;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * this is a base class that will be extended by both {@link AllocatedPeriodSet} and {@link UnallocatedPeriodSet} and others <br>
 * thus class will return a period set that is unallocated or allocated depending on the class extending this
 * for eg. period 3&4 may be unallocated whilst all other periods are allocated in a day.
 */
public abstract class PeriodSet {
    /**
     * the period where the unallocated period starts ie {@link PeriodOrLecture#periodNumber},not the index of the period!!Nnote that!!
     * the period may start in periodNumber 3 and end in period number 4,meaning that {@link UnallocatedPeriodSet#totalNumberOfPeriodsForSet} will be 2
     */
    private int periodStartingNumber;
    /**
     * the period where the unallocated period ends ie {@link PeriodOrLecture#periodNumber},not the index of the period!!Nnote that!!
     * the period may end in periodNumber 5 if it started in period 3.
     */
    private int periodEndingNumber;
    /**
     * the total number of periods for this set,if the {@linkplain UnallocatedPeriodSet#periodStartingNumber} = 3 and <br>
     * {@linkplain UnallocatedPeriodSet#periodEndingNumber} =4,automatically,{@linkplain UnallocatedPeriodSet#totalNumberOfPeriodsForSet} =2periods
     *
     * @see UnallocatedPeriodSet#periodStartingNumber
     */
    private int totalNumberOfPeriodsForSet;

    public int getPeriodStartingNumber() {
        return periodStartingNumber;
    }

    /**
     * @return int
     * @see UnallocatedPeriodSet#periodStartingNumber
     */
    public void setPeriodStartingNumber(int periodStartingNumber) {
        this.periodStartingNumber = periodStartingNumber;
    }

    /**
     * @return int
     * @see UnallocatedPeriodSet#periodEndingNumber
     */
    public int getPeriodEndingNumber() {
        return periodEndingNumber;
    }

    public void setPeriodEndingNumber(int periodEndingNumber) {
        this.periodEndingNumber = periodEndingNumber;
    }

    /**
     * @return int
     * @see UnallocatedPeriodSet#totalNumberOfPeriodsForSet
     */
    public int getTotalNumberOfPeriodsForSet() {
        return totalNumberOfPeriodsForSet;
    }

    public void setTotalNumberOfPeriodsForSet(int totalNumberOfPeriodsForSet) {
        this.totalNumberOfPeriodsForSet = totalNumberOfPeriodsForSet;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof PeriodSet)) {
            return false;
        }

        PeriodSet periodSet = (PeriodSet) o;

        return new EqualsBuilder()
                .append(periodStartingNumber, periodSet.periodStartingNumber)
                .append(periodEndingNumber, periodSet.periodEndingNumber)
                .append(totalNumberOfPeriodsForSet, periodSet.totalNumberOfPeriodsForSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(periodStartingNumber)
                .append(periodEndingNumber)
                .append(totalNumberOfPeriodsForSet)
                .toHashCode();
    }

}
