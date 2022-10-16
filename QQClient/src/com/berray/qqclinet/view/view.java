package com.berray.qqclinet.view;

import com.berray.qqclinet.service.FileClientServer;
import com.berray.qqclinet.service.MessageClient;
import com.berray.qqclinet.service.UserClinetService;
import com.berray.utils.Utility;
@SuppressWarnings({"all"})
public class view {
    private boolean loop = true;
    private String key;
    private UserClinetService userClinetService = new UserClinetService();
    private MessageClient messageClient = new MessageClient();
    public static void main(String[] args) {
        new view().mainMenu();
    }
    //主頁面
    public void mainMenu(){
        while(loop){
            System.out.println("===============網路系統===============");
            System.out.println("\t\t1  登入系統");
            System.out.println("\t\t9  退出系統");
            System.out.print("請輸入：");
            key = Utility.readString();
            switch(key){
                case "1":
                    System.out.print("帳號：");
                    String userID = Utility.readString();
                    System.out.print("密碼：");
                    String passwd = Utility.readString();
                    if(userClinetService.checkUser(userID,passwd)){
                        System.out.println("========== 歡迎 " + userID + " 登入 ========\n");
                        System.out.println("==========網路通信系統登入介面(" + userID +")==========");
                        while(loop) {
                            System.out.println("\t\t1  顯示在線用戶");
                            System.out.println("\t\t2  群發消息");
                            System.out.println("\t\t3  私聊消息");
                            System.out.println("\t\t4  發送文件");
                            System.out.println("\t\t9  退出系統");
                            System.out.print("請輸入：");
                            key = Utility.readString();
                            switch (key) {
                                case "1":
                                    System.out.println("\n");
                                    userClinetService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("輸入群發消息：");
                                    String newAll = Utility.readString();
                                    userClinetService.sendMessageToAll(newAll,userID);
                                    break;
                                case "3":
                                    System.out.print("接收者(上線)：");
                                    String getterID = Utility.readString();
                                    System.out.println("發送內容：");
                                    String content = Utility.readString();
                                    messageClient.sendMessageToSome(getterID,userID,content);
                                    break;
                                case "4":
                                    System.out.print("發送文件對象：");
                                    String getter = Utility.readString();
                                    System.out.print("發送文件路徑：");
                                    String src = Utility.readString();
                                    FileClientServer.FileToSomeone(src,userID,getter);
                                    break;
                                case "9":
                                    userClinetService.logOut();
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("輸入錯誤 重新輸入");
                                    break;
                            }
                        }

                    }
                    break;
                case "9":
                    loop = false;
                    System.out.println("退出系統");
                    break;
                default:
                    System.out.println("輸入錯誤 重新輸入");
                    break;
            }
        }
        System.out.println("退出系統了");
    }
}
