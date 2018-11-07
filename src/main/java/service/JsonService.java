package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonService {

    public static JsonObject getJson(HttpServletRequest req) throws IOException {
        JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(req.getInputStream()), JsonObject.class);
        return jsonObject;
    }
}
