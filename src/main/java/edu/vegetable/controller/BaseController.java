package edu.vegetable.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class BaseController {

    protected static String URL = "http://8.142.64.137:8080/show_img?imgPath=";
    static Map<String, String> buildData(HttpServletRequest request, List<String> fields) {
        Map<String, String> data = new HashMap<>();
        for (String field : fields) {
            String param = request.getParameter(field);
            if (param != null && !param.equals("")) {
                data.put(field, param);
            }
        }
        return data;
    }

    static Map<String, Object> buildResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        return response;
    }

}
