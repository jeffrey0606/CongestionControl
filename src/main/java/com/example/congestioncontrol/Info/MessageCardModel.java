package com.example.congestioncontrol.Info;

public class MessageCardModel extends InfoModel {
    public String hostName;
    public String hostAddress;
    public String time;
    public String message;
    public boolean isFeedback;
    public MessageCardModel(String hostName, String hostAddress, String time, String message, boolean isFeedback) {
        this.hostName = hostName;
        this.message = message;
        this.hostAddress = hostAddress;
        this.time = time;
        this.isFeedback = isFeedback;
    }

    public MessageCardModel(String str) {
        String splitStr [] = str.split("\\|");
        System.out.println(splitStr.length);
        for (int i = 0; i < splitStr.length; i++) {
            if( i == 0 ) {
                String m1 [] = splitStr[i].split("~");
                System.out.println(m1.length);
                hostName = m1[0];
                hostAddress = m1[1];
            } else if ( i == 1) {
                time = splitStr[i];
            } else if (i == 2) {
                message = splitStr[i];

                isFeedback = message.toLowerCase().contains("feedback");
            }
        }
    }
}
