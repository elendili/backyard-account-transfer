package transferaccounts;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@QuarkusTest
public class DbTestViaEntityManagerTest {

    @Inject
    EntityManager em;

    @Test
    @Transactional
    public void addGet() {
        Account ac = JsonbBuilder.create().fromJson("{\"amount\":321,\"name\":\"hello\"}", Account.class);
        em.persist(ac);
        List<Account> actual = em.createQuery("SELECT a FROM Account a", Account.class).getResultList();
        MatcherAssert.assertThat(actual, Matchers.contains(ac));
    }

}