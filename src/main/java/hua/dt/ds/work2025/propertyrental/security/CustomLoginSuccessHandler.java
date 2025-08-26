package hua.dt.ds.work2025.propertyrental.security;

import hua.dt.ds.work2025.propertyrental.entities.User;
import hua.dt.ds.work2025.propertyrental.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
                                        ) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/user/admin-dashboard");
                return;
            } else if (role.equals("ROLE_OWNER")) {
               response.sendRedirect("propertyforrent/ownerlist");
                return;
            } else if (role.equals("ROLE_TENANT")) {
                response.sendRedirect("/user/tenant-dashboard");
                return;
            }
        }

        response.sendRedirect("/default"); // fallback
    }


}
