package transferaccounts;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.lang.invoke.MethodHandles;
import java.sql.SQLException;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public void onStart(@Observes StartupEvent ev) {
        logger.info("The application is starting... " + ev);
        try {
            org.h2.tools.Server.createTcpServer("-tcpAllowOthers").start();
            logger.info("DB started");
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public void onStop(@Observes ShutdownEvent ev) {
        logger.info("The application is stopping... " + ev);
    }

}