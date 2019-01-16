package com.videojj.videoservice.util;

public class PermissionUtil {
    public final static ThreadLocal<String> THREAD_LOCAL_USERNAME = new ThreadLocal<String>();
    public final static String DEFAULT_USERNAME = "Default Username";

    public static synchronized void setCurrentUsername(String username) {
        THREAD_LOCAL_USERNAME.set(username);
    }

    public static String getCurrentUsername() {
        String currentUsername = THREAD_LOCAL_USERNAME.get();
        return currentUsername == null ? DEFAULT_USERNAME : currentUsername;
    }
}
