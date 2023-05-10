package com.example.manager.utils;

import com.example.manager.model.GioHang;
import com.example.manager.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    // Nhớ đổi sang IPv4 của WIFI đang dùng !!!
    public static final String BASE_URL = "http://192.168.43.205/tikitakadb/";
    public static List<GioHang> arr_giohang;
    public static List<GioHang> arr_muahang = new ArrayList<>();
    public static User user_current = new User();
    public static String ID_RECEIVED;
    public static final String SEND_ID = "idsend";
    public static final String RECEIVE_ID = "idreceive";
    public static final String MESSAGE = "message";
    public static final String DATETIME = "datetime";
    public static final String PATH_CHAT = "chat";
}