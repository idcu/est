package ltd.idcu.est.web;

import ltd.idcu.est.web.api.FormData;
import ltd.idcu.est.web.api.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultFormData implements FormData {

    private final Map<String, List<String>> parameters;
    private final Map<String, List<MultipartFile>> files;
    private final boolean multipart;

    public DefaultFormData() {
        this(false);
    }

    public DefaultFormData(boolean multipart) {
        this.parameters = new HashMap<>();
        this.files = new HashMap<>();
        this.multipart = multipart;
    }

    public void addParameter(String name, String value) {
        parameters.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
    }

    public void addFile(String name, MultipartFile file) {
        files.computeIfAbsent(name, k -> new ArrayList<>()).add(file);
    }

    @Override
    public String getParameter(String name) {
        List<String> values = parameters.get(name);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    @Override
    public List<String> getParameterValues(String name) {
        return parameters.get(name);
    }

    @Override
    public Map<String, List<String>> getParameters() {
        return new HashMap<>(parameters);
    }

    @Override
    public boolean hasParameter(String name) {
        return parameters.containsKey(name);
    }

    @Override
    public MultipartFile getFile(String name) {
        List<MultipartFile> fileList = files.get(name);
        return fileList != null && !fileList.isEmpty() ? fileList.get(0) : null;
    }

    @Override
    public List<MultipartFile> getFiles(String name) {
        return files.get(name);
    }

    @Override
    public Map<String, List<MultipartFile>> getFiles() {
        return new HashMap<>(files);
    }

    @Override
    public boolean hasFile(String name) {
        return files.containsKey(name);
    }

    @Override
    public boolean isMultipart() {
        return multipart;
    }
}
