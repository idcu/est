package ltd.idcu.est.data.jdbc;

public class SqlFormatter {
    
    private static final String[] KEYWORDS = {
        "SELECT", "INSERT", "UPDATE", "DELETE", "FROM", "WHERE", "AND", "OR",
        "JOIN", "INNER", "LEFT", "RIGHT", "FULL", "OUTER", "ON", "GROUP", "BY",
        "HAVING", "ORDER", "LIMIT", "OFFSET", "VALUES", "SET", "INTO", "CREATE",
        "TABLE", "ALTER", "DROP", "INDEX", "PRIMARY", "KEY", "FOREIGN", "REFERENCES",
        "UNIQUE", "NOT", "NULL", "DEFAULT", "AUTO_INCREMENT", "AUTOINCREMENT",
        "EXISTS", "IN", "BETWEEN", "LIKE", "IS", "ASC", "DESC", "UNION", "ALL",
        "DISTINCT", "AS", "CASE", "WHEN", "THEN", "ELSE", "END"
    };
    
    public static String format(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return sql;
        }
        
        String formatted = sql.trim();
        
        formatted = formatted.replaceAll("\\s+", " ");
        
        for (String keyword : KEYWORDS) {
            String pattern = "\\b" + keyword.toLowerCase() + "\\b";
            formatted = formatted.replaceAll(pattern, keyword);
        }
        
        StringBuilder sb = new StringBuilder();
        String indent = "";
        String[] tokens = formatted.split(" ");
        
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            
            if (isStartOfNewClause(token)) {
                sb.append("\n");
                sb.append(indent);
            }
            
            sb.append(token);
            
            if (i < tokens.length - 1) {
                sb.append(" ");
            }
            
            if (token.equals("(")) {
                indent += "  ";
            } else if (token.equals(")")) {
                indent = indent.length() >= 2 ? indent.substring(0, indent.length() - 2) : "";
            }
        }
        
        return sb.toString().trim();
    }
    
    private static boolean isStartOfNewClause(String token) {
        String[] newClauseKeywords = {
            "SELECT", "INSERT", "UPDATE", "DELETE", "FROM", "WHERE", "AND", "OR",
            "JOIN", "INNER", "LEFT", "RIGHT", "FULL", "OUTER", "ON", "GROUP", "BY",
            "HAVING", "ORDER", "LIMIT", "OFFSET", "VALUES", "SET", "INTO", "CREATE",
            "TABLE", "ALTER", "DROP", "INDEX"
        };
        
        for (String keyword : newClauseKeywords) {
            if (token.equalsIgnoreCase(keyword)) {
                return true;
            }
        }
        
        return false;
    }
}
