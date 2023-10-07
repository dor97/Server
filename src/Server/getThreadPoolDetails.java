package Server;

import DTO.DTOThreadPoolDetails;
import DTO.Status;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "getThreadPoolDetails", urlPatterns = "/getThreadPoolDetails")
public class getThreadPoolDetails extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = (Engine) getServletContext().getAttribute("engine");

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

        DTOThreadPoolDetails threadPoolDetails = new DTOThreadPoolDetails();
        try {
            threadPoolDetails.setThreadPoolDetails(engine.threadPoolDetails());
        } catch (Exception e) {
            threadPoolDetails.getException().setException(e.getMessage());
        }

        Gson gson = new Gson();
        String jsonRes = gson.toJson(threadPoolDetails);
        response.getWriter().write(jsonRes);


    }


}
