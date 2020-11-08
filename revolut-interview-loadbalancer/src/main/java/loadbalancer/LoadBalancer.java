package loadbalancer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LoadBalancer {
    private final int maxAmount;
    public Set<Instance> set = new HashSet<>();
    private final GetterStrategy getter;
    public LoadBalancer(Integer maxAmount, GetterStrategy getter){
        this.maxAmount = Objects.requireNonNull(maxAmount);
        this.getter = Objects.requireNonNull(getter);
    }

    public void add(final Instance instance) {
        Objects.requireNonNull(instance, "instance should be defined");
        if (set.size() >= maxAmount) {
            throw new IllegalArgumentException("no more instances than " + maxAmount);
        }
        if (set.contains(instance)) {
            throw new IllegalArgumentException("similar instance already registered " + instance);
        }
        set.add(instance);
    }



    public Instance get() {
        if(set.isEmpty()){
            throw new IllegalArgumentException("no added instances");
        }
        return getter.apply(set);
    }

}
