package loadbalancer;

import java.net.URL;
import java.util.Objects;

public class Instance {
    final URL iPAddress;

    public Instance(URL iPAddress) {
        this.iPAddress = iPAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return Objects.equals(iPAddress, instance.iPAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iPAddress);
    }

    @Override
    public String toString() {
        return iPAddress.toString();
    }
}
