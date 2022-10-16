package com.berray.qqcommon;
//表示消息類型
@SuppressWarnings({"all"})
public interface MessageType {
    //在此接口中定義一些不同常量的詞,表示不同消息型態
    String MESSAGE_LOGIN_SUCCESSEDD = "1";
    String MESSAGE_LOGIN_FAIL = "2";
    String MESSAGE_COMMON_MESSAGE = "3";//普通訊息
    String MESSAGE_GET_ONLINEFRIEND = "4";//要求返回在線用戶列表
    String MESSAGE_RET_ONLINEFRIEND = "5";//返回在線用戶列表
    String MESSAGE_CLIENT_EXIT = "6";//客戶端請求退出
    String MESSAGE_CONTENTALL_Message = "7";//傳訊息給所有人
    String MESSAGE_FILE_Message = "8";//傳送文件
}
