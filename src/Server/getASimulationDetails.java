package Server;

import DTO.DTODataForReRun;
import DTO.DTOSimulationDetails;
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


@WebServlet(name = "getASimulationDetails", urlPatterns = "/getASimulationDetails")
public class getASimulationDetails extends HttpServlet {

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
//        DTOSimulationName simulationName = gson.fromJson(reader, DTOSimulationName.class);
        String simulationName = request.getParameter("simulationName");
        DTOSimulationDetails simulationDetails = new DTOSimulationDetails();
        try {
            simulationDetails = engine.getSimulationDetails(simulationName);
        }catch (Exception e){
            simulationDetails.getException().setException(e.getMessage());
        }

        String jsonRes = gson.toJson(simulationDetails);
        response.getWriter().write(jsonRes);


    }
}
