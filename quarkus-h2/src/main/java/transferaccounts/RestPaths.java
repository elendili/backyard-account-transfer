package transferaccounts;

import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class RestPaths {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    private DbBean db;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<BigInteger> create(Account account) {
        logger.infov("Account is created: {0}", account);
        return Uni.createFrom().item(db.save(account));
    }

    @GET
    @Path("/{accountId}")
    public Uni<Account> get(@PathParam("accountId") BigInteger accountId) {
        logger.infov("Account is requested: {0}", accountId);
        return Uni.createFrom().optional(() -> db.findById(accountId));
    }

    @GET
    public Uni<List<Account>> get() {
        logger.infov("Accounts are requested");
        return Uni.createFrom().item(() -> db.findAll());
    }

}