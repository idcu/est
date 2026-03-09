package ltd.idcu.est.grpc.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrpcServiceTest {
    
    @Test
    public void testGrpcServiceAnnotation() {
        @GrpcService(name = "TestService", version = "1.0.0", description = "Test gRPC service")
        class TestServiceImpl {
        }
        
        GrpcService annotation = TestServiceImpl.class.getAnnotation(GrpcService.class);
        assertNotNull(annotation);
        assertEquals("TestService", annotation.name());
        assertEquals("1.0.0", annotation.version());
        assertEquals("Test gRPC service", annotation.description());
    }
    
    @Test
    public void testGrpcServiceDefaultValues() {
        @GrpcService
        class DefaultService {
        }
        
        GrpcService annotation = DefaultService.class.getAnnotation(GrpcService.class);
        assertNotNull(annotation);
        assertEquals("", annotation.name());
        assertEquals("1.0.0", annotation.version());
        assertEquals("", annotation.description());
    }
    
}
