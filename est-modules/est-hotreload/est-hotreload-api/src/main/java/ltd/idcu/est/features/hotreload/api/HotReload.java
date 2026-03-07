package ltd.idcu.est.features.hotreload.api;

public class HotReload {
    private static HotReloadService defaultService;

    public static HotReloadService create() {
        try {
            Class<?> clazz = Class.forName("ltd.idcu.est.features.hotreload.impl.DefaultHotReloadService");
            return (HotReloadService) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HotReloadService", e);
        }
    }

    public static synchronized HotReloadService getDefault() {
        if (defaultService == null) {
            defaultService = create();
        }
        return defaultService;
    }

    public static synchronized void setDefault(HotReloadService service) {
        defaultService = service;
    }
}
