package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Collections;

public final class CollectionInitializer {

    private static volatile boolean initialized = false;

    static {
        init();
    }

    private CollectionInitializer() {
    }

    public static void init() {
        if (!initialized) {
            synchronized (CollectionInitializer.class) {
                if (!initialized) {
                    Collections.setProvider(new DefaultCollectionProvider());
                    initialized = true;
                }
            }
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
