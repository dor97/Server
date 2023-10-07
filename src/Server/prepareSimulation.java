package Server;


import DTO.DTOException;
import DTO.DTOPrepareSimulationData;
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

@WebServlet(name = "prepareSimulation", urlPatterns = "/prepareSimulation")
public class prepareSimulation extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = (Engine) getServletContext().getAttribute("engine");

        BufferedReader reader = request.getReader();

        Gson gson = new Gson();
        DTOPrepareSimulationData prepareSimulationData = gson.fromJson(reader, DTOPrepareSimulationData.class);

//        StringBuilder requestBody = new StringBuilder();
//        String line;
//        String approval;
//        Integer id;
//        List<String> strings = new ArrayList<>();
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//            strings.add(line);
//        }
        DTOException exception = new DTOException();
        try {
            engine._prepareSimulation(prepareSimulationData.getRequestId(), prepareSimulationData.getUserName(), prepareSimulationData.getEnvironmentsValues(), prepareSimulationData.getEntitiesPopulation());
        }catch (Exception e){
            exception.setException(e.getMessage());
        }

        String jsonRes = gson.toJson(exception);
        response.getWriter().write(jsonRes);
    }
}
