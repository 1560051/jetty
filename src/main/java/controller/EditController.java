package controller;

import entity.Article;
import service.ArticlesResponse;
import teamplate.engine.FreeMakerConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static controller.AdminController.configFreeMarker;

public class EditController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if(req.getServletPath().equals("/admin/blog/edit")){
            int id=Integer.parseInt(req.getParameter("idEdited"));
            String title=req.getParameter("titleEdit");
            String tag=req.getParameter("tagEdit");
            String content=req.getParameter("contentEdit");
            ArticlesResponse.edit(id,title,tag,content);
            resp.sendRedirect("../list");
            return;
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(req.getServletPath().equals("/admin/blog/list")){
            Configuration configuration = configFreeMarker("./template/admin/blog");

            ArticlesResponse articles = ArticlesResponse.getAllArticles();;

            Template tempalte = configuration.getTemplate("list-article.ftl");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", articles.getArticles());
            Writer out = resp.getWriter();

            try {
                tempalte.process(map, out);
                return;
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }

        String getPath=req.getPathInfo();
        String servletPath=req.getServletPath();
        if(getPath.matches("^\\/[1-9][0-9]*")&&servletPath.equals("/admin/blog/edit")){
            String getId = req.getPathInfo().replaceAll("/", "");
            int id=Integer.parseInt(getId);
            Configuration configuration = FreeMakerConfig.config("./template/admin/blog");

            Template articleTmp=configuration.getTemplate("edit.ftl");
            Article article= ArticlesResponse.getByID(id);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("article", article);
            Writer out = resp.getWriter();

            try {
                articleTmp.process(map, out);
                return;
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }

        //delete by id
        if(req.getServletPath().equals("/admin/blog/delete") && getPath.matches("^\\/[1-9][0-9]*")){
            String getId = req.getPathInfo().replaceAll("/", "");
            int id=Integer.parseInt(getId);
            ArticlesResponse.delete(id);
            resp.sendRedirect("../list");
            return;
        }
    }
}
