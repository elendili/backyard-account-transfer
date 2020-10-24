package transferaccounts;

import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.LogMessage;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.invoke.MethodHandles;
import java.sql.SQLException;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class RestPaths {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Inject
    EntityManager em;

    public RestPaths(){
        try {
            org.h2.tools.Server.createTcpServer("-tcpAllowOthers").start();
            logger.info("DB started");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

/*

    @GET
    @Path("/greeting/{name}")
    public Uni<String> greeting(@PathParam("name") String name) {
        return Uni.createFrom().item(name)
                .onItem().transform(n -> String.format("hello %s", name));
    }

    @GET
    public String hello() {
        return "hello";
    }
*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Uni<Integer> create(Account account) {
        logger.infov("Account is created: {0}",account);
        em.persist(account);
//        em.flush();
//        Account ac = em.find(Account.class, account.id);
        return Uni.createFrom().item(account.id);
    }

    @GET
    @Path("/{accountId}")
    @Transactional
    public Uni<Account> get(@PathParam("accountId") Integer accountId) {
        logger.infov("Account is requested: {0}", accountId);
        return Uni.createFrom().item(()->em.find(Account.class, accountId));
    }

}