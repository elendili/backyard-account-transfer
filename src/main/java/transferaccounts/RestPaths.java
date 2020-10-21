package transferaccounts;

import io.agroal.api.AgroalDataSource;
import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.ConcurrentHashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.invoke.MethodHandles;
import java.util.Set;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestPaths {
    @Inject
    EntityManager em;

    private static final Logger LOG = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    Set<Account> set = new ConcurrentHashSet<>();

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

    @POST
    @Transactional
    public Uni<Account> create(Account account) {
        set.add(account);
        LOG.info("Account is created: {}",account);
//        em.persist(account);
        return Uni.createFrom().item(account);
    }
}