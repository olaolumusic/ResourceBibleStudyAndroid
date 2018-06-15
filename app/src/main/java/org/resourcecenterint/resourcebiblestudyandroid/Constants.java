package org.resourcecenterint.resourcebiblestudyandroid;


public class Constants {

    public static final String TAG="chatbubbles";
    public static String SignalRChatUrl = "/signalr";
    public static String BaseUrl = "http://10.0.2.2/yourbaseurl";


    public static final String ISNIGHT = "isNight";

    public static String getSignalRChatUrl() {
        return String.format("%s%s", BaseUrl, SignalRChatUrl);
    }
}
