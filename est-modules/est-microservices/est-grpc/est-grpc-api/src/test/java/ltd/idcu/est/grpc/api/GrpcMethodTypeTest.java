package ltd.idcu.est.grpc.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrpcMethodTypeTest {
    
    @Test
    public void testEnumValues() {
        GrpcMethodType[] values = GrpcMethodType.values();
        assertEquals(4, values.length);
        assertEquals(GrpcMethodType.UNARY, values[0]);
        assertEquals(GrpcMethodType.SERVER_STREAMING, values[1]);
        assertEquals(GrpcMethodType.CLIENT_STREAMING, values[2]);
        assertEquals(GrpcMethodType.BIDI_STREAMING, values[3]);
    }
    
    @Test
    public void testValueOf() {
        assertEquals(GrpcMethodType.UNARY, GrpcMethodType.valueOf("UNARY"));
        assertEquals(GrpcMethodType.SERVER_STREAMING, GrpcMethodType.valueOf("SERVER_STREAMING"));
        assertEquals(GrpcMethodType.CLIENT_STREAMING, GrpcMethodType.valueOf("CLIENT_STREAMING"));
        assertEquals(GrpcMethodType.BIDI_STREAMING, GrpcMethodType.valueOf("BIDI_STREAMING"));
    }
    
}
