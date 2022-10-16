package com.berray.qqclinet.service;

import com.berray.qqcommon.Message;
import com.berray.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class MessageClient {
    public void sendMessageToSome(String getterID,String senderID,String content){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMMON_MESSAGE);
        message.setSender(senderID);
        message.setGetter(getterID);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(senderID + " 對 " + getterID + " 説： " + content);
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (senderID).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
