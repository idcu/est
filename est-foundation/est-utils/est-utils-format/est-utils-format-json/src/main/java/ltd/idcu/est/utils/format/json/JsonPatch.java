package ltd.idcu.est.utils.format.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonPatch {

    private final List<PatchOperation> operations;

    public JsonPatch() {
        this.operations = new ArrayList<>();
    }

    public JsonPatch(List<PatchOperation> operations) {
        this.operations = new ArrayList<>(operations);
    }

    public JsonPatch add(String path, JsonNode value) {
        operations.add(new PatchOperation("add", path, null, value));
        return this;
    }

    public JsonPatch remove(String path) {
        operations.add(new PatchOperation("remove", path, null, null));
        return this;
    }

    public JsonPatch replace(String path, JsonNode value) {
        operations.add(new PatchOperation("replace", path, null, value));
        return this;
    }

    public JsonPatch move(String from, String path) {
        operations.add(new PatchOperation("move", path, from, null));
        return this;
    }

    public JsonPatch copy(String from, String path) {
        operations.add(new PatchOperation("copy", path, from, null));
        return this;
    }

    public JsonPatch test(String path, JsonNode value) {
        operations.add(new PatchOperation("test", path, null, value));
        return this;
    }

    public List<PatchOperation> getOperations() {
        return new ArrayList<>(operations);
    }

    public JsonNode apply(JsonNode target) {
        JsonNode result = deepCopy(target);
        for (PatchOperation op : operations) {
            result = applyOperation(result, op);
        }
        return result;
    }

    private JsonNode applyOperation(JsonNode target, PatchOperation op) {
        switch (op.op) {
            case "add":
                return add(target, op.path, op.value);
            case "remove":
                return remove(target, op.path);
            case "replace":
                return replace(target, op.path, op.value);
            case "move":
                return move(target, op.from, op.path);
            case "copy":
                return copy(target, op.from, op.path);
            case "test":
                test(target, op.path, op.value);
                return target;
            default:
                throw new IllegalArgumentException("Unknown operation: " + op.op);
        }
    }

    private JsonNode deepCopy(JsonNode node) {
        if (node == null || node.isNull()) {
            return NullNode.getInstance();
        }
        switch (node.getNodeType()) {
            case OBJECT:
                ObjectNode obj = new ObjectNode();
                for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    obj.set(entry.getKey(), deepCopy(entry.getValue()));
                }
                return obj;
            case ARRAY:
                ArrayNode arr = new ArrayNode();
                for (Iterator<JsonNode> it = node.elements(); it.hasNext(); ) {
                    arr.add(deepCopy(it.next()));
                }
                return arr;
            default:
                return node;
        }
    }

    private JsonNode add(JsonNode target, String path, JsonNode value) {
        String[] tokens = parsePath(path);
        return addAtPath(target, tokens, 0, value);
    }

    private JsonNode addAtPath(JsonNode node, String[] tokens, int index, JsonNode value) {
        if (index == tokens.length - 1) {
            String token = tokens[index];
            if (node.isObject()) {
                ObjectNode obj = (ObjectNode) node;
                obj.set(token, value);
                return obj;
            } else if (node.isArray()) {
                ArrayNode arr = (ArrayNode) node;
                int pos;
                if ("-".equals(token)) {
                    pos = arr.size();
                } else {
                    pos = Integer.parseInt(token);
                }
                if (pos < 0 || pos > arr.size()) {
                    throw new IndexOutOfBoundsException("Invalid array index: " + pos);
                }
                ArrayNode newArr = new ArrayNode();
                for (int i = 0; i < pos; i++) {
                    newArr.add(arr.get(i));
                }
                newArr.add(value);
                for (int i = pos; i < arr.size(); i++) {
                    newArr.add(arr.get(i));
                }
                return newArr;
            }
            throw new IllegalArgumentException("Cannot add to non-container node");
        } else {
            String token = tokens[index];
            JsonNode child = node.get(token);
            if (child == null) {
                throw new IllegalArgumentException("Path not found: " + token);
            }
            JsonNode newChild = addAtPath(child, tokens, index + 1, value);
            if (node.isObject()) {
                ObjectNode obj = new ObjectNode();
                for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    if (entry.getKey().equals(token)) {
                        obj.set(entry.getKey(), newChild);
                    } else {
                        obj.set(entry.getKey(), entry.getValue());
                    }
                }
                return obj;
            } else if (node.isArray()) {
                ArrayNode arr = new ArrayNode();
                int pos = Integer.parseInt(token);
                for (int i = 0; i < node.size(); i++) {
                    if (i == pos) {
                        arr.add(newChild);
                    } else {
                        arr.add(node.get(i));
                    }
                }
                return arr;
            }
            throw new IllegalArgumentException("Invalid node type");
        }
    }

    private JsonNode remove(JsonNode target, String path) {
        String[] tokens = parsePath(path);
        return removeAtPath(target, tokens, 0);
    }

    private JsonNode removeAtPath(JsonNode node, String[] tokens, int index) {
        if (index == tokens.length - 1) {
            String token = tokens[index];
            if (node.isObject()) {
                ObjectNode obj = new ObjectNode();
                for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    if (!entry.getKey().equals(token)) {
                        obj.set(entry.getKey(), entry.getValue());
                    }
                }
                return obj;
            } else if (node.isArray()) {
                ArrayNode arr = (ArrayNode) node;
                int pos = Integer.parseInt(token);
                if (pos < 0 || pos >= arr.size()) {
                    throw new IndexOutOfBoundsException("Invalid array index: " + pos);
                }
                ArrayNode newArr = new ArrayNode();
                for (int i = 0; i < arr.size(); i++) {
                    if (i != pos) {
                        newArr.add(arr.get(i));
                    }
                }
                return newArr;
            }
            throw new IllegalArgumentException("Cannot remove from non-container node");
        } else {
            String token = tokens[index];
            JsonNode child = node.get(token);
            if (child == null) {
                throw new IllegalArgumentException("Path not found: " + token);
            }
            JsonNode newChild = removeAtPath(child, tokens, index + 1);
            if (node.isObject()) {
                ObjectNode obj = new ObjectNode();
                for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    if (entry.getKey().equals(token)) {
                        obj.set(entry.getKey(), newChild);
                    } else {
                        obj.set(entry.getKey(), entry.getValue());
                    }
                }
                return obj;
            } else if (node.isArray()) {
                ArrayNode arr = new ArrayNode();
                int pos = Integer.parseInt(token);
                for (int i = 0; i < node.size(); i++) {
                    if (i == pos) {
                        arr.add(newChild);
                    } else {
                        arr.add(node.get(i));
                    }
                }
                return arr;
            }
            throw new IllegalArgumentException("Invalid node type");
        }
    }

    private JsonNode replace(JsonNode target, String path, JsonNode value) {
        JsonNode removed = remove(target, path);
        return add(removed, path, value);
    }

    private JsonNode move(JsonNode target, String from, String path) {
        JsonNode fromNode = getNodeAtPath(target, parsePath(from));
        JsonNode removed = remove(target, from);
        return add(removed, path, fromNode);
    }

    private JsonNode copy(JsonNode target, String from, String path) {
        JsonNode fromNode = getNodeAtPath(target, parsePath(from));
        return add(target, path, deepCopy(fromNode));
    }

    private void test(JsonNode target, String path, JsonNode value) {
        JsonNode node = getNodeAtPath(target, parsePath(path));
        if (!nodeEquals(node, value)) {
            throw new IllegalArgumentException("Test failed: values not equal at path " + path);
        }
    }

    private boolean nodeEquals(JsonNode a, JsonNode b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.getNodeType() != b.getNodeType()) return false;
        
        switch (a.getNodeType()) {
            case OBJECT:
                if (a.size() != b.size()) return false;
                for (Iterator<Map.Entry<String, JsonNode>> it = a.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    JsonNode bVal = b.get(entry.getKey());
                    if (!nodeEquals(entry.getValue(), bVal)) return false;
                }
                return true;
            case ARRAY:
                if (a.size() != b.size()) return false;
                for (int i = 0; i < a.size(); i++) {
                    if (!nodeEquals(a.get(i), b.get(i))) return false;
                }
                return true;
            default:
                return Objects.equals(a.asText(), b.asText());
        }
    }

    private JsonNode getNodeAtPath(JsonNode node, String[] tokens) {
        JsonNode current = node;
        for (String token : tokens) {
            current = current.get(token);
            if (current == null) {
                throw new IllegalArgumentException("Path not found: " + token);
            }
        }
        return current;
    }

    private String[] parsePath(String path) {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must start with /");
        }
        String[] parts = path.substring(1).split("/");
        String[] result = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = parts[i].replace("~1", "/").replace("~0", "~");
        }
        return result;
    }

    public String toJson() {
        ArrayNode arr = new ArrayNode();
        for (PatchOperation op : operations) {
            ObjectNode obj = new ObjectNode();
            obj.set("op", new TextNode(op.op));
            obj.set("path", new TextNode(op.path));
            if (op.from != null) {
                obj.set("from", new TextNode(op.from));
            }
            if (op.value != null) {
                obj.set("value", op.value);
            }
            arr.add(obj);
        }
        return arr.toJson();
    }

    public static JsonPatch fromJson(String json) {
        JsonNode node = JsonUtils.parseTree(json);
        if (!node.isArray()) {
            throw new IllegalArgumentException("JSON Patch must be an array");
        }
        List<PatchOperation> ops = new ArrayList<>();
        for (Iterator<JsonNode> it = node.elements(); it.hasNext(); ) {
            JsonNode opNode = it.next();
            if (!opNode.isObject()) {
                throw new IllegalArgumentException("Each patch operation must be an object");
            }
            String op = opNode.get("op").asText();
            String path = opNode.get("path").asText();
            String from = opNode.has("from") ? opNode.get("from").asText() : null;
            JsonNode value = opNode.has("value") ? opNode.get("value") : null;
            ops.add(new PatchOperation(op, path, from, value));
        }
        return new JsonPatch(ops);
    }

    public static class PatchOperation {
        public final String op;
        public final String path;
        public final String from;
        public final JsonNode value;

        public PatchOperation(String op, String path, String from, JsonNode value) {
            this.op = op;
            this.path = path;
            this.from = from;
            this.value = value;
        }
    }
}
