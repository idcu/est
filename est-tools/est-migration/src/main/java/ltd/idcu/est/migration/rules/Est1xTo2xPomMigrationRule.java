package ltd.idcu.est.migration.rules;

import ltd.idcu.est.migration.AbstractMigrationRule;
import ltd.idcu.est.migration.MigrationConfig;
import ltd.idcu.est.migration.MigrationResult;

public class Est1xTo2xPomMigrationRule extends AbstractMigrationRule {

    public Est1xTo2xPomMigrationRule() {
        super("EST 1.X to 2.0 POM Migration", "Migrates EST 1.X pom.xml dependencies to EST 2.0", 90);
    }

    @Override
    public boolean canApply(String content, MigrationConfig config) {
        return content.contains("<artifactId>est-") && 
               (content.contains("est-features-") || content.contains("est-foundation"));
    }

    @Override
    public String apply(String content, MigrationConfig config, MigrationResult result) {
        String migrated = content;

        migrated = migrateDependencies(migrated, result);

        return migrated;
    }

    private String migrateDependencies(String content, MigrationResult result) {
        String migrated = content;

        migrated = migrated.replaceAll(
            "<artifactId>est-features-cache-api</artifactId>", 
            "<artifactId>est-cache-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-cache-memory</artifactId>", 
            "<artifactId>est-cache-memory</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-cache-file</artifactId>", 
            "<artifactId>est-cache-file</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-cache-redis</artifactId>", 
            "<artifactId>est-cache-redis</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-logging-api</artifactId>", 
            "<artifactId>est-logging-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-logging-console</artifactId>", 
            "<artifactId>est-logging-console</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-logging-file</artifactId>", 
            "<artifactId>est-logging-file</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-data-api</artifactId>", 
            "<artifactId>est-data-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-data-jdbc</artifactId>", 
            "<artifactId>est-data-jdbc</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-data-memory</artifactId>", 
            "<artifactId>est-data-memory</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-data-redis</artifactId>", 
            "<artifactId>est-data-redis</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-data-mongodb</artifactId>", 
            "<artifactId>est-data-mongodb</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-security-api</artifactId>", 
            "<artifactId>est-security-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-security-basic</artifactId>", 
            "<artifactId>est-security-basic</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-security-jwt</artifactId>", 
            "<artifactId>est-security-jwt</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-security-oauth2</artifactId>", 
            "<artifactId>est-security-oauth2</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-api</artifactId>", 
            "<artifactId>est-messaging-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-local</artifactId>", 
            "<artifactId>est-messaging-local</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-kafka</artifactId>", 
            "<artifactId>est-messaging-kafka</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-redis</artifactId>", 
            "<artifactId>est-messaging-redis</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-activemq</artifactId>", 
            "<artifactId>est-messaging-activemq</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-amqp</artifactId>", 
            "<artifactId>est-messaging-amqp</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-mqtt</artifactId>", 
            "<artifactId>est-messaging-mqtt</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-nats</artifactId>", 
            "<artifactId>est-messaging-nats</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-pulsar</artifactId>", 
            "<artifactId>est-messaging-pulsar</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-rocketmq</artifactId>", 
            "<artifactId>est-messaging-rocketmq</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-stomp</artifactId>", 
            "<artifactId>est-messaging-stomp</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-websocket</artifactId>", 
            "<artifactId>est-messaging-websocket</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-messaging-zeromq</artifactId>", 
            "<artifactId>est-messaging-zeromq</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-monitor-api</artifactId>", 
            "<artifactId>est-monitor-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-monitor-jvm</artifactId>", 
            "<artifactId>est-monitor-jvm</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-monitor-system</artifactId>", 
            "<artifactId>est-monitor-system</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-scheduler-api</artifactId>", 
            "<artifactId>est-scheduler-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-scheduler-cron</artifactId>", 
            "<artifactId>est-scheduler-cron</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-scheduler-fixed</artifactId>", 
            "<artifactId>est-scheduler-fixed</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-event-api</artifactId>", 
            "<artifactId>est-event-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-event-local</artifactId>", 
            "<artifactId>est-event-local</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-event-async</artifactId>", 
            "<artifactId>est-event-async</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-circuitbreaker-api</artifactId>", 
            "<artifactId>est-circuitbreaker-api</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-discovery-api</artifactId>", 
            "<artifactId>est-discovery-api</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-config-api</artifactId>", 
            "<artifactId>est-config-api</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-performance-api</artifactId>", 
            "<artifactId>est-performance-api</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-hotreload-api</artifactId>", 
            "<artifactId>est-hotreload-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-hotreload-impl</artifactId>", 
            "<artifactId>est-hotreload-impl</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-features-ai-api</artifactId>", 
            "<artifactId>est-ai-api</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-features-ai-impl</artifactId>", 
            "<artifactId>est-ai-impl</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-foundation</artifactId>", 
            "<artifactId>est-base</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-foundation/est-utils</artifactId>", 
            "<artifactId>est-base/est-utils</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-foundation/est-collection</artifactId>", 
            "<artifactId>est-base/est-collection</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-foundation/est-patterns</artifactId>", 
            "<artifactId>est-base/est-patterns</artifactId>"
        );
        migrated = migrated.replaceAll(
            "<artifactId>est-foundation/est-test</artifactId>", 
            "<artifactId>est-base/est-test</artifactId>"
        );

        migrated = migrated.replaceAll(
            "<artifactId>est-microservices-gateway</artifactId>", 
            "<artifactId>est-gateway</artifactId>"
        );

        if (!migrated.equals(content)) {
            result.addWarning("Updated pom.xml dependencies from EST 1.X to 2.0");
        }

        return migrated;
    }
}
