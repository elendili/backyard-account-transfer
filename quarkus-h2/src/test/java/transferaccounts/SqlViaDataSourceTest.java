package transferaccounts;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@QuarkusTest
public class SqlViaDataSourceTest {

    @Inject
    AgroalDataSource defaultDataSource;

    public static List<Map<String, String>> resultSetToListOfMap(ResultSet rs) {
        List<Map<String, String>> out = new ArrayList<>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                Map<String, String> columns = new LinkedHashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    columns.put(metaData.getColumnLabel(i), String.valueOf(rs.getObject(i)));
                }
                out.add(columns);
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
        return out;
    }

    public static BigInteger addInAccountTable(AgroalDataSource defaultDataSource, String string) {
        try (Connection con = defaultDataSource.getConnection()) {
            PreparedStatement statement = con.prepareStatement("INSERT INTO Account (amount, name) VALUES " + string + ";\n", new String[]{"id"});
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return BigInteger.valueOf(rs.getInt(1));
            }
            return BigInteger.ZERO;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public static void cleanAccounts(AgroalDataSource defaultDataSource) {
        try (Connection con = defaultDataSource.getConnection()) {
            PreparedStatement statement = con.prepareStatement("TRUNCATE TABLE Account;\n");
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    List<Map<String, String>> getAccountTable() {
        try (Connection con = defaultDataSource.getConnection()) {
            return resultSetToListOfMap(con.prepareStatement("SELECT * FROM Account").executeQuery());
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @Test
    public void reviewOperations() {
        try (Connection con = defaultDataSource.getConnection()) {
            List<Map<String, String>> lm = resultSetToListOfMap(con.getMetaData().getTables(null, null, null, null));
            System.out.println("1. ==>" + lm);
            MatcherAssert.assertThat(lm.toString(), Matchers.containsString("ACCOUNT"));
            System.out.println("2. ==>" +
                    con.prepareStatement("INSERT INTO Account VALUES (2, 32, 'ZZ')").executeUpdate());
            System.out.println("3. ==>" +
                    resultSetToListOfMap(con.prepareStatement("SELECT * FROM Account").executeQuery()));
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @Test
    public void addGet() {
        addInAccountTable(defaultDataSource, "(32, 'ZZ')");
        MatcherAssert.assertThat(getAccountTable().toString(), Matchers.containsString("ZZ"));
    }
}