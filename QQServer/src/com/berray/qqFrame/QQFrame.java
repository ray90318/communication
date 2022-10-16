package com.berray.qqFrame;

import com.berray.qqserver.service.QQServer;

//該類創建一個qq對象 啟動後台服務
public class QQFrame {
    public static void main(String[] args) {
        new QQServer();
    }
}
