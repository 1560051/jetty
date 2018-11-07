package controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static controller.AdminController.configFreeMarker;

public class LoginController extends HttpServlet {
    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.sendRedirect(req.getHeader("referer"));
        Configuration configuration = configFreeMarker("./template");

        Template template=configuration.getTemplate("login.ftl");
        Map<String, Object> map = new HashMap<String, Object>();
        Writer out = resp.getWriter();
        try {
            template.process(map, out);
            return;
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie user = new Cookie("account",req.getParameter("user"));
        Cookie password= new Cookie("password",req.getParameter("pwd"));
        user.setMaxAge(600);
        password.setMaxAge(600);
        resp.addCookie(user);
        resp.addCookie(password);
        resp.sendRedirect("/admin");
    }
}
