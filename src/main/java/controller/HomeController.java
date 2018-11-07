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
import java.util.*;

public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("id")==null) {
            ArticlesResponse articles = ArticlesResponse.getAllArticles();
            Configuration configuration = FreeMakerConfig.config();

            Template tempalte = configuration.getTemplate("home.ftl");
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

        if(req.getParameter("id")!=null){
            int id=Integer.parseInt(req.getParameter("id"));
            Configuration configuration = FreeMakerConfig.config();
            Template articleTmp=configuration.getTemplate("article.ftl");
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

    }
}
