package com.berray.utils;

import java.util.Scanner;

public class Utility {
    static String key;
    static Scanner scanner = new Scanner(System.in);
    public static String readString(){
        key = scanner.next();
        return key;
    }
}
