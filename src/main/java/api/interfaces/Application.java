package api.interfaces;

/**
 * Created by kolodiy on 10/12/16.
 */
public interface Application {

    void forceStop();
    void clearData();
    Object open();
    String packageID();
    String activityID();


}
