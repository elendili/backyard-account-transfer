package transferaccounts;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import java.math.BigDecimal;
import java.math.BigInteger;

public class MoneyTransfer {
    public BigInteger from;
    public BigInteger to;
    public BigDecimal amount;

    @JsonbCreator
    public MoneyTransfer(
            @JsonbProperty("from") BigInteger from,
            @JsonbProperty("to") BigInteger to,
            @JsonbProperty("amount") BigDecimal amount
    ) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", amount=" + amount +
                '}';
    }
}
