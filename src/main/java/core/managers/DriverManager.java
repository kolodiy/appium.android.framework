package core.managers;

import api.android.Android;
import core.ADB;
import core.MyLogger;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kolodiy on 9/20/16.
 */
public class DriverManager {

    private static HashMap<String, URL> hosts;
    private static String unlockPackage = "io.appium.unlock";

    private static DesiredCapabilities getCaps(String deviceID){
        MyLogger.log.info(" Creating driver capabilities for device: " +deviceID);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "TA44904UV0");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("app", "/Users/kolodiy/appium.android.framework/src/main/resources/speedtest.apk");
        return capabilities;
    }

    private static URL host(String deviceID) throws MalformedURLException {
        if(hosts == null){
            hosts = new HashMap<>();
            hosts.put("TA44904UV0", new URL ("http://127.0.0.1:4723/wd/hub"));
        }
        return  hosts.get(deviceID);
    }

    private static ArrayList <String> getAvailableDevices() {
        MyLogger.log.info("Checking for available device:");
        ArrayList<String> availableDevices = new ArrayList<>();
        ArrayList connectedDevices = ADB.getConnectedDevices();
        for (Object connectedDevice : connectedDevices) {
            String device = connectedDevice.toString();
            ArrayList apps = new ADB(device).getInstalledPackages();
            if (!apps.contains(unlockPackage)) availableDevices.add(device);
            else
                MyLogger.log.info("Device: " + device + " has " + unlockPackage + "installed, it is already under testing");
        }
        if (availableDevices.size() == 0) throw new RuntimeException("No device is available for testing");
        return availableDevices;
    }

    public static void createDriver() throws MalformedURLException {
        ArrayList<String> devices = getAvailableDevices();
        for (String device : devices) {
            try {
                MyLogger.log.info("Attempting to create new driver for device: " + device);
                Android.driver = new AndroidDriver(host(device), getCaps(device));
                Android.adb = new ADB(device);
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void killDriver() {
        if (Android.driver != null) {
            MyLogger.log.info("Killing the Android Driver instance");
            Android.driver.quit();
            Android.adb.uninstallApp(unlockPackage);
        } else MyLogger.log.info("Android driver has not been intialized yet");
    }

}
