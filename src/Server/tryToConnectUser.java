package Server;


import DTO.*;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@WebServlet(name = "tryToConnectUser", urlPatterns = "/tryToConnectUser")
public class tryToConnectUser extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Engine engine = (Engine) getServletContext().getAttribute("engine");
//        InputStream inputStream = request.getInputStream();
//        String line = reader.readLine();
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//        }
        //InputStream inputStream = request.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        StringBuilder xmlContent = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            xmlContent.append(line.trim());
//        }
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        DTONameOfUser nameOfUser = gson.fromJson(reader, DTONameOfUser.class);
        DTOConnected connected = new DTOConnected();
        try {
            synchronized (getServletContext().getAttribute("users")){
                HashSet<String> users = (HashSet<String>)getServletContext().getAttribute("users");
                if(users.contains(nameOfUser.getUserName())){
                    connected.setConnected(false);
                }else{
                    users.add(nameOfUser.getUserName());
                    connected.setConnected(true);
                }
            }
        }catch (Exception e){
            connected.getException().setException(e.getMessage());
        }
        String jsonRes = gson.toJson(connected);
        response.getWriter().write(jsonRes);

    }
}
