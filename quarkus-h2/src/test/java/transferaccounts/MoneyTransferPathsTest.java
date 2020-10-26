package transferaccounts;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@QuarkusTest
public class MoneyTransferPathsTest {

    @Inject
    AgroalDataSource defaultDataSource;

    @Inject
    EntityManager em;

    @BeforeEach
    public void setup() {
//        em.createQuery("TRUNCATE TABLE Account").executeUpdate();
        SqlViaDataSourceTest.cleanAccounts(defaultDataSource);
    }

    @Test
    public void transfer() {
        BigInteger id1 = SqlViaDataSourceTest.addInAccountTable(defaultDataSource, "(100, 'A')");
        BigInteger id2 = SqlViaDataSourceTest.addInAccountTable(defaultDataSource, "(200, 'B')");
        String jsonString = "[{\"amount\":100,\"from\":\"" + id1 + "\",\"to\":\"" + id2 + "\"}]";
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonString)
                .when().post("/transfers")
                .then()
                .statusCode(200)
                .body(Matchers.equalTo("[\"Success\"]"));
        List<Account> list = em.createQuery("SELECT a from Account a").getResultList();
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals(300, list.get(0).amount.longValue());
        Assertions.assertEquals(0, list.get(1).amount.longValue());
        Assertions.assertEquals(BigDecimal.ZERO.toBigInteger(), em.find(Account.class, id1).amount.toBigInteger());
        Assertions.assertEquals(BigDecimal.valueOf(300).toBigInteger(), em.find(Account.class, id2).amount.toBigInteger());
    }

}