package ltd.idcu.est.web;

import ltd.idcu.est.web.api.WebApplication;

public class Web {

    public static WebApplication create() {
        return new DefaultWebApplication("EST Application", "1.0.0");
    }

    public static WebApplication create(String name, String version) {
        return new DefaultWebApplication(name, version);
    }
}
