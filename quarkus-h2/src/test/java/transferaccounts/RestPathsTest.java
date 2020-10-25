package transferaccounts;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigInteger;

@QuarkusTest
public class RestPathsTest {

    @Inject
    AgroalDataSource defaultDataSource;

    @Test
    public void post() {
        String jsonString = "{\"amount\":321,\"name\":\"hello\"}";
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonString)
                .when().post("/accounts")
                .then()
                .statusCode(200)
                .body(Matchers.matchesPattern("\\d+"));
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
    public void getKnown() {
        BigInteger id = SqlViaDataSourceTest.addInAccountTable(defaultDataSource, "(666, 'UU')");
//        em.persist(new Account("hi",1));
        RestAssured.given()
                .when().get("/accounts/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.equalTo("{\"amount\":666,\"id\":" + id + ",\"name\":\"UU\"}"));
    }

    @Test
    public void findAll() {
        BigInteger id1 = SqlViaDataSourceTest.addInAccountTable(defaultDataSource, "(333, 'A')");
        BigInteger id2 = SqlViaDataSourceTest.addInAccountTable(defaultDataSource, "(666, 'B')");
        RestAssured.given()
                .when().get("/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.equalTo(
                        "[{\"amount\":333,\"id\":" + id1 + ",\"name\":\"A\"},{\"amount\":666,\"id\":" + id2 + ",\"name\":\"B\"}]"));
    }
}