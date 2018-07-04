import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
//We need to have this installed in your computer.


import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

public class App {
    //The main method
    public static void main(String args[]){
        staticFileLocation("public");
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("name", request.session().attribute(request.queryParams("name")));
            model.put("templates", "templates/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
        get("/send_mail",(request, response) -> {
            Map<String, Object> models = new HashMap<String, Object>();
            models.put("templates", "templates/send_mail.vtl");
            return new ModelAndView(models,"templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/send_mail",(request, response) -> {
          Map<String, Object> models = new HashMap<String, Object>();
          String enteredName = request.queryParams("name");
          request.session().attribute("name", enteredName);
          models.put("name", enteredName);
            models.put("templates", "templates/sent_mail.vtl");
            return new ModelAndView(models, "templates/layout.vtl");
        },new VelocityTemplateEngine());
    }
}
