package loadbalancer;

import java.util.Set;
import java.util.function.Function;

public interface GetterStrategy extends Function<Set<Instance>,Instance> {
}
