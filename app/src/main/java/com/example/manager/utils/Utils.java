package com.example.manager.utils;

import com.example.manager.model.GioHang;
import com.example.manager.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    // Nhớ đổi sang IPv4 của WIFI đang dùng !!!
    public static final String BASE_URL = "http://192.168.1.13/tikitakadb/";
    public static List<GioHang> arr_giohang;
    public static List<GioHang> arr_muahang = new ArrayList<>();
    public static User user_current = new User();
}