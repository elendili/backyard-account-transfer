package transferaccounts;

import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Status;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.List;

@Path("/transfers")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class MoneyTransferPaths {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    private DbBean db;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<List<String>> transfer(List<MoneyTransfer> transfers) {
        logger.infov("Money transfer");
        return Uni.createFrom().item(db.transfer(transfers));
    }


}