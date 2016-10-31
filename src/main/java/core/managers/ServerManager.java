package core.managers;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by kolodiy on 9/21/16.
 */
public class ServerManager {

    private static String OS;

    private static String ANDROID_HOME;

    public static String getAndroidHome(){
        if(ANDROID_HOME == null){
            ANDROID_HOME = System.getenv("ANDROID_HOME");
            if (ANDROID_HOME == null) throw new RuntimeException("Failed to find ANDROID_HOME, make sure the variable is set corectly");
        }
        return ANDROID_HOME;
    }

    public static String getOS() {
        if (OS==null) OS = System.getenv("os.name");
        return OS;
    }

    public  static boolean isMac(){
        return getOS().startsWith("Mac");
    }

    public  static boolean isWindows(){
        return getOS().startsWith("Windows");
    }

    public static  String runCommand(String command){
        String output = null;
        try {
            Scanner scanner = null;
            try {
                Process pb = Runtime.getRuntime().exec(command);
                pb.waitFor();
                scanner = new Scanner(pb.getInputStream()).useDelimiter("\\A");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (scanner.hasNext()) output = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

}
