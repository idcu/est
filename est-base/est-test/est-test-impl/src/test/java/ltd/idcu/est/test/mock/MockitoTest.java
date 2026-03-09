package ltd.idcu.est.test.mock;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class MockitoTest {

    public interface TestService {
        String getName();
        int add(int a, int b);
        void doSomething();
    }

    @Test
    public void testMockInterface() {
        TestService service = Mockito.mock(TestService.class);
        Assertions.assertNotNull(service);
    }

    @Test
    public void testMockToString() {
        TestService service = Mockito.mock(TestService.class);
        String toString = service.toString();
        Assertions.assertTrue(toString.contains("Mock for"));
        Assertions.assertTrue(toString.contains("TestService"));
    }

    @Test
    public void testDefaultReturnValue() {
        TestService service = Mockito.mock(TestService.class);
        Assertions.assertEquals(0, service.add(1, 2));
        Assertions.assertNull(service.getName());
    }

    @Test
    public void testVerify() {
        TestService service = Mockito.mock(TestService.class);
        service.getName();
        Mockito.verify(service).getName();
    }

    @Test
    public void testVerifyTimes() {
        TestService service = Mockito.mock(TestService.class);
        service.getName();
        service.getName();
        Mockito.verify(service, Mockito.times(2)).getName();
    }

    @Test
    public void testVerifyNever() {
        TestService service = Mockito.mock(TestService.class);
        Mockito.verify(service, Mockito.never()).getName();
    }

    @Test
    public void testVerifyOnce() {
        TestService service = Mockito.mock(TestService.class);
        service.getName();
        Mockito.verify(service, Mockito.once()).getName();
    }

    @Test
    public void testIsMock() {
        TestService service = Mockito.mock(TestService.class);
        Assertions.assertTrue(Mockito.isMock(service));
    }

    @Test
    public void testIsMockWithNormalObject() {
        TestService normalService = new TestService() {
            @Override
            public String getName() { return "normal"; }
            @Override
            public int add(int a, int b) { return a + b; }
            @Override
            public void doSomething() {}
        };
        Assertions.assertFalse(Mockito.isMock(normalService));
    }

    @Test
    public void testVoidMethod() {
        TestService service = Mockito.mock(TestService.class);
        service.doSomething();
        Mockito.verify(service).doSomething();
    }

    @Test
    public void testMockWithMultipleMethods() {
        TestService service = Mockito.mock(TestService.class);
        service.getName();
        service.add(1, 2);
        service.doSomething();
        
        Mockito.verify(service).getName();
        Mockito.verify(service).add(1, 2);
        Mockito.verify(service).doSomething();
    }

    @Test
    public void testVerifyAtLeast() {
        TestService service = Mockito.mock(TestService.class);
        service.getName();
        service.getName();
        service.getName();
        Mockito.verify(service, Mockito.atLeast(2)).getName();
    }

    @Test
    public void testVerifyAtMost() {
        TestService service = Mockito.mock(TestService.class);
        service.getName();
        service.getName();
        Mockito.verify(service, Mockito.atMost(3)).getName();
    }
}
