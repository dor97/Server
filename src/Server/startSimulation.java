package Server;


import DTO.DTOPrepareSimulationData;
import DTO.DTOSimulationId;
import DTO.DTOStartSimulation;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "startSimulation", urlPatterns = "/startSimulation")
public class startSimulation extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Engine engine = (Engine) getServletContext().getAttribute("engine");

        BufferedReader reader = request.getReader();

        Gson gson = new Gson();
        DTOStartSimulation startSimulation = gson.fromJson(reader, DTOStartSimulation.class);
        DTOSimulationId simulationId = new DTOSimulationId();

//        BufferedReader reader = request.getReader();
//
//        StringBuilder requestBody = new StringBuilder();
//        String line;
//        String approval;
//        Integer id;
//        List<String> strings = new ArrayList<>();
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//            strings.add(line);
//        }
        try {
            simulationId.setSimulationId(engine._startSimulation(startSimulation.getUserName()));
        }catch (Exception e){
            simulationId.getException().setException(e.getMessage());
        }

        String jsonRes = gson.toJson(simulationId);
        response.getWriter().write(jsonRes);

    }
}
