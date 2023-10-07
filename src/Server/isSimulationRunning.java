package Server;

import DTO.DTOIsSimulationRunning;
import DTO.DTORunningSimulationDetails;
import DTO.DTOSimulationId;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;


@WebServlet(name = "isSimulationRunning", urlPatterns = "/isSimulationRunning")
public class isSimulationRunning extends HttpServlet {
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
        String simulationId = request.getParameter("simulationId");
        DTOIsSimulationRunning isSimulationRunning = new DTOIsSimulationRunning();
        try {
            isSimulationRunning.setIsSimulationRunning(engine.isSimulationRunning(Integer.parseInt(simulationId)));
        }catch (Exception e){
            isSimulationRunning.getException().setException(e.getMessage());
        }

        String jsonRes = gson.toJson(isSimulationRunning);
        response.getWriter().write(jsonRes);


    }
}
