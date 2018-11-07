package controller;

import service.ArticlesResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class AdminController extends HttpServlet {

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //create
        if(req.getServletPath().equals("/admin/blog/create-form")){

            Configuration configuration = configFreeMarker("./template/admin/blog");

            Template createTmp=configuration.getTemplate("create.ftl");
            Map<String, Object> map = new HashMap<String, Object>();
            Writer out = resp.getWriter();
            try {
                createTmp.process(map, out);
                return;
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }

        if(req.getServletPath().equals("/admin/blog/create")) {
            String title = req.getParameter("title");
            String tag = req.getParameter("tag");
            String content = req.getParameter("content");
            if (title != null && content != null) {
                ArticlesResponse.insert(title, tag, content);
                resp.sendRedirect("./list");
            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

            Configuration configuration = configFreeMarker("./template/admin/blog");

            Template articleTmp=configuration.getTemplate("admin.ftl");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("admin","admin");
            Writer out = resp.getWriter();

            try {
                articleTmp.process(map, out);
                return;
            } catch (TemplateException e) {
                e.printStackTrace();
            }

    }

    static Configuration configFreeMarker(String path){
        //./template/admin/blog
        Configuration configuration = new Configuration();
        try {
            configuration.setDirectoryForTemplateLoading(new
                    File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return configuration;
    }
}
