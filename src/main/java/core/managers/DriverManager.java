package core.managers;

import api.android.Android;
import core.ADB;
import core.MyLogger;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kolodiy on 9/20/16.
 */
public class DriverManager {


/*    private static DriverService service;*/
    private static AppiumDriverLocalService service;

    private static HashMap<String, URL> hosts;
    private static String unlockPackage = "io.appium.unlock";
    private static String deviceID;

    private static DesiredCapabilities getCaps(String deviceID){
        MyLogger.log.info(" Creating driver capabilities for device: " +deviceID);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "0191bc258f9549ea");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("app", "/Users/kolodiy/appium.android.framework/src/main/resources/speedtest.apk");
        return capabilities;
    }

    private static URL host(String deviceID) throws MalformedURLException {
        if(hosts == null){
            hosts = new HashMap<>();
            hosts.put("0191bc258f9549ea", new URL ("http://127.0.0.1:4723/wd/hub"));
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

    private static AppiumDriverLocalService createService() throws MalformedURLException {
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {

            service = AppiumDriverLocalService.buildDefaultService();
            service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingDriverExecutable(new File("/usr/local/bin/node"))
                    .withAppiumJS(new File("/Applications/Appium.app/Contents/Resources/node_modules/appium/build/lib/main.js"))
                    .withIPAddress(host(deviceID).toString().split(":")[1].replace("//", ""))
                    .usingPort(Integer.parseInt(host(deviceID).toString().split(":")[2].replace("/wd/hub",""))));
/*                    .withIPAddress("127.0.0.1")
                    .usingPort(4723));*/

        }
        else if (osName.contains("Windows")) {
            service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingPort(Integer.parseInt(host(deviceID).toString().split(":")[2].replace("/wd/hub", "")))
                    .withLogFile(new File("target/"+deviceID+".log")));
        }
        else {
            MyLogger.log.info("Unspecified OS found, Appium can't run");
        }

        return service;
    }

/*    private static DriverService createService() throws MalformedURLException {
*//*        service = new AppiumServiceBuilder()
                .usingDriverExecutable(new File("/usr/local/Cellar/node/6.3.1/bin/node"))
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .withIPAddress(host(deviceID).toString().split(":")[1].replace("//", ""))
                .usingPort(Integer.parseInt(host(deviceID).toString().split(":")[2].replace("/wd/hub","")))
                .build();*//*

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingDriverExecutable(new File("/usr/local/bin/node"))
                .withAppiumJS(new File("/Applications/Appium.app/Contents/Resources/node_modules/appium/build/lib/main.js"))
                .withIPAddress("127.0.0.1")
                .usingPort(4723);
*//*                .withLogFile(new File("target/"+deviceID+".log"));*//*

*//*        service = AppiumDriverLocalService.buildDefaultService();*//*
        service = builder.build();
        return service;
    }*/

    public static void createDriver() throws MalformedURLException {
        ArrayList<String> devices = getAvailableDevices();
        for (String device : devices) {
            try {
                deviceID = device;
                MyLogger.log.info("Attempting to create new driver for device: " + device);
                MyLogger.log.info("- - - - - - - - Starting Appium Server- - - - - - - - ");
                createService().start();
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
            service.stop();
        } else MyLogger.log.info("Android driver has not been intialized yet");
    }

}
