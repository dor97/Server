package Server;

import DTO.DTOAllRequestData;
import DTO.DTONumOfCounterRunningSimulation;
import DTO.DTORequestData;
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


@WebServlet(name = "GetSystemData", urlPatterns = "/GetSystemData")
public class GetSystemData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = (Engine) getServletContext().getAttribute("engine");

//        BufferedReader reader = request.getReader();
//        String name = reader.readLine();

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

        DTOAllRequestData systemData = new DTOAllRequestData();
        try {
            systemData.setRequestDataList(engine._getSystemData());
        }catch (Exception e){
            systemData.getException().setException(e.getMessage());
        }

        Gson gson = new Gson();
        String jsonRes = gson.toJson(systemData);
        response.getWriter().write(jsonRes);


    }
}
