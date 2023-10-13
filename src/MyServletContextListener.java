import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import Engine.Engine;
import jakarta.servlet.annotation.WebListener;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@WebListener
public class MyServletContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        Engine engine = new Engine();
        servletContext.setAttribute("engine", engine);
        servletContext.setAttribute("isThereAdmin", false);
        servletContext.setAttribute("users", new HashSet<String>());
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Engine engine = (Engine)servletContextEvent.getServletContext().getAttribute("engine");
        engine.disposeOfThreadPool();

        // Cleanup code if needed
    }
}
