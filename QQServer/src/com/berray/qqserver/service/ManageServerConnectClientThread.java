package com.berray.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ManageServerConnectClientThread {
    private static HashMap<String,ServerConnectClinetThread> hashmap = new HashMap<>();

    public static HashMap<String, ServerConnectClinetThread> getHashmap() {
        return hashmap;
    }

    public static void addServerConnectClientThread(String userID, ServerConnectClinetThread serverConnectClinetThread){
        hashmap.put(userID,serverConnectClinetThread);
    }
    public static ServerConnectClinetThread getServerConnectClinetThread(String userID){
        return hashmap.get(userID);
    }
    public static String getonlineUser(){
        Iterator<String> iterator = hashmap.keySet().iterator();
        String onlineUsersList = "";
        while(iterator.hasNext()){
            onlineUsersList += iterator.next().toString() + " ";
        }
        return onlineUsersList;
    }
    public static void deleteServerConnectClinetThread(String userID){
        hashmap.remove(userID);
    }
}
