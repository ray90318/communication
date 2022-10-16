package com.berray.qqclinet.service;

import com.berray.qqcommon.Message;
import com.berray.qqcommon.MessageType;
import com.berray.utils.Utility;
import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

@SuppressWarnings({"all"})
public class ClientConnectServerThread extends Thread {//讓客戶端不斷跟服務端進行通訊
    //將socket放入線程
    private Socket socket = new Socket();
    Scanner scanner;
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("客戶端線程,等待讀取從服務端發送的消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // 如果服務端沒有發送Message對象,線程會阻塞在這
                Message message = (Message) ois.readObject();
                //判斷是否返回在線用戶列表
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINEFRIEND)) {
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n==============在線用戶==============");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用戶：" + onlineUsers[i]);
                    }
                    //如果傳來類型是訊息
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMMON_MESSAGE)) {
                    System.out.println("\n(" + message.getSendTime() + ")" + message.getSender() + " 對我説：" + message.getContent());
                } else if(message.getMesType().equals(MessageType.MESSAGE_CONTENTALL_Message)){
                    System.out.println("\n(" + message.getSendTime() + ")" + message.getSender() + " 對大家說：" + message.getContent());
                }else if(message.getMesType().equals(MessageType.MESSAGE_FILE_Message)){
                    System.out.print("\n" + message.getSender() + " 發了文件給我請輸入保存位置：");
                    String dest = scanner.next();
                    message.setDest(dest);
                    System.out.println("\n" + message.getSender() + "給我發了文件：" + message.getSrc() + " 到我的目錄：" + message.getDest());
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n保存文件成功");
                }else {
                    System.out.println("返回其他類型,暫時不處理");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


}
