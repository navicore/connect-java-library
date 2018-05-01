package com.urbanairship.connect.client;

final class Constants {

    private static String UA_URL = System.getenv("UA_URL");
    static {
        if (UA_URL == null)  {
            UA_URL = "https://connect.urbanairship.com/api/events/";
            System.out.println("using URL " + UA_URL);
        }
    }
    static final String API_URL = UA_URL;
    private Constants() { }

}
