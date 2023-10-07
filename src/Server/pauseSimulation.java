package Server;

import DTO.DTOException;
import DTO.DTONumOfCounterRunningSimulation;
import DTO.DTOSimulationId;
import DTO.DTOSimulationName;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;


@WebServlet(name = "pauseSimulation", urlPatterns = "/pauseSimulation")
public class pauseSimulation extends HttpServlet {

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
        //DTOSimulationId simulationId = gson.fromJson(reader, DTOSimulationId.class);
        String simulationId = request.getParameter("simulationId");

        DTOException exception = new DTOException();
        try {
            engine.pauseSimulation(Integer.parseInt(simulationId));
        }catch (Exception e){
            exception.setException(e.getMessage());
        }

        String jsonRes = gson.toJson(exception);
        response.getWriter().write(jsonRes);


    }
}
