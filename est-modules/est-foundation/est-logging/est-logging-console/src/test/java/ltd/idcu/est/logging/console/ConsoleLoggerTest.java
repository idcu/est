package ltd.idcu.est.logging.console;

import ltd.idcu.est.logging.api.LogConfig;
import ltd.idcu.est.logging.api.LogLevel;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;

public class ConsoleLoggerTest {

    @Test
    public void testGetLoggerByName() {
        Logger logger = ConsoleLogs.getLogger("test.logger");
        Assertions.assertNotNull(logger);
        Assertions.assertEquals("test.logger", logger.getName());
    }

    @Test
    public void testGetLoggerByClass() {
        Logger logger = ConsoleLogs.getLogger(ConsoleLoggerTest.class);
        Assertions.assertNotNull(logger);
        Assertions.assertEquals(ConsoleLoggerTest.class.getName(), logger.getName());
    }

    @Test
    public void testGetLoggerWithConfig() {
        LogConfig config = LogConfig.defaultConfig().setLevel(LogLevel.DEBUG);
        Logger logger = ConsoleLogs.getLogger("test.logger", config);
        Assertions.assertNotNull(logger);
        Assertions.assertEquals(LogLevel.DEBUG, logger.getLevel());
    }

    @Test
    public void testGetLoggerWithLevel() {
        Logger logger = ConsoleLogs.getLogger("test.logger", LogLevel.WARN);
        Assertions.assertNotNull(logger);
        Assertions.assertEquals(LogLevel.WARN, logger.getLevel());
    }

    @Test
    public void testDebugLogger() {
        Logger logger = ConsoleLogs.debugLogger("test.logger");
        Assertions.assertNotNull(logger);
        Assertions.assertEquals(LogLevel.DEBUG, logger.getLevel());
    }

    @Test
    public void testTraceLogger() {
        Logger logger = ConsoleLogs.traceLogger("test.logger");
        Assertions.assertNotNull(logger);
        Assertions.assertEquals(LogLevel.TRACE, logger.getLevel());
    }

    @Test
    public void testLogInfo() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            LogConfig config = LogConfig.defaultConfig().addAppender(ConsoleLogs.stdoutAppender());
            Logger logger = ConsoleLogs.getLogger("test.logger", config);
            logger.info("Test info message");
            
            String output = outContent.toString();
            Assertions.assertTrue(output.contains("INFO"));
            Assertions.assertTrue(output.contains("Test info message"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testLogDebug() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            LogConfig config = LogConfig.defaultConfig()
                    .setLevel(LogLevel.DEBUG)
                    .addAppender(ConsoleLogs.stdoutAppender());
            Logger logger = ConsoleLogs.getLogger("test.logger", config);
            logger.debug("Test debug message");
            
            String output = outContent.toString();
            Assertions.assertTrue(output.contains("DEBUG"));
            Assertions.assertTrue(output.contains("Test debug message"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testLogWarn() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));
        
        try {
            LogConfig config = LogConfig.defaultConfig().addAppender(ConsoleLogs.stderrAppender());
            Logger logger = ConsoleLogs.getLogger("test.logger", config);
            logger.warn("Test warn message");
            
            String output = errContent.toString();
            Assertions.assertTrue(output.contains("WARN"));
            Assertions.assertTrue(output.contains("Test warn message"));
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    public void testLogError() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));
        
        try {
            LogConfig config = LogConfig.defaultConfig().addAppender(ConsoleLogs.stderrAppender());
            Logger logger = ConsoleLogs.getLogger("test.logger", config);
            logger.error("Test error message");
            
            String output = errContent.toString();
            Assertions.assertTrue(output.contains("ERROR"));
            Assertions.assertTrue(output.contains("Test error message"));
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    public void testLogTrace() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            LogConfig config = LogConfig.defaultConfig()
                    .setLevel(LogLevel.TRACE)
                    .addAppender(ConsoleLogs.stdoutAppender());
            Logger logger = ConsoleLogs.getLogger("test.logger", config);
            logger.trace("Test trace message");
            
            String output = outContent.toString();
            Assertions.assertTrue(output.contains("TRACE"));
            Assertions.assertTrue(output.contains("Test trace message"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testLogWithException() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));
        
        try {
            LogConfig config = LogConfig.defaultConfig().addAppender(ConsoleLogs.stderrAppender());
            Logger logger = ConsoleLogs.getLogger("test.logger", config);
            Exception exception = new RuntimeException("Test exception");
            logger.error("Test error with exception", exception);
            
            String output = errContent.toString();
            Assertions.assertTrue(output.contains("ERROR"));
            Assertions.assertTrue(output.contains("Test error with exception"));
            Assertions.assertTrue(output.contains("RuntimeException"));
            Assertions.assertTrue(output.contains("Test exception"));
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    public void testColoredFormatter() {
        ConsoleLogFormatter formatter = ConsoleLogs.coloredFormatter();
        Assertions.assertNotNull(formatter);
        Assertions.assertTrue(formatter.isColorEnabled());
    }

    @Test
    public void testPlainFormatter() {
        ConsoleLogFormatter formatter = ConsoleLogs.plainFormatter();
        Assertions.assertNotNull(formatter);
        Assertions.assertFalse(formatter.isColorEnabled());
    }

    @Test
    public void testCustomFormatter() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ConsoleLogFormatter formatter = ConsoleLogs.formatter(dateTimeFormatter, true);
        Assertions.assertNotNull(formatter);
        Assertions.assertTrue(formatter.isColorEnabled());
    }

    @Test
    public void testAppender() {
        ConsoleLogAppender appender = ConsoleLogs.appender();
        Assertions.assertNotNull(appender);
        Assertions.assertEquals("CONSOLE", appender.getName());
    }

    @Test
    public void testAppenderWithFormatter() {
        ConsoleLogFormatter formatter = ConsoleLogs.formatter();
        ConsoleLogAppender appender = ConsoleLogs.appender(formatter);
        Assertions.assertNotNull(appender);
    }

    @Test
    public void testStdoutAppender() {
        ConsoleLogAppender appender = ConsoleLogs.stdoutAppender();
        Assertions.assertNotNull(appender);
    }

    @Test
    public void testStderrAppender() {
        ConsoleLogAppender appender = ConsoleLogs.stderrAppender();
        Assertions.assertNotNull(appender);
    }

    @Test
    public void testBuilder() {
        Logger logger = ConsoleLogs.builder()
                .name("test.builder.logger")
                .level(LogLevel.DEBUG)
                .formatter(ConsoleLogs.plainFormatter())
                .build();
        
        Assertions.assertNotNull(logger);
        Assertions.assertEquals("test.builder.logger", logger.getName());
        Assertions.assertEquals(LogLevel.DEBUG, logger.getLevel());
    }

    @Test
    public void testBuilderWithStringLevel() {
        Logger logger = ConsoleLogs.builder()
                .name("test.builder.logger")
                .level("WARN")
                .build();
        
        Assertions.assertNotNull(logger);
        Assertions.assertEquals(LogLevel.WARN, logger.getLevel());
    }

    @Test
    public void testBuilderWithoutNameThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ConsoleLogs.builder().build();
        });
    }

    @Test
    public void testBuilderWithEmptyNameThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ConsoleLogs.builder().name("").build();
        });
    }

    @Test
    public void testSetAsDefault() {
        ConsoleLogs.setAsDefault();
        Logger logger = ConsoleLogs.getLogger("default.logger");
        Assertions.assertNotNull(logger);
    }

    @Test
    public void testSetAsDefaultWithConfig() {
        LogConfig config = LogConfig.defaultConfig().setLevel(LogLevel.DEBUG);
        ConsoleLogs.setAsDefault(config);
        Logger logger = ConsoleLogs.getLogger("default.config.logger");
        Assertions.assertNotNull(logger);
    }

    @Test
    public void testIsEnabledFor() {
        Logger logger = ConsoleLogs.getLogger("test.logger", LogLevel.INFO);
        Assertions.assertTrue(logger.isEnabledFor(LogLevel.INFO));
        Assertions.assertTrue(logger.isEnabledFor(LogLevel.WARN));
        Assertions.assertTrue(logger.isEnabledFor(LogLevel.ERROR));
        Assertions.assertFalse(logger.isEnabledFor(LogLevel.DEBUG));
        Assertions.assertFalse(logger.isEnabledFor(LogLevel.TRACE));
    }

    @Test
    public void testSetLevel() {
        Logger logger = ConsoleLogs.getLogger("test.logger");
        logger.setLevel(LogLevel.DEBUG);
        Assertions.assertEquals(LogLevel.DEBUG, logger.getLevel());
    }

    @Test
    public void testLogStats() {
        Logger logger = ConsoleLogs.getLogger("test.stats.logger");
        logger.info("Message 1");
        logger.info("Message 2");
        logger.warn("Warning 1");
        
        Assertions.assertEquals(2, logger.getStats().getInfoCount());
        Assertions.assertEquals(1, logger.getStats().getWarnCount());
        Assertions.assertEquals(0, logger.getStats().getErrorCount());
    }
}
