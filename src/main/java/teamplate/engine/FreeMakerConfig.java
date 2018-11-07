package teamplate.engine;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

public class FreeMakerConfig {

    public static Configuration config(){
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

    public static Configuration config(String path){
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
