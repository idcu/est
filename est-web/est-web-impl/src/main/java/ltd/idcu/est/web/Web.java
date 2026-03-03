package ltd.idcu.est.web;

import ltd.idcu.est.web.api.Controller;
import ltd.idcu.est.web.api.Middleware;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.web.api.RestController;

public class Web {

    public static Web create() {
        return new DefaultWebApplication("EST Application", "1.0.0");
    }

    public static Web create(String name, String version) {
        return new DefaultWebApplication(name, version);
    }
}
