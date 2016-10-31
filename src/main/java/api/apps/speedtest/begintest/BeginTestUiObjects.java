package api.apps.speedtest.begintest;

import core.UiObject;
import core.UiSelector;

/**
 * Created by kolodiy on 10/13/16.
 */
public class BeginTestUiObjects {

    private static UiObject beginTestButton;

    public UiObject beginTestButton(){
        if(beginTestButton == null) beginTestButton = new UiSelector().text("Begin Test").makeUiObject();
        return beginTestButton;
    }
}
