package com.berray.qqclinet.service;

import com.berray.qqcommon.Message;
import com.berray.qqcommon.MessageType;

import java.io.*;
import java.nio.MappedByteBuffer;
/*  思路分析
* 1. 先將Message擴充功能,且新增MessageType類型
* 2. 創建方法創建message對象,將message對象內屬性重新設置
* 3. 創建輸入流將源文件讀取至內存中
* 4. 將內存中的原文件封裝到message類中,並傳入數據通道給服務端
*/

/*
* src源文件
* dest把文件傳到某個目錄
* sender發送方
* getter接收方
*
* */
public class FileClientServer {
    public static void FileToSomeone(String src,String sender,String getter){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_Message);
        message.setSrc(src);
        message.setGetter(getter);
        message.setSender(sender);
        //將文件讀取
        FileInputStream fileInputStream = null;
        byte [] fileBytes = new byte[(int)new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);
            //將文件字節數組設置到message
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //關閉流
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //提示訊息
        System.out.println("\n" + sender + " 給 " + getter + " 發送文件：" + src);

        //發送message到數據通道給服務端
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
