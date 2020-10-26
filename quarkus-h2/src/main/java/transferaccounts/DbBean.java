package transferaccounts;

import com.arjuna.ats.jta.TransactionManager;
import io.quarkus.runtime.Startup;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<BigInteger> saveAll(List<Account> accounts) {
//        em.getTransaction().begin(); // doesn' work
        List<BigInteger> out = new ArrayList<>();
        for (int i = 0; i < accounts.size(); i++) {
            Account ac = accounts.get(i);
            em.persist(ac);
            out.add(ac.id);
            if ((i % 1000) == 0) { // speed optimization
//                em.getTransaction().commit();
                em.clear();
//                em.getTransaction().begin();
            }
        }
//        em.getTransaction().commit();
        return out;
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

    @Transactional
    public String transfer(MoneyTransfer transfer) {
        Account from = em.find(Account.class,transfer.from);
        Account to = em.find(Account.class,transfer.to);
        from.amount = from.amount.subtract(transfer.amount);
        to.amount = to.amount.add(transfer.amount);
        em.createQuery("UPDATE Account SET amount = "+to.amount+" WHERE id="+to.id).executeUpdate();
        em.createQuery("UPDATE Account SET amount = "+from.amount+" WHERE id="+from.id).executeUpdate();
//        em.persist(from);
//        em.persist(to);
//        em.flush();
//        em.clear();
        return "Success";
    }

    public List<String> transfer(List<MoneyTransfer> transfers) {
        return transfers.stream().map(
                this::transfer
        ).collect(Collectors.toList());
    }
}
