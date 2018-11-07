package controller;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;

        String reqPath= req.getServletPath();

        if(reqPath.matches("^\\/apis[a-z/]*")){
            chain.doFilter(req,resp);
            return;
        }

        if(!reqPath.matches("^\\/admin[a-z/]*")){
            chain.doFilter(req,resp);
            return;
        }

         else if(reqPath.matches("^\\/admin[a-z/]*")) {
            Cookie[] cookies = req.getCookies();
            if(cookies==null) {
                resp.sendRedirect("/login");
                return;
            }
            String user = "";
            String pwd = "";
            boolean isAdmin = false;
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("account"))
                    user = cookies[i].getValue();
                if (cookies[i].getName().equals("password"))
                    pwd = cookies[i].getValue();
            }
            if(user.equals("admin") && pwd.equals("admin")) {
                request.getServletContext().setAttribute("username", null);
                chain.doFilter(req, resp);
                return;
            }
            else {
                resp.sendRedirect("/login");
                return;
            }
        }

    }

    @Override
    public void destroy() {

    }
}
