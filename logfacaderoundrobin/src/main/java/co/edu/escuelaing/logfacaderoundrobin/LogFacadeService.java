package co.edu.escuelaing.logfacaderoundrobin;

import spark.Request;
import spark.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class LogFacadeService {

    public static int currentService = 0;
    public static List<String> serviceList = new ArrayList<>();

    public static void main(String[] args){
        serviceList.add("http://dockercontainer1:6000/logmsg?msg=");
        serviceList.add("http://dockercontainer2:6000/logmsg?msg=");
        serviceList.add("http://dockercontainer3:6000/logmsg?msg=");

        staticFiles.location("/public");
        port(getPort());

        post("/logfacade", (req,res) -> roundRobinFacadeDelegation(req,res));

    }

    public static Integer getPort(){
        if(System.getenv("PORT") != null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    public static String roundRobinFacadeDelegation(Request req, Response res){

        System.out.println("Calling service on: " +  serviceList.get(currentService));
        String resp = callDelegateService(serviceList.get(currentService),req,res);
        currentService ++;
        System.out.println("Respuesta " + resp);
        if(currentService > 2){
            currentService = 0;
        }
        return resp;
    }

    public static String callDelegateService(String serviceUrlStr, Request req, Response res){
        serviceUrlStr = serviceUrlStr.concat(req.queryParams("msg"));
        String resp = "";
        try {
            URL serviceUrl = new URL(serviceUrlStr);
            BufferedReader in = new BufferedReader(new InputStreamReader(serviceUrl.openStream()));
            String inputLine;
            while((inputLine = in.readLine()) != null){
                resp = inputLine;
            }
            return resp;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Something goes wrong...";
    }

}
