package core;

import java.util.Date;

/**
 * Created by kolodiy on 9/21/16.
 */
public class Timer {

    public  long startStamp;

    public void startTimer(){
        startStamp = getTimeStamp();
    }

    public static long getTimeStamp(){
        return new Date().getTime();
    }

    public boolean expired(int seconds){
        int difference = (int) ((getTimeStamp() - startStamp)/1000);
        return difference > seconds;
    }
}
