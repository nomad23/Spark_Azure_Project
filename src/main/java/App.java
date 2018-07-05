import org.sql2o.Connection;
import spark.ModelAndView;

import java.sql.ResultSet;
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
            ResultSet rst;
            Map<String, Object> models = new HashMap<String, Object>();
            models.put("templates", "templates/send_mail.vtl");
            //Retrieve data from the database
            try(Connection con = DB.sql2o.open()){
                String sqlQuery = "SELECT * FROM people;";
                 rst = (ResultSet) con.createQuery(sqlQuery);

            }
            List<Model> myModels = new ArrayList<Model>();
            while (rst.next()){
                Model themodel = new Model(rst.getString("email"), rst.getString("comment"));
                myModels.add(themodel);
            }


            return new ModelAndView(models,"templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/send_mail",(request, response) -> {
          Map<String, Object> models = new HashMap<String, Object>();
          String enteredName = request.queryParams("name");
          String enteredEmail = request.queryParams("email");
          String enteredComment = request.queryParams("comment");
          request.session().attribute("name", enteredName);
          models.put("name", enteredName);
          models.put("email", enteredEmail);
          models.put("comment", enteredComment);
          //Sving the data to the database
          try(Connection con = DB.sql2o.open()){
              String sqlQuery = "INSERT INTO people (email, comment) VALUES (:enteredEmail, :enteredComment);";
              con.createQuery(sqlQuery)
                      .addParameter("enteredEmail", enteredEmail)
                      .addParameter("enteredComment", enteredComment)
                      .executeUpdate();
          }
            models.put("templates", "templates/sent_mail.vtl");
            return new ModelAndView(models, "templates/layout.vtl");
        },new VelocityTemplateEngine());
    }
}
