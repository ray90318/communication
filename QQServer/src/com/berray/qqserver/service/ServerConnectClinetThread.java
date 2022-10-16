package com.berray.qqserver.service;

import com.berray.qqcommon.Message;
import com.berray.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class ServerConnectClinetThread extends Thread {
    private Socket socket = new Socket();
    private String userID;

    public Socket getSocket() {
        return socket;
    }

    public ServerConnectClinetThread(Socket socket, String userID) {
        this.socket = socket;
        this.userID = userID;
    }

    @Override
    public void run() {
        while(true) {
            try {
                System.out.println("服務端和 " + userID + " 保持通訊,讀取訊息...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message mes = (Message) ois.readObject();
                //根據不同類型的message,返回相對應的功能
                Message message = new Message();
                if(mes.getMesType().equals(MessageType.MESSAGE_GET_ONLINEFRIEND)){
                    System.out.println(mes.getSender() + "要求查看在線用戶列表");
                    String onlineUsers = ManageServerConnectClientThread.getonlineUser();

                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINEFRIEND);
                    message2.setGetter(mes.getSender());
                    message2.setContent(onlineUsers);
                    //返回給客戶端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                    //接收一般訊息並返回訊息
                }else if(mes.getMesType().equals(MessageType.MESSAGE_COMMON_MESSAGE)){
                    ServerConnectClinetThread serverConnectClinetThread = ManageServerConnectClientThread.getServerConnectClinetThread(mes.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClinetThread.getSocket().getOutputStream());
                    oos.writeObject(mes);
                    //傳過來的類型是發送群聊,並返回訊息
                }else if(mes.getMesType().equals(MessageType.MESSAGE_CONTENTALL_Message)){
                    Message message2 = new Message();
                    message2.setContent(mes.getContent());
                    message2.setMesType(MessageType.MESSAGE_CONTENTALL_Message);
                    message2.setSender(mes.getSender());
                    message2.setSendTime(mes.getSendTime());
                    HashMap<String, ServerConnectClinetThread> hashmap = ManageServerConnectClientThread.getHashmap();
                    Iterator<String> iterator = hashmap.keySet().iterator();
                    while (iterator.hasNext()) {
                        String s = iterator.next().toString();
                        if(!(s == mes.getSender())){
                            ObjectOutputStream oos = new ObjectOutputStream
                                    (ManageServerConnectClientThread.getServerConnectClinetThread(s)
                                            .getSocket().getOutputStream());
                            oos.writeObject(message2);

                        }

                    }
//                    String onlineAll = ManageServerConnectClientThread.getonlineUser();
//                    Message message2 = new Message();
//                    message2.setContent(mes.getContent());
//                    message2.setMesType(MessageType.MESSAGE_CONTENTALL_Message);
//                    message2.setSender(mes.getSender());
//                    message2.setSendTime(mes.getSendTime());
//                    String[] userOnlie = onlineAll.split(" ");
//                    for(int i=0;i<userOnlie.length;i++){
//                        ServerConnectClinetThread serverConnectClinetThread = ManageServerConnectClientThread.getServerConnectClinetThread(userOnlie[i]);
//                        ObjectOutputStream oos = new ObjectOutputStream
//                                (serverConnectClinetThread.getSocket().getOutputStream());
//                        oos.writeObject(message2);
//                    }
                    //發送過來的事文件類型
                }else if(mes.getMesType().equals(MessageType.MESSAGE_FILE_Message)){
                    ObjectOutputStream oos = new ObjectOutputStream
                            (ManageServerConnectClientThread.getServerConnectClinetThread
                                    (mes.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(mes);

                }else if(mes.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){//如果傳過來的類型是退出

                    System.out.println(userID + " 退出了");
                    ManageServerConnectClientThread.deleteServerConnectClinetThread(userID);
                    //關閉連接
                    socket.close();
                    //退出循環
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
