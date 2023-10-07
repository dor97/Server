import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import Engine.Engine;
import com.google.gson.Gson;


@WebServlet(name = "EngineServer", urlPatterns = "/engineServer")
public class server extends HttpServlet {

    private Engine engine;
    private String message;

    public void init() {
        message = "Hello World!";
        engine = new Engine();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.getWriter().println();

        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");

    }


}
