package Server;

import DTO.DTOFinishedSimulationForAdmin;
import DTO.DTOPostRunPrepareSimulationData;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet(name = "getSimulationPrepareData", urlPatterns = "/getSimulationPrepareData")
public class getSimulationPrepareData extends HttpServlet {

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
        //String userName = request.getParameter("userName");
        String simulationId = request.getParameter("simulationId");
        DTOPostRunPrepareSimulationData postRunPrepareSimulationData = new DTOPostRunPrepareSimulationData();
        try {
            postRunPrepareSimulationData = engine.getSimulationPrepareData(Integer.parseInt(simulationId));
        } catch (Exception e) {
            postRunPrepareSimulationData.getException().setException(e.getMessage());
        }

        Gson gson = new Gson();
        String jsonRes = gson.toJson(postRunPrepareSimulationData);
        response.getWriter().write(jsonRes);


    }
}
