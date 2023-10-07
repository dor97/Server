package Server;

import DTO.DTOFinishedSimulation;
import DTO.DTOFinishedSimulationForAdmin;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "updateAdminNewlyFinishedSimulation", urlPatterns = "/updateAdminNewlyFinishedSimulation")
public class updateAdminNewlyFinishedSimulation extends HttpServlet {

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
        DTOFinishedSimulationForAdmin finishedSimulationForAdmin = new DTOFinishedSimulationForAdmin();
        try {
            finishedSimulationForAdmin.setSimulationIdAndUserNames(engine.updateNewlyFinishedSimulation(Integer.parseInt(Index)));
        } catch (Exception e) {
            finishedSimulationForAdmin.getException().setException(e.getMessage());
        }

        Gson gson = new Gson();
        String jsonRes = gson.toJson(finishedSimulationForAdmin);
        response.getWriter().write(jsonRes);


    }
}
