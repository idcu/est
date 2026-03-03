package ltd.idcu.est.web.api;

import java.util.Collection;
import java.util.Map;

public interface RestController extends Controller {

    default String json(Object object) {
        getResponse().json(object);
        return null;
    }

    default String json(String json) {
        getResponse().json(json);
        return null;
    }

    default String json(Collection<?> collection) {
        getResponse().json(collection);
        return null;
    }

    default String json(Map<String, ?> map) {
        getResponse().json(map);
        return null;
    }

    default String xml(String xml) {
        getResponse().xml(xml);
        return null;
    }

    default String html(String html) {
        getResponse().html(html);
        return null;
    }

    default String text(String text) {
        getResponse().text(text);
        return null;
    }

    default String ok() {
        getResponse().ok();
        return null;
    }

    default String created() {
        getResponse().created();
        return null;
    }

    default String noContent() {
        getResponse().noContent();
        return null;
    }

    default String badRequest() {
        getResponse().badRequest();
        return null;
    }

    default String unauthorized() {
        getResponse().unauthorized();
        return null;
    }

    default String forbidden() {
        getResponse().forbidden();
        return null;
    }

    default String notFound() {
        getResponse().notFound();
        return null;
    }

    default String internalServerError() {
        getResponse().internalServerError();
        return null;
    }

    default String error(HttpStatus status) {
        getResponse().sendError(status);
        return null;
    }

    default String error(HttpStatus status, String message) {
        getResponse().sendError(status, message);
        return null;
    }

    default String error(int code) {
        getResponse().sendError(code);
        return null;
    }

    default String error(int code, String message) {
        getResponse().sendError(code, message);
        return null;
    }

    default String success(Object data) {
        return json(Map.of(
            "success", true,
            "data", data
        ));
    }

    default String success(String message, Object data) {
        return json(Map.of(
            "success", true,
            "message", message,
            "data", data
        ));
    }

    default String fail(String message) {
        return json(Map.of(
            "success", false,
            "message", message
        ));
    }

    default String fail(int code, String message) {
        return json(Map.of(
            "success", false,
            "code", code,
            "message", message
        ));
    }

    default String fail(String code, String message) {
        return json(Map.of(
            "success", false,
            "code", code,
            "message", message
        ));
    }

    default String result(int code, String message, Object data) {
        return json(Map.of(
            "code", code,
            "message", message,
            "data", data
        ));
    }

    default String result(String code, String message, Object data) {
        return json(Map.of(
            "code", code,
            "message", message,
            "data", data
        ));
    }

    default String page(Object list, long total, int pageNum, int pageSize) {
        return json(Map.of(
            "success", true,
            "data", Map.of(
                "list", list,
                "total", total,
                "pageNum", pageNum,
                "pageSize", pageSize
            )
        ));
    }
}
