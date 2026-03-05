package ltd.idcu.est.web;

import ltd.idcu.est.web.api.*;

import java.util.Map;

public abstract class AbstractController implements Controller {

    protected Request request;
    protected Response response;

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public Response getResponse() {
        return response;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String render(String viewName, Object model) {
        if (response instanceof DefaultResponse) {
            DefaultResponse defaultResponse = (DefaultResponse) response;
            View.ViewResolver viewResolver = defaultResponse.getViewResolver();
            if (viewResolver != null) {
                View view = viewResolver.resolve(viewName);
                if (model != null) {
                    if (model instanceof Map) {
                        view.setModel((Map<String, Object>) model);
                    } else {
                        view.setModel(Map.of("model", model));
                    }
                }
                return view.render();
            }
        }
        return null;
    }
}
