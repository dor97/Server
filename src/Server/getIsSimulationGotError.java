package Server;

import DTO.DTOIsSimulationGotError;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "getIsSimulationGotError", urlPatterns = "/getIsSimulationGotError")
public class getIsSimulationGotError extends HttpServlet {

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
        DTOIsSimulationGotError isSimulationGotError = new DTOIsSimulationGotError();
        String simulationId = request.getParameter("simulationId");
        try {
//            DTOSimulationId simulationId = gson.fromJson(reader, DTOSimulationId.class);
            isSimulationGotError.setIsSimulationGotError(engine.isSimulationGotError(Integer.parseInt(simulationId)));
        } catch (Exception e) {
            isSimulationGotError.getException().setException(e.getMessage());
        }


        String jsonRes = gson.toJson(isSimulationGotError);
        response.getWriter().write(jsonRes);


    }
}
