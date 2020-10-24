package transferaccounts;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.gradle.internal.UncheckedException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@QuarkusTest
public class RestPathsTest {

    @Test
    public void post() {
        String jsonString = "{\"amount\":321,\"name\":\"hello\"}";
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonString)
                .when().post("/accounts")
                .then()
                .statusCode(200)
                .body(CoreMatchers.equalTo("1"));
//                .body(CoreMatchers.equalTo(jsonString));
    }

    @Test
    public void getUnknown() {
        RestAssured.given()
                .when().get("/accounts/100")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .body(Matchers.emptyString());
    }

    @Inject
    AgroalDataSource defaultDataSource;

    @Test
    public void getKnown() {
        SqlViaDataSourceTest.addInAccountTable(defaultDataSource,"(5, 666, 'UU')");
//        em.persist(new Account("hi",1));
        RestAssured.given()
                .when().get("/accounts/5")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.equalTo("{\"amount\":666,\"id\":5,\"name\":\"UU\"}"));
    }
}