package com.swiftpot.timetable.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         29-Dec-16 @ 7:12 PM
 */
public final class PrettyJSON {

    /**
     * Convert a JSON string to pretty print version
     *
     * @param jsonString
     * @return
     */
    public static String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

    /**
     * pass in a list to be converted to a pretty format
     *
     * @param listOfObjects
     * @return
     */
    public static String toListPrettyFormat(List listOfObjects) {

        Gson objGson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = objGson.toJson(listOfObjects);


        return prettyJson;
    }
}
