package transferaccounts;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigInteger;

@QuarkusTest
public class AccountPathsTest {

    @Inject
    AgroalDataSource defaultDataSource;

    @BeforeEach
    public void setup(){
//        em.createQuery("TRUNCATE TABLE Account").executeUpdate();
        SqlViaDataSourceTest.cleanAccounts(defaultDataSource);
    }

    @Test
    public void post() {
        String jsonString = "[{\"amount\":321,\"name\":\"hello\"}]";
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonString)
                .when().post("/accounts")
                .then()
                .statusCode(200)
                .body(Matchers.matchesPattern("\\[\\d+\\]"));
    }

    @Test
    public void postAll() {
        String jsonString = "[{\"amount\":321,\"name\":\"a\"},{\"amount\":123,\"name\":\"b\"}]";
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonString)
                .when().post("/accounts")
                .then()
                .statusCode(200)
                .body(Matchers.matchesPattern("\\[(\\d,?)+\\]"));
    }

    @Test
    public void getUnknown() {
        RestAssured.given()
                .when().get("/accounts/100")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .body(Matchers.emptyString());
    }

    @Test
    public void getKnownUsingAgroalDataSource() {
        BigInteger id = SqlViaDataSourceTest.addInAccountTable(defaultDataSource, "(666, 'UU')");
        RestAssured.given()
                .when().get("/accounts/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.equalTo("{\"amount\":666.00,\"id\":" + id + ",\"name\":\"UU\"}"));
    }

    @Test
    public void findAllUsingAgroalDataSource() {
        BigInteger id1 = SqlViaDataSourceTest.addInAccountTable(defaultDataSource, "(333, 'A')");
        BigInteger id2 = SqlViaDataSourceTest.addInAccountTable(defaultDataSource, "(666, 'B')");
        RestAssured.given()
                .when().get("/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.equalTo(
                        "[{\"amount\":333.00,\"id\":" + id1 + ",\"name\":\"A\"},{\"amount\":666.00,\"id\":" + id2 + ",\"name\":\"B\"}]"));
    }
}