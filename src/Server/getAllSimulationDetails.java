package Server;

import DTO.DTOSimulationDetails;
import DTO.DTOSimulationName;
import DTO.DTOSimulationsDetails;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;


@WebServlet(name = "getAllSimulationDetails", urlPatterns = "/getAllSimulationsDetails")
public class getAllSimulationDetails extends HttpServlet {

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
        Gson gson = new Gson();
//        DTOSimulationName simulationName = gson.fromJson(reader, DTOSimulationName.class);
        DTOSimulationsDetails simulationsDetails = new DTOSimulationsDetails();
        try {
            simulationsDetails = engine.getSimulationsDetails();
        }catch (Exception e){
            simulationsDetails.getException().setException(e.getMessage());
        }

        String jsonRes = gson.toJson(simulationsDetails);
        response.getWriter().write(jsonRes);


    }
}
