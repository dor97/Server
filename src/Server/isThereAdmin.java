package Server;

import DTO.DTOIsSimulationRunning;
import DTO.DTOIsThereAdmin;
import Engine.Engine;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "isThereAdmin", urlPatterns = "/isThereAdmin")
public class isThereAdmin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        DTOIsThereAdmin isThereAdmin = new DTOIsThereAdmin();
        isThereAdmin.setAdminExist(true);
        try {
            synchronized (getServletContext()) {
                if(!(Boolean) getServletContext().getAttribute("isThereAdmin")){
                    isThereAdmin.setAdminExist(false);
                    getServletContext().setAttribute("isThereAdmin", true);
                }
            }
        }catch (Exception e){
            isThereAdmin.getException().setException(e.getMessage());
        }
        Gson gson = new Gson();
        String jsonRes = gson.toJson(isThereAdmin);
        response.getWriter().write(jsonRes);


    }
}
