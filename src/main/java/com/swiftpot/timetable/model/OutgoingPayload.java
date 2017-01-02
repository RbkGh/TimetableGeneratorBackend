package com.swiftpot.timetable.model;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 1:49 PM
 */
public class OutgoingPayload {

    private String message;

    private int status;

    private Object responseObject;

    public OutgoingPayload(String message, int status, Object responseObject) {
        this.message = message;
        this.status = status;
        this.responseObject = responseObject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }
}
