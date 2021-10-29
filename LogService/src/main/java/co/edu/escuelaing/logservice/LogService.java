package co.edu.escuelaing.logservice;

import com.google.gson.Gson;
import com.mongodb.client.*;
import org.bson.Document;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static spark.Spark.*;

public class LogService {

    public static MongoCollection<Document> collection;
    public static Document object;
    public static List<Log> logs;

    public static void main(String[] args) {
        try {
            MongoClient mongoClient = MongoClients.create("mongodb://mongodb:27017");
            MongoDatabase database = mongoClient.getDatabase("AYGO");
            collection = database.getCollection("LogSave");
        } catch (Exception e) {
            e.printStackTrace();
        }
        port(getPort());
        get("/logmsg", (req, res) -> storeLogMessage(req,res));
    }

    public static Integer getPort(){
        if(System.getenv("PORT") != null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    public static String storeLogMessage(Request req, Response res){
        logs = new ArrayList<>();
        object = new Document();
        String msg = req.queryParams("msg");
        object.append("log",msg);
        object.append("date",new Date());
        collection.insertOne(object);
        Document sorting = new Document();
        sorting.append("$natural",-1);
        FindIterable<Document> lastLogs = collection.find().sort(sorting).limit(10);
        for(Document doc : lastLogs){
            Log log = new Log(doc.get("log").toString(),doc.get("date").toString());
            logs.add(log);
        }
        String response = new Gson().toJson(logs);
        return response;
    }

}
