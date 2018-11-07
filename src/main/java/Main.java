import controller.*;
//import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        LoginService loginService = new HashLoginService();

        ServletContextHandler handler = new ServletContextHandler();
        handler.addFilter(AuthenticationFilter.class, "/*", null );
//        handler.addFilter(DebugFilter.class, "/*", 2 );
//        handler.addFilter(DebugFilter.class, "/*", 4 );
//        handler.addFilter(DebugFilter.class, "/*", 8 );
//        handler.addFilter(DebugFilter.class, "/*", 16 );
        handler.addServlet(HomeController.class, "/home");
        handler.addServlet(HomeController.class,"/home/article");
//        handler.addServlet(HomeController.class,"");
        handler.addServlet(LoginController.class,"/login");
        handler.addServlet(AdminController.class,"/admin");
        handler.addServlet(AdminController.class,"/admin/blog");
        handler.addServlet(LoginController.class,"");
        handler.addServlet(EditController.class,"/admin/blog/list");
        handler.addServlet(AdminController.class,"/admin/blog/create");
        handler.addServlet(AdminController.class,"/admin/blog/create-form");
        handler.addServlet(EditController.class,"/admin/blog/edit-form");
        handler.addServlet(EditController.class,"/admin/blog/edit/*");
        handler.addServlet(EditController.class,"/admin/blog/delete/*");
        handler.addServlet(ApiBlogController.class,"/apis/blog/*");
        handler.addServlet(ApiBlogController.class,"/apis/blog");



        //1.Creating the resource handler
        ResourceHandler resourceHandler= new ResourceHandler();

        //2.Setting Resource Base
        resourceHandler.setResourceBase("template");

        //3.Enabling Directory Listing
        resourceHandler.setDirectoriesListed(true);

        //4.Setting Context Source
        ContextHandler contextHandler= new ContextHandler("/");

        //5.Attaching Handlers
        contextHandler.setHandler(resourceHandler);

        server.setHandler(contextHandler);
        HandlerList handlerList=new HandlerList();
        handlerList.setHandlers(new Handler[]{contextHandler,handler});
        server.setHandler(handlerList);

        server.start();
        server.join();
    }

   public static class DebugFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            System.out.println(((HttpServletRequest) request).getPathInfo());
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {

        }
    }
}
