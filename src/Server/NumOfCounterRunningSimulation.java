package Server;

import DTO.DTONumOfCounterRunningSimulation;
import DTO.DTOPrepareSimulationData;
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

@WebServlet(name = "NumOfCounterRunningSimulation", urlPatterns = "/NumOfCounterRunningSimulation")
public class NumOfCounterRunningSimulation extends HttpServlet {

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
        //DTOSimulationName simulationName = gson.fromJson(reader, DTOSimulationName.class);
        String simulationName = request.getParameter("simulationName");

        DTONumOfCounterRunningSimulation numOfCounterRunningSimulation = new DTONumOfCounterRunningSimulation();
        Integer num;
        try {

            num = engine._getNumOfCounterRunningSimulation(simulationName);
            numOfCounterRunningSimulation.setNumOfRunningSimulation(num);
        }catch (Exception e){
            numOfCounterRunningSimulation.getException().setException(e.getMessage());
        }

        String jsonRes = gson.toJson(numOfCounterRunningSimulation);
        response.getWriter().write(jsonRes);


    }
}
