package ltd.idcu.est.codecli.web;

public class ApiResponse {
    
    private boolean success;
    private Object data;
    private String message;
    private long timestamp;
    
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public static ApiResponse success(Object data) {
        ApiResponse response = new ApiResponse();
        response.success = true;
        response.data = data;
        return response;
    }
    
    public static ApiResponse success(Object data, String message) {
        ApiResponse response = new ApiResponse();
        response.success = true;
        response.data = data;
        response.message = message;
        return response;
    }
    
    public static ApiResponse error(String message) {
        ApiResponse response = new ApiResponse();
        response.success = false;
        response.message = message;
        return response;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"success\":").append(success).append(",");
        if (data != null) {
            sb.append("\"data\":").append(escapeJson(data.toString())).append(",");
        }
        if (message != null) {
            sb.append("\"message\":\"").append(escapeJson(message)).append("\",");
        }
        sb.append("\"timestamp\":").append(timestamp);
        sb.append("}");
        return sb.toString();
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
