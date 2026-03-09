package ltd.idcu.est.grpc.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrpcMethodTest {
    
    @Test
    public void testGrpcMethodAnnotation() {
        class TestService {
            @GrpcMethod(name = "testMethod", type = GrpcMethodType.UNARY)
            public void testMethod() {
            }
        }
        
        try {
            GrpcMethod annotation = TestService.class.getMethod("testMethod").getAnnotation(GrpcMethod.class);
            assertNotNull(annotation);
            assertEquals("testMethod", annotation.name());
            assertEquals(GrpcMethodType.UNARY, annotation.type());
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }
    
    @Test
    public void testGrpcMethodDefaultValues() {
        class TestService {
            @GrpcMethod
            public void defaultMethod() {
            }
        }
        
        try {
            GrpcMethod annotation = TestService.class.getMethod("defaultMethod").getAnnotation(GrpcMethod.class);
            assertNotNull(annotation);
            assertEquals("", annotation.name());
            assertEquals(GrpcMethodType.UNARY, annotation.type());
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }
    
    @Test
    public void testGrpcMethodTypes() {
        class TestService {
            @GrpcMethod(type = GrpcMethodType.SERVER_STREAMING)
            public void serverStreamingMethod() {
            }
            
            @GrpcMethod(type = GrpcMethodType.CLIENT_STREAMING)
            public void clientStreamingMethod() {
            }
            
            @GrpcMethod(type = GrpcMethodType.BIDI_STREAMING)
            public void bidiStreamingMethod() {
            }
        }
        
        try {
            assertEquals(GrpcMethodType.SERVER_STREAMING, 
                TestService.class.getMethod("serverStreamingMethod").getAnnotation(GrpcMethod.class).type());
            assertEquals(GrpcMethodType.CLIENT_STREAMING, 
                TestService.class.getMethod("clientStreamingMethod").getAnnotation(GrpcMethod.class).type());
            assertEquals(GrpcMethodType.BIDI_STREAMING, 
                TestService.class.getMethod("bidiStreamingMethod").getAnnotation(GrpcMethod.class).type());
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }
    
}
