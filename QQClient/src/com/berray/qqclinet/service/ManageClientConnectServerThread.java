package com.berray.qqclinet.service;

import java.util.HashMap;
//用來保存線程的集合
public class ManageClientConnectServerThread {
    static HashMap<String,ClientConnectServerThread> hashMap = new HashMap<>();
    //新增線程到集合的方法
    public static void addClientConnectServer(String userID,ClientConnectServerThread clientConnectServerThread){
        hashMap.put(userID,clientConnectServerThread);
    }
    //獲取集合中的線程
    public static ClientConnectServerThread getClientConnectServerThread(String userID){
        return hashMap.get(userID);
    }
    public static void deleteServerConnectClinetThread(String userID){
        hashMap.remove(userID);
    }
}
