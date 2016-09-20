package core;

/**
 * Created by kolodiy on 9/20/16.
 */
public class UiSelecor {

    private String locator = "new UiSelector()";

    public UiSelecor resourceId(String value){
        locator += ".resource(\""+value+"\")";
    return this;
    }

    public UiSelecor className(String value){
        locator += ".className(\""+value+"\")";
    return this;
    }

    public UiSelecor classNameMatches(String regex){
        locator += ".classNameMatches(\""+regex+"\")";
        return this;
    }

    public UiSelecor text(String value){
        locator += ".text(\""+value+"\")";
        return this;
    }
    public UiSelecor textContains(String value){
        locator += ".textContains(\""+value+"\")";
        return this;
    }

    public UiSelecor index(int value){
        locator += ".index("+value+")";
        return this;
    }

    public UiSelecor clickable(boolean value){
        locator += ".clickable("+value+")";
        return this;
    }

    public UiSelecor checked(boolean value){
        locator += ".checked("+value+")";
        return this;
    }

    public UiSelecor enabled(boolean value){
        locator += ".enabled("+value+")";
        return this;
    }

    public UiSelecor description(String value){
        locator += ".description(\""+value+"\")";
        return this;
    }

    public UiSelecor descriptionContains(String value){
        locator += ".descriptionContains(\""+value+"\")";
        return this;
    }

    public UiSelecor descriptionMatches(String regex){
        locator += ".descriptionMatches(\""+regex+"\")";
        return this;
    }

    public UiSelecor xPath(String xPath){
        locator = xPath;
        return this;
    }public UiObject makeUiObject(){
        return  new UiObject(locator);
    }



}
