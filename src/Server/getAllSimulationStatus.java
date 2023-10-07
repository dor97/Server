package Server;

import DTO.DTOAllSimulationStatus;
import DTO.Status;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "getAllSimulationStatus", urlPatterns = "/getAllSimulationStatus")
public class getAllSimulationStatus extends HttpServlet {

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

        DTOAllSimulationStatus simulationStatus = new DTOAllSimulationStatus();
        try {
            simulationStatus.setSimulationStatus(engine.getAllSimulationStatus());
        } catch (Exception e) {
            simulationStatus.getException().setException(e.getMessage());
        }

        Gson gson = new Gson();
        String jsonRes = gson.toJson(simulationStatus);
        response.getWriter().write(jsonRes);


    }
}
