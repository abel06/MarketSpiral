package com.example.abela.marketspiral.interfaces;

/**
 * Created by HaZe on 5/2/17.
 * Those methods have to be implemented by an activity, the implementation have to define how should act in case of specific cases
 */
public interface RemoteResponse {


    /**This method is called once the login have been performed, it returns the current user id (if registered)*/
    void loginFinished(int id, Object result);

    /**This method is called after the user registration, it returns 0 if everything went right , 1 if inserted data is already registered, -1 if there are errors*/
    void registerFinished(int value,boolean externalService);

    /**  --- This method is called when the item is responded from server  ---    */
    void searchFinished(int value, Object result);

    /**  --- This method is called when we geocode from lat and lng and when it finished geocoding ---    */
    void geocodeFinished(int id,Object result);

    /**This method is called when a new item have been added into the DB, if no error arises the id has the item id, -1 otherwise*/
    void addItem(int id);

    /**This method is called when a new item have been added into the DB, if no error arises the id has the item id, -1 otherwise*/
    void itemAdded(int id, Object result);

    /**This method is called when an item have been removed into the DB, if no error arises the id has the item id, -1 otherwise*/
    void itemRemoved(int id);

    /**  --- This method is called to start the method search in RemotTask class ---    */
    void searchItem(int id);

    void profileFinished(int responce);

    void imageUploaded(int value);

    void myItems(Integer integer, Object result);
}
