package com.example.abela.marketspiral.interfaces;

/**
 * Created by HaZe on 5/2/17.
 * Those methods have to be implemented by an activity, the implementation have to define how should act in case of specific cases
 */
public interface RemoteResponse {

    void loginFinished(int value);
    void registerFinished(int value);
    void searchFinished(int value, Object result);
    void geocodeFinished(int id,Object result);
    void itemAdded(int id);
    void itemRemoved(int id);
    void searchItem(int id);


}
