package Server;


import DTO.DTOException;
import DTO.DTOSimulationId;
import DTO.DTOThreadPoolSize;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "setThreadPoolSize", urlPatterns = "/setThreadPoolSize")
public class setThreadPoolSize extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {//TODO changed to get, also in the client
        Engine engine = (Engine) getServletContext().getAttribute("engine");

        BufferedReader reader = request.getReader();

//        Gson gson = new Gson();
//        DTOPrepareSimulationData prepareSimulationData = gson.fromJson(reader, DTOPrepareSimulationData.class);

//        StringBuilder requestBody = new StringBuilder();
//        String line;
//        String approval;
//        Integer id;
//        List<String> strings = new ArrayList<>();
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//            strings.add(line);
//        }
        Gson gson = new Gson();
        DTOThreadPoolSize threadPoolSize = gson.fromJson(reader, DTOThreadPoolSize.class);
        DTOException exception = new DTOException();
        try {
            engine.setThreadPoolSize(threadPoolSize.getSize());
        }catch (Exception e){
            exception.setException(e.getMessage());
        }

        String jsonRes = gson.toJson(exception);
        response.getWriter().write(jsonRes);

    }

}
