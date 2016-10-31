import core.MyLogger;
import core.managers.DriverManager;
import org.apache.log4j.Level;
import org.junit.runner.JUnitCore;
import tests.TestPrimer;

import java.net.MalformedURLException;

/**
 * Created by kolodiy on 9/20/16.
 */
public class Runner {

    public static void main(String[] args) throws MalformedURLException {
        MyLogger.log.setLevel(Level.DEBUG);


        try{
            DriverManager.createDriver();
            JUnitCore.runClasses(TestPrimer.class);

        }
        finally {
            DriverManager.killDriver();
        }

    }

}
