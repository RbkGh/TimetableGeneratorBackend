package com.swiftpot.timetable.model;

import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 2:05 PM
 */
public class SuccessfulOutgoingPayload extends OutgoingPayload {

    public SuccessfulOutgoingPayload(Object responseObject) {
        super("Success", 00, responseObject);
    }

    public SuccessfulOutgoingPayload(String message,Object responseObject) {
        super(message, 00, responseObject);
    }

}
