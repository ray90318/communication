package com.berray.qqserver.service;

import com.berray.qqcommon.Message;
import com.berray.qqcommon.MessageType;



import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class ServerToAll implements Runnable{
    Scanner scanner;
    @Override
    public void run() {
        Message message = new Message();
        scanner = new Scanner(System.in);
        while(true){
            System.out.println("輸入要對客戶端說的內容(exit退出)：");
            String news = scanner.next();
            if(news.equals("exit")){

                break;
            }
            message.setMesType(MessageType.MESSAGE_CONTENTALL_Message);
            message.setSender("伺服器");
            message.setContent(news);
            message.setSendTime(new Date().toString());
            HashMap<String, ServerConnectClinetThread> hashmap = ManageServerConnectClientThread.getHashmap();
            Iterator<String> iterator = hashmap.keySet().iterator();
            System.out.println("伺服器對所有人説：" + message.getContent());
            while(iterator.hasNext()){
                String userOnlie = iterator.next().toString();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream
                            (ManageServerConnectClientThread.getServerConnectClinetThread
                                    (userOnlie).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
