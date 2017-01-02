package com.swiftpot.timetable.model;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 2:08 PM
 */
public class ErrorOutgoingPayload extends OutgoingPayload {

    public ErrorOutgoingPayload(Object responseObject) {
        super("Error", 11, responseObject);
    }

    public ErrorOutgoingPayload() {
        super("Error", 11, null);
    }

    public ErrorOutgoingPayload(String message) {
        super(message, 11, null);
    }
}
