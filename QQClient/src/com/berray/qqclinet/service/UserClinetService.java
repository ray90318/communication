package com.berray.qqclinet.service;

import com.berray.qqcommon.Message;
import com.berray.qqcommon.MessageType;
import com.berray.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

@SuppressWarnings({"all"})
public class UserClinetService {//系統功能方法類
    User user = new User();
    boolean b = false;
    Socket socket = new Socket();
    //對所有人發送消息
    public void sendMessageToAll(String content,String sender){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CONTENTALL_Message);
        message.setContent(content);
        message.setSender(sender);
        message.setSendTime(new Date().toString());
        System.out.println(sender + " 對所有人說：" + content);
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //此方法用來驗證是否有此用戶
    public boolean checkUser(String userID,String passwd){
        user.setUserID(userID);
        user.setPasswd(passwd);
        try {
            //將user對象傳到服務端
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            //接收服務端傳來的訊息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message)ois.readObject();
            if(message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCESSEDD)){//登入成功
                b = true;
            //創建一個線程來保持跟服務端的通訊-創建一個線程類ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                //將此線程傳入集合中
                ManageClientConnectServerThread.addClientConnectServer(userID,clientConnectServerThread);


            }else{//登入失敗
                //關閉socket流
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;

    }
    //獲取在線用戶列表方法
    public void onlineFriendList(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINEFRIEND);
        message.setSender(user.getUserID());
        try {
            //從集合中,通過userID得到當下的線程,且再從當下線程的socket得到相對應的objectoutputstream
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageClientConnectServerThread.getClientConnectServerThread
                            (user.getUserID()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //退出系統
    public void logOut(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserID());
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (user.getUserID()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserID() + "退出系統");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
