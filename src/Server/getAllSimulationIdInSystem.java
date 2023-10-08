package Server;

import DTO.DTOAllSimulationId;
import DTO.DTOAllSimulationStatus;
import DTO.DTOFinishedSimulationForAdmin;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "getAllSimulationIdInSystem", urlPatterns = "/getAllSimulationIdInSystem")
public class getAllSimulationIdInSystem extends HttpServlet {

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
        String Index = request.getParameter("index");
        DTOAllSimulationId allSimulationId = new DTOAllSimulationId();
        try {
            allSimulationId.setAllSimulationId(engine.getAllSimulationIdInSystem(Integer.parseInt(Index)));
        } catch (Exception e) {
            allSimulationId.getException().setException(e.getMessage());
        }

        Gson gson = new Gson();
        String jsonRes = gson.toJson(allSimulationId);
        response.getWriter().write(jsonRes);


    }
}
