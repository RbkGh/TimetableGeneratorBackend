/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services.servicemodels;

/**
 * THis will hold instances of periods to be allocated for each programmeDay eg Monday may have<br>
 * 2,3,3,2 as the periods for the day,thus there will be four instances of this model object.<br>THe
 * first object will have {@link PeriodSetForProgrammeDay#totalNumberOfPeriodsForSet} = 2 and the <br>
 * {@link PeriodSetForProgrammeDay#periodStartingNumber} will be 1 and the {@link PeriodSetForProgrammeDay#periodEndingNumber} will be 2
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         10-Mar-17 @ 12:45 PM
 */
public class PeriodSetForProgrammeDay extends PeriodSet {
}
