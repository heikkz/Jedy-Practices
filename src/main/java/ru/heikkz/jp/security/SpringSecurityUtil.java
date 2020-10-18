package ru.heikkz.jp.security;

import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class SpringSecurityUtil {

    private SpringSecurityUtil() {
        throw new AssertionError();
    }

    /**
     * Gets the current user details.
     *
     * @return the current user details or null if can't be retrieved.
     */
    public static UserDetails getCurrentUserDetails() {
        final Authentication authentication = getCurrentAuthentication();
        if (authentication == null) {
            return null;
        }

        final Object principal = authentication.getPrincipal();
        if (principal == null) {
            return null;
        }
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        return null;
    }

    public static Authentication getCurrentAuthentication() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            return null;
        }
        return securityContext.getAuthentication();
    }

    /**
     * Check if current user is authenticated.
     *
     * @return true if user is authenticated.
     */
    public static boolean isAuthenticated() {
        return SpringSecurityUtil.getCurrentUserDetails() != null;
    }

    /**
     * Check if current user is anonymous guest.
     *
     * @return true if user is anonymous guest.
     */
    public static boolean isAnonymous() {
        return SpringSecurityUtil.getCurrentUserDetails() == null;
    }

}
