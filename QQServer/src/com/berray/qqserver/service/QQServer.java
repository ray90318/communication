package com.berray.qqserver.service;

import com.berray.qqcommon.Message;
import com.berray.qqcommon.MessageType;
import com.berray.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class QQServer {
    private ServerSocket serverSocket;
    User user;
    private static HashMap<String,User> validUsers = new HashMap<>();
    static{
        validUsers.put("berray",new User("berray","111"));
        validUsers.put("br",new User("br","111"));
        validUsers.put("ray",new User("ray","111"));
        validUsers.put("pupu",new User("pupu","111"));
        validUsers.put("aji",new User("aji","111"));
    }
    public boolean checkUser(String userID,String passwd){
        User user = validUsers.get((userID));
        //判斷帳號是否為空
        if(user.getUserID() == null){
            return false;
        }
        //判斷密碼是否符合且取反
        if(!user.getPasswd().equals(passwd)){
            return false;
        }
        return true;
    }
    public QQServer(){
        try {
            serverSocket = new ServerSocket(9999);
            Thread thread = new Thread(new ServerToAll());
            thread.start();
            while(true) {
                System.out.println("服務端在9999監聽...");
                Socket socket = serverSocket.accept();//當和某個客戶連接後會繼續監聽 所以用while
                //得到socket關聯對象的輸入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //得到socket關聯對象的輸入流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User)ois.readObject();
                Message message = new Message();
                if(checkUser(u.getUserID(),u.getPasswd())){//登入成功
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCESSEDD);
                    oos.writeObject(message);
                    //創建線程來不斷收取訊息
                    ServerConnectClinetThread serverConnectClinetThread = new ServerConnectClinetThread(socket,u.getUserID());
                    //啟動線程
                    serverConnectClinetThread.start();
                    //將線程放進集合中管理
                    ManageServerConnectClientThread.addServerConnectClientThread(u.getUserID(),serverConnectClinetThread);

                }else{//登入失敗
                    System.out.println("用戶端 " + u.getUserID() + "密碼 " + u.getPasswd() + "登入失敗");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //如果服務端退出了while 說明服務端不在監聽 因此關閉ServerSocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
