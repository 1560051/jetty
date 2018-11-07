package service;

import entity.Article;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Random;

public class ArticlesResponse {
    private Collection<Article> articles = new LinkedHashSet<>();

    public Collection<Article> getArticles() {
        return articles;
    }

    public boolean add(Article article){
        return this.articles.add(article);
    }

    public static ArticlesResponse getAllArticles(){
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

    public static Article getByID(int id){
        Article article= new Article();
        try(Connection conn=connect()) {
            String sql="Select * from article where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,id);
            ResultSet rs =st.executeQuery();
            if(rs.next()==false){
                return null;
            }

            rs.previous();
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

    static Connection connect() throws IOException {
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

    public static void delete(int id){
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

    public static void edit(int id, String title, String tag, String content){
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

    public static Article insert(String title,String tag, String content){
        Article article = new Article(title,tag,content);
        return insert(article);
    }
    public static Article insert(Article article){
        try(Connection conn= connect()) {
            String sql="insert into article(title,tag,content) values(?,?,?)";
            PreparedStatement preStmt = null;
            preStmt=conn.prepareStatement(sql);
            preStmt.setString(1,article.getTitle());
            preStmt.setString(2,article.getTag());
            preStmt.setString(3,article.getContent());
            preStmt.execute();
            preStmt.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    public static Article getIdAfterInsert(Article article) {
        String sql="insert into article(title,tag,content) values(?,?,?)";
        try (Connection conn = connect()) {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, article.getTitle());
            statement.setString(2, article.getTag());
            statement.setString(3, article.getContent());

            int id=0;
            int affectedRows = statement.executeUpdate();
            if(affectedRows==0)
                return null;

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    article.setId(generatedKeys.getInt(1));
                    return article;
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return article;
    }

        public static Article getMaxID(){
        Article article= new Article();
        try(Connection conn=connect()) {
            String sql="select * from article where id=(select max(id) from article)";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs =st.executeQuery();

            while (rs.next()) {
                int id=rs.getInt("id");
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

}
