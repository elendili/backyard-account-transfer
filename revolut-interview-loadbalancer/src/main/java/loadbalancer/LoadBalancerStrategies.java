package loadbalancer;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancerStrategies {
    static class RandomStrategy implements GetterStrategy {
        private final ThreadLocalRandom random = ThreadLocalRandom.current();

        @Override
        public Instance apply(Set<Instance> set) {
            int i = random.nextInt(set.size());
            Instance[] x = set.toArray(new Instance[0]);
            return x[i];
        }
    }


    static class RoundRobinStrategy implements GetterStrategy {
        AtomicInteger roundRobinCounter = new AtomicInteger();

        @Override
        public Instance apply(Set<Instance> set) {
            Instance[] x = set.toArray(new Instance[0]);
            return x[roundRobinCounter.getAndIncrement() % set.size()];
        }
    }

    static GetterStrategy createRoundRobinGetterStrategy() {
        return new RoundRobinStrategy();
    }
    static GetterStrategy createRandomGetterStrategy() {
        return new RoundRobinStrategy();
    }
}
