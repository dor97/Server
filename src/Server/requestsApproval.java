package Server;


import DTO.DTOException;
import DTO.DTOPrepareSimulationData;
import DTO.DTORequestsApproval;
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

@WebServlet(name = "requestsApproval", urlPatterns = "/simulationDefinition/requestsApproval")
public class requestsApproval extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = (Engine) getServletContext().getAttribute("engine");

        BufferedReader reader = request.getReader();

        Gson gson = new Gson();
        DTORequestsApproval requestsApproval = gson.fromJson(reader, DTORequestsApproval.class);


//        StringBuilder requestBody = new StringBuilder();
//        String line;
//        String approval;
//        Integer id;
//        List<String> strings = new ArrayList<>();
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//            strings.add(line);
//        }
//
//        approval = strings.get(0);
        DTOException exception = new DTOException();
        try {
//            id = Integer.parseInt(strings.get(1));
            if (requestsApproval.getRequest().equals("approve")){
                engine._approveSimulation(requestsApproval.getId());
            }else if(requestsApproval.getRequest().equals("denied")){
                engine._denySimulation(requestsApproval.getId());
            }
        }catch (Exception e){
            exception.setException(e.getMessage());
        }

        String jsonRes = gson.toJson(exception);
        response.getWriter().write(jsonRes);
    }
}
