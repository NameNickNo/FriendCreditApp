package com.friend.app.models.person;

public enum Role {
    ROLE_ADMIN, ROLE_USER, ROLE_REMOVED;

    public static String getUserShortName() {
        return "USER";
    }

    public static String getAdminShortName() {
        return "ADMIN";
    }

}
