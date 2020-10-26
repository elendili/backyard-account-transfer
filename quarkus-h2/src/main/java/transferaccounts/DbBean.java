package transferaccounts;

import io.quarkus.runtime.Startup;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Startup
@Singleton
@NamedQuery(
        name = "findAllAccounts",
        query = "SELECT c FROM Account"
)
public class DbBean {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    private EntityManager em;

    @Transactional
    public BigInteger save(Account account) {
        em.persist(account);
        return account.id;
    }

    @Transactional
    public Optional<Account> findById(BigInteger accountId) {
        return Optional.ofNullable(em.find(Account.class, accountId));
    }

    @Transactional
    public List<Account> findAll() {
        TypedQuery<Account> query =
                em.createQuery("SELECT a FROM Account a", Account.class);
        return query.getResultList();
    }
}
