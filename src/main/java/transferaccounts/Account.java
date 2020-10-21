package transferaccounts;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Account {
    @Id
    public String name;
    public long amount;

    public Account(){
        this("first",100);
    }

    @JsonbCreator
    public Account(
            @JsonbProperty("name") String name,
            @JsonbProperty("amount") long amount
    ) {
        this.name = name;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return amount == account.amount &&
                Objects.equals(name, account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount);
    }
}
