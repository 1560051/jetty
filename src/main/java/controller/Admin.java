package controller;

import entity.Article;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
//import org.eclipse.jetty.util.ajax.JSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.*;
import java.util.*;

public class Admin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String getById=req.getParameter("getById");

        // GET ALL
        if(req.getParameter("getAll")!=null) {
            if (req.getParameter("getAll").equals("true")) {
                Gson gson = new Gson();
                String _articlesJson = gson.toJson(getAllArticles());
                resp.getWriter().write(_articlesJson);
                return;
            }
        }
        // GET BY ID
        else if(getById!=null){
            Gson gson=new Gson();
            String articleJson=gson.toJson(getArticleByID(Integer.parseInt(getById)));
            resp.getWriter().write(articleJson);
            return;
        }

        //Get ID Edited
        String getIdEdit=req.getParameter("idEdit");
        if(getIdEdit!=null){
            Article article=getArticleByID(Integer.parseInt(getIdEdit));

            Configuration configuration = initFreeMaker();

            Template tempalte = configuration.getTemplate("edit.ftl");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id",article.getId());
            map.put("title",article.getTitle());
            map.put("tag",article.getTag());
            map.put("content",article.getContent());

            Writer out = resp.getWriter();

            try {
                tempalte.process(map, out);
                return;
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        //Login admin
        if(req.getParameter("user")!=null){
            if(req.getParameter("user").equals("admin")){
                resp.sendRedirect("/admin.html");
                return;
            }
        }

        //DELETE
        String idDelete=req.getParameter("delete");
        if(idDelete!=null){
            delete(Integer.parseInt(idDelete));
            resp.getWriter().print("Deleted!! "+idDelete);
            return;
        }


        String title = req.getParameter("title");
        String tag=req.getParameter("tag");
        String content=req.getParameter("content");
        //INSERT
        if(title!=null && content != null){
            JsonObject json= insert(title,tag,content);
            resp.getWriter().write(json.toString());
            return;
        }


         //Edit
        if(req.getParameter("idEdited")!=null) {
            String idEdit = req.getParameter("idEdited");
            String titleEdit = req.getParameter("titleEdit");
            String tagEdit = req.getParameter("tagEdit");
            String contentEdit = req.getParameter("contentEdit");
            edit(Integer.parseInt(idEdit), titleEdit, tagEdit, contentEdit);
            resp.getWriter().write("Update Success");
            return;
        }

    }

    JsonObject getById(int id){
        JsonObject json = new JsonObject();
        Gson gson = new GsonBuilder().create();
        try(Connection conn=connect()) {
            String sql="Select * from article where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,id);
            ResultSet rs =st.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String tag=rs.getString("tag");
                String content=rs.getString("content");

                Article article = new Article(id,title,tag,content);
                json.add("article",gson.toJsonTree(article));
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    Article getArticleByID(int id){
        Article article= new Article();
        try(Connection conn=connect()) {
            String sql="Select * from article where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,id);
            ResultSet rs =st.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String tag=rs.getString("tag");
                String content=rs.getString("content");
                article.set(id,title,tag,content);
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return article;
    }

    JsonObject insert(String title,String tag, String content){
        JsonObject json = new JsonObject();
        try(Connection conn= connect()) {
            String sql="insert into article(title,tag,content) values(?,?,?)";
            PreparedStatement preStmt = null;
            preStmt=conn.prepareStatement(sql);
            preStmt.setString(1,title);
            preStmt.setString(2,tag);
            preStmt.setString(3,content);
            preStmt.execute();
            preStmt.close();

            Article article = new Article(title,tag,content);
            Gson gson = new GsonBuilder().create();
            JsonElement jsonElement = gson.toJsonTree(article);
            json.add("article",jsonElement);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return json;
    }


    static class ArticlesResponse{
        Collection<Article> articles = new LinkedHashSet<>();

        public boolean add(Article article){
            return this.articles.add(article);
        }
    }

    ArticlesResponse getAllArticles(){
        ArticlesResponse response = new ArticlesResponse();
        try(Connection conn=connect()) {
            String sql="Select * from article";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String tag=rs.getString("tag");
                String content=rs.getString("content");
                Article article = new Article(id,title,tag,content);
                response.add(article);
                if(new Random(1L).nextBoolean())
                    response.add(article);
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    void edit(int id, String title, String tag, String content){
        try(Connection conn=connect()) {
            String sql="update article set title=?, tag=?, content=? where id=?";
            PreparedStatement preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setString(1,title);
            preparedStatement.setString(2,tag);
            preparedStatement.setString(3,content);
            preparedStatement.setInt(4,id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void delete(int id){
        JsonObject json = new JsonObject();
        Gson gson = new GsonBuilder().create();
        try(Connection conn=connect()) {
            String sql="delete from article where id=?";
            PreparedStatement preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws IOException {
        Connection conn = null;

        String user = "root";
        String pwd = "123456";
        String url = "jdbc:mysql://localhost:3306/blog?useSSL=false";
        try{
            conn = DriverManager.getConnection(url, user, pwd);

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }

        return conn;
    }


    public static Configuration initFreeMaker(){
        Configuration configuration = new Configuration();
        try {
            configuration.setDirectoryForTemplateLoading(new
                    File("./template/"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return configuration;
    }
}
