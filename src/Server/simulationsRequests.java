package Server;


import DTO.*;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "simulationsRequests", urlPatterns = "/simulationDefinition/simulationsRequests")
public class simulationsRequests extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = (Engine) getServletContext().getAttribute("engine");
        BufferedReader reader = request.getReader();

        Gson gson = new Gson();
        //DTOUserName userName = gson.fromJson(reader, DTOUserName.class);
        String userName = request.getParameter("userName");


        DTOAllSimulationApprovementManager allSimulationApprovementManager = new DTOAllSimulationApprovementManager();
        try {
            if (userName.equals("admin")) {
                allSimulationApprovementManager.setManager(engine._getApprovementManager());
            } else {
                allSimulationApprovementManager.setManager(engine._getApprovementManager(userName));
            }
        }catch (Exception e){
            allSimulationApprovementManager.getException().setException(e.getMessage());
        }

        String jsonRes = gson.toJson(allSimulationApprovementManager);
        response.getWriter().write(jsonRes);

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Engine engine = (Engine) getServletContext().getAttribute("engine");
        BufferedReader reader = request.getReader();

        Gson gson = new Gson();
        DTOAskToRunASimulation askToRunASimulation = gson.fromJson(reader, DTOAskToRunASimulation.class);

//        StringBuilder requestBody = new StringBuilder();
//        String line;
//        List<String> strings = new ArrayList<>();
//        String simulationName;
//        String userName;
//        Integer amountToRun;
//        Integer ticks;
//        Integer sec;
//        Integer id = -1;
//
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//            strings.add(line);
//        }
//        simulationName = strings.get(0);
//        userName = "user";
        DTORequestId requestId = new DTORequestId();
        try {
//            amountToRun = Integer.parseInt(strings.get(1));
//            ticks = Integer.parseInt(strings.get(2));
//            sec = Integer.parseInt(strings.get(3));

            if(askToRunASimulation.getTicks() == null || askToRunASimulation.getTicks() <= 0){
                askToRunASimulation.setTicks(null);
            }
            if (askToRunASimulation.getSec() == null || askToRunASimulation.getSec() <= 0){
                askToRunASimulation.setSec(null);
            }
            requestId.setRequestId(engine._askToRunASimulation(askToRunASimulation.getSimulationName(), askToRunASimulation.getUserName(), askToRunASimulation.getAmountToRun(), askToRunASimulation.getTicks(), askToRunASimulation.getSec()));

        } catch (Exception e) {
            requestId.getException().setException(e.getMessage());
        }

        //Gson gson = new Gson();
        String jsonRes = gson.toJson(requestId);
        response.getWriter().write(jsonRes);

    }

}
