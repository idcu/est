package ltd.idcu.est.web;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.web.api.Session;
import ltd.idcu.est.web.api.SessionManager;

import java.util.concurrent.TimeUnit;

public class DefaultSessionManagerTest {

    @Test
    public void testCreateSession() {
        SessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        
        Assertions.assertNotNull(session);
        Assertions.assertNotNull(session.getId());
        Assertions.assertTrue(session.isValid());
    }

    @Test
    public void testGetSession() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        String sessionId = session.getId();
        
        Session retrievedSession = sessionManager.getSession(sessionId);
        Assertions.assertNotNull(retrievedSession);
        Assertions.assertEquals(sessionId, retrievedSession.getId());
    }

    @Test
    public void testGetSessionNonExistent() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.getSession("non-existent-id", false);
        Assertions.assertNull(session);
    }

    @Test
    public void testGetSessionCreateIfMissing() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.getSession("non-existent-id", true);
        Assertions.assertNotNull(session);
    }

    @Test
    public void testDestroySession() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        String sessionId = session.getId();
        
        sessionManager.destroySession(sessionId);
        
        Session retrievedSession = sessionManager.getSession(sessionId, false);
        Assertions.assertNull(retrievedSession);
    }

    @Test
    public void testDestroySessionBySession() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        String sessionId = session.getId();
        
        sessionManager.destroySession(session);
        
        Session retrievedSession = sessionManager.getSession(sessionId, false);
        Assertions.assertNull(retrievedSession);
    }

    @Test
    public void testHasSession() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        String sessionId = session.getId();
        
        Assertions.assertTrue(sessionManager.hasSession(sessionId));
    }

    @Test
    public void testGetAllSessions() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session1 = sessionManager.createSession();
        Session session2 = sessionManager.createSession();
        
        Assertions.assertEquals(2, sessionManager.getAllSessions().size());
    }

    @Test
    public void testGetActiveSessionCount() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.createSession();
        sessionManager.createSession();
        
        Assertions.assertEquals(2, sessionManager.getActiveSessionCount());
    }

    @Test
    public void testSetAndGetMaxInactiveInterval() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setMaxInactiveInterval(3600);
        
        Assertions.assertEquals(3600, sessionManager.getMaxInactiveInterval());
    }

    @Test
    public void testSetAndGetSessionTimeout() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionTimeout(60);
        
        Assertions.assertEquals(60, sessionManager.getSessionTimeout());
        Assertions.assertEquals(3600, sessionManager.getMaxInactiveInterval());
    }

    @Test
    public void testGenerateSessionId() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        String sessionId1 = sessionManager.generateSessionId();
        String sessionId2 = sessionManager.generateSessionId();
        
        Assertions.assertNotNull(sessionId1);
        Assertions.assertNotNull(sessionId2);
        Assertions.assertNotEquals(sessionId1, sessionId2);
    }

    @Test
    public void testIsValidSessionId() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        String validSessionId = sessionManager.generateSessionId();
        
        Assertions.assertTrue(sessionManager.isValidSessionId(validSessionId));
        Assertions.assertFalse(sessionManager.isValidSessionId(null));
        Assertions.assertFalse(sessionManager.isValidSessionId(""));
        Assertions.assertFalse(sessionManager.isValidSessionId("invalid-session-id"));
    }

    @Test
    public void testSessionAttributes() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        
        session.setAttribute("key1", "value1");
        session.setAttribute("key2", 123);
        
        Assertions.assertEquals("value1", session.getAttribute("key1"));
        Assertions.assertEquals(123, session.getAttribute("key2"));
        Assertions.assertNull(session.getAttribute("non-existent-key"));
    }

    @Test
    public void testSessionAttributeOrDefault() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        
        Assertions.assertEquals("default", session.getAttributeOrDefault("key", "default"));
        
        session.setAttribute("key", "value");
        Assertions.assertEquals("value", session.getAttributeOrDefault("key", "default"));
    }

    @Test
    public void testRemoveSessionAttribute() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        
        session.setAttribute("key", "value");
        session.removeAttribute("key");
        
        Assertions.assertNull(session.getAttribute("key"));
    }

    @Test
    public void testSessionInvalidate() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        
        session.invalidate();
        Assertions.assertFalse(session.isValid());
    }

    @Test
    public void testCleanExpiredSessions() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        Session session = sessionManager.createSession();
        session.setMaxInactiveInterval(1);
        
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        sessionManager.cleanExpiredSessions();
        
        Assertions.assertEquals(0, sessionManager.getActiveSessionCount());
    }

    @Test
    public void testCleanupTaskStartAndStop() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        
        Assertions.assertFalse(sessionManager.isCleanupStarted());
        
        sessionManager.startCleanupTask(100, TimeUnit.MILLISECONDS);
        Assertions.assertTrue(sessionManager.isCleanupStarted());
        
        sessionManager.stopCleanupTask();
        Assertions.assertFalse(sessionManager.isCleanupStarted());
    }

    @Test
    public void testCleanupTaskMultipleStart() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        
        sessionManager.startCleanupTask();
        boolean firstStarted = sessionManager.isCleanupStarted();
        
        sessionManager.startCleanupTask();
        boolean secondStarted = sessionManager.isCleanupStarted();
        
        Assertions.assertTrue(firstStarted);
        Assertions.assertTrue(secondStarted);
        
        sessionManager.stopCleanupTask();
    }

    @Test
    public void testGetSessionConfig() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        SessionManager.SessionConfig config = sessionManager.getSessionConfig();
        
        Assertions.assertNotNull(config);
        Assertions.assertEquals("JSESSIONID", config.getSessionCookieName());
    }
}
