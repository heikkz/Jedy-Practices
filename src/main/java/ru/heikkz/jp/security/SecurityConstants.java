package ru.heikkz.jp.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityConstants {

    public static final String AUTH_URLS = "/api/v1/auth/**";
    public static final String USER_URLS = "/api/v1/user/**";
    public static final String HABIT_URLS = "/api/v1/habit/**";
    public static final String ADMIN_URLS = "/api/v1/admin/**";

}
