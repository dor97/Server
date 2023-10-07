package Server;

import DTO.DTOSimulationId;
import DTO.DTOSimulationStatus;
import DTO.Status;
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

@WebServlet(name = "getSimulationStatus", urlPatterns = "/getSimulationStatus")
public class getSimulationStatus extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        //DTOSimulationId simulationId = gson.fromJson(reader, DTOSimulationId.class);
        DTOSimulationStatus status = new DTOSimulationStatus();
        String simulationId = request.getParameter("simulationId");
        try {

            status.setSimulationStatus(engine.getSimulationStatus(Integer.parseInt(simulationId)));
        } catch (Exception e) {
            status.getException().setException(e.getMessage());
        }

        String jsonRes = gson.toJson(status);
        response.getWriter().write(jsonRes);


    }
}
