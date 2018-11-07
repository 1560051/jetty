package controller;

import entity.Article;
import entity.ErrorJson;
import service.ArticlesResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import service.JsonService;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApiBlogController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { resp.setHeader("Access-Control-Allow-Origin","*");
//        String s = req.getServletPath();
//        System.out.println("s = " + s);
//        System.out.println("req.getPathInfo() = " + req.getPathInfo());


        //GET ALL
        if (req.getPathInfo() != null) {
            if (req.getPathInfo().equals("/")) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                Gson gson = new Gson();
                String _articlesJson = gson.toJson(ArticlesResponse.getAllArticles());
                resp.getWriter().write(_articlesJson);
                return;
            }
//            String getParam = req.getRequestURL().toString();
//            getParam = getParam.substring(32, getParam.length());
            //GET BY ID
            else if (req.getPathInfo().matches("^\\/[1-9][0-9]*$")) {
                String getId = req.getPathInfo().replaceAll("/", "");
                int id = 0;
                Gson gson = new Gson();
                id=Integer.parseInt(getId);
                Article article = ArticlesResponse.getByID(id);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                if (article == null) {
                    resp.setStatus(202);
                    ErrorJson errJ= new ErrorJson("ID is not Exist!",202);
                    String json = gson.toJson(errJ);
                    resp.getWriter().write(json);
                    return;
                }

                String articleJson = gson.toJson(article);
                resp.getWriter().write(articleJson);
                return;
            } else {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.setStatus(400);
                Gson gson= new Gson();
                ErrorJson errJ= new ErrorJson("Servlet path is invalid",400);
                String json = gson.toJson(errJ);
                resp.getWriter().write(json);
                return;
//                Configuration configuration = configFreeMarker("./template");
//
//                Template createTmp = configuration.getTemplate("error.ftl");
//                Map<String, Object> map = new HashMap<String, Object>();
//                Writer out = resp.getWriter();
//                try {
//                    resp.setStatus(400);
//                    createTmp.process(map, out);
//                    return;
//                } catch (TemplateException e) {
//                    e.printStackTrace();
//                }
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ensureContentType(req);
        resp.setContentType("application/json");
        JsonObject jsonObject = JsonService.getJson(req);

        String title = jsonObject.get("title").getAsString();
        String tag = jsonObject.get("tag").getAsString();
        String content = jsonObject.get("content").getAsString();
//        if (title == null)
//            title = "";
//        if (tag == null)
//            tag = "";
//        if (content == null)
//            content = "";

        Article article= ArticlesResponse.getIdAfterInsert(new Article(title,tag,content));
        Gson gson = new Gson();
        resp.setStatus(200);
        resp.getWriter().write(gson.toJson(article));
    }

    private void ensureContentType(HttpServletRequest req) {
        String header = req.getHeader("Content-type");

    }





    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getPathInfo() != null) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            if (req.getPathInfo().matches("^\\/[1-9][0-9]*$") == true) {
                String getId = req.getPathInfo().replaceAll("/", "");
                int id = 0;
                id = Integer.parseInt(getId);
                Article article = ArticlesResponse.getByID(id);
                Gson gson = new Gson();

                if (article == null) {
                    resp.setStatus(202);
                    ErrorJson errJ= new ErrorJson("ID is not Exist!",202);
                    String json = gson.toJson(errJ);
                    resp.getWriter().write(json);
                    return;
                }
                ArticlesResponse.delete(id);
                String json = gson.toJson(article);
                resp.getWriter().write(json);
                return;
            } else {
                resp.setStatus(400);
                Gson gson= new Gson();
                ErrorJson errJ= new ErrorJson("Servlet path is invalid",400);
                String json = gson.toJson(errJ);
                resp.getWriter().write(json);
                return;
            }
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() != null) {
            resp.setContentType("application/json");
            String path=req.getPathInfo();
            if (path.matches("^\\/[1-9][0-9]*$")) {
                String getId = path.replaceAll("/", "");
                int id = Integer.parseInt(getId);
                String title = req.getParameter("title");
                String tag = req.getParameter("tag");
                String content = req.getParameter("content");
                ArticlesResponse.edit(id, title, tag, content);
                Article article = ArticlesResponse.getByID(id);
                Gson gson=new Gson();

                if(article==null){
                    resp.setStatus(202);
                    ErrorJson errJ= new ErrorJson("ID is not Exist!",202);
                    String json = gson.toJson(errJ);
                    resp.getWriter().write(json);
                    return;
                }

                resp.getWriter().write(gson.toJson(article));
            }
            else{
                resp.setStatus(400);
                Gson gson= new Gson();
                ErrorJson errJ= new ErrorJson("Servlet path is invalid",400);
                String json = gson.toJson(errJ);
                resp.getWriter().write(json);
                return;
            }
        }
    }
}
