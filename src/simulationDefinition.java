import DTO.DTOException;
import DTO.DTOWorldDifenichanCollecen;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Set;
import Engine.*;

import com.google.gson.Gson;


@WebServlet(name = "SimulationDefinition", urlPatterns = "/simulationDefinition/WorldDifenichanCollecen")
public class simulationDefinition extends HttpServlet {

//    public void init() {
//        getServletContext().setAttribute("engin", new Engine());
//    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = (Engine) getServletContext().getAttribute("engine");

        DTOWorldDifenichanCollecen worldDifenichanCollecen = new DTOWorldDifenichanCollecen();
        String index = request.getParameter("index");
        try {
            worldDifenichanCollecen.setWorldDifenichanCollecen(engine._getWorldDifenichanCollecen(Integer.parseInt(index)));
        }catch (Exception e){
            worldDifenichanCollecen.getException().setException(e.getMessage());
        }

        Gson gson = new Gson();
        String jsonRes = gson.toJson(worldDifenichanCollecen);
        response.getWriter().write(jsonRes);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = (Engine) getServletContext().getAttribute("engine");
//        BufferedReader reader = request.getReader();
//        InputStream inputStream = request.getInputStream();
//        String line = reader.readLine();
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//        }
        InputStream inputStream = request.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        StringBuilder xmlContent = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            xmlContent.append(line.trim());
//        }
        DTOException exception = new DTOException();
        try {
            engine._loadSimulationDefinition(inputStream);
        }catch (Exception e){
            exception.setException(e.getMessage());
        }
        Gson gson = new Gson();
        String jsonRes = gson.toJson(exception);
        response.getWriter().write(jsonRes);

    }
}
