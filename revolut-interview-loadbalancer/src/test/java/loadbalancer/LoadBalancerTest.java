package loadbalancer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.Test;

class LoadBalancerTest {



    @Test
    public void testExceeding() throws MalformedURLException {
        LoadBalancer lb = new LoadBalancer(10, LoadBalancerStrategies.createRandomGetterStrategy());
        for (int i = 0; i < 10; i++) {
            lb.add(new Instance(new URL("http://" + i + ".com")));
        }
        IllegalArgumentException t = assertThrows(IllegalArgumentException.class, () -> lb.add(new Instance(new URL("http://11.com"))));
        assertEquals("no more instances than " + 10, t.getMessage());
    }

    @Test
    public void testSameInstance() throws MalformedURLException {
        LoadBalancer lb = new LoadBalancer(10, LoadBalancerStrategies.createRandomGetterStrategy());
        lb.add(new Instance(new URL("http://1.com")));
        IllegalArgumentException t = assertThrows(IllegalArgumentException.class, () -> lb.add(new Instance(new URL("http://1.com"))));
        assertEquals("similar instance already registered http://1.com", t.getMessage());
    }

    @Test
    public void npe() {
        LoadBalancer lb = new LoadBalancer(10, LoadBalancerStrategies.createRandomGetterStrategy());
        NullPointerException t = assertThrows(NullPointerException.class, () -> lb.add(null));
        assertEquals("instance should be defined", t.getMessage());
    }

    @Test
    public void emptyTest() {
        LoadBalancer lb = new LoadBalancer(10, LoadBalancerStrategies.createRandomGetterStrategy());
        NullPointerException t = assertThrows(NullPointerException.class, () -> lb.add(null));
        assertEquals("instance should be defined", t.getMessage());
    }

    @Test
    public void getRandomWith1Test() throws MalformedURLException {
        LoadBalancer lb = new LoadBalancer(10, LoadBalancerStrategies.createRandomGetterStrategy());
        Instance input = new Instance(new URL("http://1.com"));
        lb.add(input);
        assertEquals(input,lb.get());
    }


    @Test
    public void getRoundRobinTest() throws MalformedURLException {
        LoadBalancer lb = new LoadBalancer(2, LoadBalancerStrategies.createRoundRobinGetterStrategy());
        Instance input1 = new Instance(new URL("http://1.com"));
        Instance input2 = new Instance(new URL("http://2.com"));
        lb.add(input1);
        lb.add(input2);
        assertEquals(input1,lb.get());
        assertEquals(input2,lb.get());
        assertEquals(input1,lb.get());
        assertEquals(input2,lb.get());
        assertEquals(input1,lb.get());
        assertEquals(input2,lb.get());
    }

}