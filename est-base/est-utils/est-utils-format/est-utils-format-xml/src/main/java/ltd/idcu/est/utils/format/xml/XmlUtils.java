package ltd.idcu.est.utils.format.xml;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class XmlUtils {

    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String INDENT = "  ";

    private XmlUtils() {
    }

    public static String toXml(XmlNode node) {
        return toXml(node, true);
    }

    public static String toXml(XmlNode node, boolean includeDeclaration) {
        StringBuilder sb = new StringBuilder();
        if (includeDeclaration) {
            sb.append(XML_DECLARATION).append("\n");
        }
        toXml(node, sb, 0);
        return sb.toString();
    }

    public static String toXml(XmlNode node, boolean includeDeclaration, String indent) {
        StringBuilder sb = new StringBuilder();
        if (includeDeclaration) {
            sb.append(XML_DECLARATION).append("\n");
        }
        toXml(node, sb, 0, indent);
        return sb.toString();
    }

    private static void toXml(XmlNode node, StringBuilder sb, int level) {
        toXml(node, sb, level, INDENT);
    }

    private static void toXml(XmlNode node, StringBuilder sb, int level, String indent) {
        String indentStr = indent.repeat(level);

        sb.append(indentStr).append("<").append(node.getName());

        for (Map.Entry<String, String> attr : node.getAttributes().entrySet()) {
            sb.append(" ").append(attr.getKey()).append("=\"").append(escapeXml(attr.getValue())).append("\"");
        }

        List<XmlNode> children = node.getChildren();
        String textContent = node.getTextContent();

        if (children.isEmpty() && (textContent == null || textContent.isEmpty())) {
            sb.append("/>\n");
        } else if (textContent != null && !textContent.isEmpty() && children.isEmpty()) {
            sb.append(">").append(escapeXml(textContent)).append("</").append(node.getName()).append(">\n");
        } else {
            sb.append(">\n");

            if (textContent != null && !textContent.isEmpty()) {
                sb.append(indentStr).append(indent).append(escapeXml(textContent)).append("\n");
            }

            for (XmlNode child : children) {
                toXml(child, sb, level + 1, indent);
            }

            sb.append(indentStr).append("</").append(node.getName()).append(">\n");
        }
    }

    public static XmlNode parse(String xml) {
        if (xml == null || xml.trim().isEmpty()) {
            return null;
        }
        XmlParser parser = new XmlParser(xml.trim());
        return parser.parse();
    }

    public static String escapeXml(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '<' -> sb.append("&lt;");
                case '>' -> sb.append("&gt;");
                case '&' -> sb.append("&amp;");
                case '"' -> sb.append("&quot;");
                case '\'' -> sb.append("&apos;");
                default -> {
                    if (c < ' ') {
                        sb.append("&#").append((int) c).append(";");
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String unescapeXml(String str) {
        if (str == null) {
            return null;
        }
        String result = str.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&apos;", "'");
        
        java.util.regex.Pattern decimalPattern = java.util.regex.Pattern.compile("&#(\\d+);");
        java.util.regex.Matcher decimalMatcher = decimalPattern.matcher(result);
        StringBuffer sb1 = new StringBuffer();
        while (decimalMatcher.find()) {
            decimalMatcher.appendReplacement(sb1, String.valueOf((char) Integer.parseInt(decimalMatcher.group(1))));
        }
        decimalMatcher.appendTail(sb1);
        
        java.util.regex.Pattern hexPattern = java.util.regex.Pattern.compile("&#x([0-9a-fA-F]+);");
        java.util.regex.Matcher hexMatcher = hexPattern.matcher(sb1.toString());
        StringBuffer sb2 = new StringBuffer();
        while (hexMatcher.find()) {
            hexMatcher.appendReplacement(sb2, String.valueOf((char) Integer.parseInt(hexMatcher.group(1), 16)));
        }
        hexMatcher.appendTail(sb2);
        
        return sb2.toString();
    }

    public static boolean isValidXml(String xml) {
        if (xml == null || xml.trim().isEmpty()) {
            return false;
        }
        try {
            parse(xml);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String compact(String xml) {
        if (xml == null) {
            return null;
        }
        return xml.replaceAll(">\\s+<", "><").trim();
    }

    public static String format(String xml) {
        return format(xml, INDENT);
    }

    public static String format(String xml, String indent) {
        if (xml == null) {
            return null;
        }
        XmlNode node = parse(xml);
        if (node == null) {
            return xml;
        }
        return toXml(node, xml.trim().startsWith("<?xml"), indent);
    }

    public static XmlNode createElement(String name) {
        return new XmlNode(name);
    }

    public static XmlNode createElement(String name, String textContent) {
        return new XmlNode(name, textContent);
    }

    public static XmlNode createElement(String name, Map<String, String> attributes) {
        XmlNode node = new XmlNode(name);
        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            node.setAttribute(attr.getKey(), attr.getValue());
        }
        return node;
    }

    public static XmlNode createElement(String name, String textContent, Map<String, String> attributes) {
        XmlNode node = new XmlNode(name, textContent);
        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            node.setAttribute(attr.getKey(), attr.getValue());
        }
        return node;
    }

    public static class XmlNode {
        private final String name;
        private String textContent;
        private final Map<String, String> attributes;
        private final List<XmlNode> children;
        private XmlNode parent;

        public XmlNode(String name) {
            this.name = name;
            this.attributes = new LinkedHashMap<>();
            this.children = new ArrayList<>();
        }

        public XmlNode(String name, String textContent) {
            this(name);
            this.textContent = textContent;
        }

        public String getName() {
            return name;
        }

        public String getTextContent() {
            return textContent;
        }

        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }

        public Map<String, String> getAttributes() {
            return Collections.unmodifiableMap(attributes);
        }

        public String getAttribute(String name) {
            return attributes.get(name);
        }

        public void setAttribute(String name, String value) {
            attributes.put(name, value);
        }

        public void removeAttribute(String name) {
            attributes.remove(name);
        }

        public boolean hasAttribute(String name) {
            return attributes.containsKey(name);
        }

        public List<XmlNode> getChildren() {
            return Collections.unmodifiableList(children);
        }

        public XmlNode addChild(XmlNode child) {
            if (child != null) {
                child.parent = this;
                children.add(child);
            }
            return this;
        }

        public XmlNode addChild(String name) {
            return addChild(new XmlNode(name));
        }

        public XmlNode addChild(String name, String textContent) {
            return addChild(new XmlNode(name, textContent));
        }

        public XmlNode removeChild(XmlNode child) {
            if (child != null && children.remove(child)) {
                child.parent = null;
            }
            return this;
        }

        public XmlNode removeChild(int index) {
            if (index >= 0 && index < children.size()) {
                XmlNode child = children.remove(index);
                child.parent = null;
                return child;
            }
            return null;
        }

        public XmlNode getParent() {
            return parent;
        }

        public List<XmlNode> getChildrenByName(String name) {
            List<XmlNode> result = new ArrayList<>();
            for (XmlNode child : children) {
                if (child.getName().equals(name)) {
                    result.add(child);
                }
            }
            return result;
        }

        public XmlNode getFirstChildByName(String name) {
            for (XmlNode child : children) {
                if (child.getName().equals(name)) {
                    return child;
                }
            }
            return null;
        }

        public String getChildTextContent(String name) {
            XmlNode child = getFirstChildByName(name);
            return child != null ? child.getTextContent() : null;
        }

        public String getChildTextContent(String name, String defaultValue) {
            String content = getChildTextContent(name);
            return content != null ? content : defaultValue;
        }

        public boolean hasChildren() {
            return !children.isEmpty();
        }

        public boolean hasTextContent() {
            return textContent != null && !textContent.isEmpty();
        }

        @Override
        public String toString() {
            return toXml(this);
        }
    }

    private static class XmlParser {
        private final String xml;
        private int pos;

        XmlParser(String xml) {
            this.xml = xml;
            this.pos = 0;
        }

        XmlNode parse() {
            skipWhitespace();
            skipDeclaration();
            skipComments();
            return parseElement();
        }

        private void skipDeclaration() {
            if (pos < xml.length() && xml.startsWith("<?xml", pos)) {
                int end = xml.indexOf("?>", pos);
                if (end != -1) {
                    pos = end + 2;
                    skipWhitespace();
                }
            }
        }

        private void skipComments() {
            while (pos < xml.length() && xml.startsWith("<!--", pos)) {
                int end = xml.indexOf("-->", pos);
                if (end != -1) {
                    pos = end + 3;
                    skipWhitespace();
                } else {
                    break;
                }
            }
        }

        private void skipCDATA() {
            if (pos < xml.length() && xml.startsWith("<![CDATA[", pos)) {
                int end = xml.indexOf("]]>", pos);
                if (end != -1) {
                    pos = end + 3;
                }
            }
        }

        private XmlNode parseElement() {
            skipWhitespace();
            if (pos >= xml.length() || xml.charAt(pos) != '<') {
                return null;
            }

            pos++;
            String name = parseName();
            if (name == null || name.isEmpty()) {
                return null;
            }

            XmlNode node = new XmlNode(name);

            skipWhitespace();

            while (pos < xml.length() && xml.charAt(pos) != '>' && xml.charAt(pos) != '/') {
                String attrName = parseName();
                if (attrName == null || attrName.isEmpty()) {
                    break;
                }
                skipWhitespace();
                if (pos < xml.length() && xml.charAt(pos) == '=') {
                    pos++;
                    skipWhitespace();
                    String attrValue = parseAttributeValue();
                    node.setAttribute(attrName, attrValue);
                }
                skipWhitespace();
            }

            if (pos < xml.length() && xml.charAt(pos) == '/') {
                pos++;
                if (pos < xml.length() && xml.charAt(pos) == '>') {
                    pos++;
                }
                return node;
            }

            if (pos < xml.length() && xml.charAt(pos) == '>') {
                pos++;
            }

            StringBuilder textBuilder = new StringBuilder();
            while (pos < xml.length()) {
                skipWhitespace();

                if (xml.startsWith("</", pos)) {
                    break;
                }

                if (xml.startsWith("<!--", pos)) {
                    skipComments();
                    continue;
                }

                if (xml.startsWith("<![CDATA[", pos)) {
                    int start = pos + 9;
                    int end = xml.indexOf("]]>", start);
                    if (end != -1) {
                        textBuilder.append(xml, start, end);
                        pos = end + 3;
                    } else {
                        pos++;
                    }
                    continue;
                }

                if (xml.charAt(pos) == '<') {
                    if (textBuilder.length() > 0) {
                        if (node.hasChildren()) {
                            textBuilder.setLength(0);
                        }
                    }
                    XmlNode child = parseElement();
                    if (child != null) {
                        node.addChild(child);
                    }
                } else {
                    String text = parseText();
                    if (text != null && !text.isEmpty()) {
                        textBuilder.append(text);
                    }
                }
            }

            if (textBuilder.length() > 0 && !node.hasChildren()) {
                node.setTextContent(unescapeXml(textBuilder.toString().trim()));
            }

            if (xml.startsWith("</", pos)) {
                pos += 2;
                parseName();
                skipWhitespace();
                if (pos < xml.length() && xml.charAt(pos) == '>') {
                    pos++;
                }
            }

            return node;
        }

        private String parseName() {
            int start = pos;
            while (pos < xml.length()) {
                char c = xml.charAt(pos);
                if (Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == ':' || c == '.') {
                    pos++;
                } else {
                    break;
                }
            }
            return pos > start ? xml.substring(start, pos) : null;
        }

        private String parseAttributeValue() {
            if (pos >= xml.length()) {
                return "";
            }
            char quote = xml.charAt(pos);
            if (quote != '"' && quote != '\'') {
                return "";
            }
            pos++;
            int start = pos;
            while (pos < xml.length() && xml.charAt(pos) != quote) {
                pos++;
            }
            String value = xml.substring(start, pos);
            if (pos < xml.length()) {
                pos++;
            }
            return unescapeXml(value);
        }

        private String parseText() {
            int start = pos;
            while (pos < xml.length() && xml.charAt(pos) != '<') {
                pos++;
            }
            String text = xml.substring(start, pos).trim();
            return text.isEmpty() ? null : unescapeXml(text);
        }

        private void skipWhitespace() {
            while (pos < xml.length() && Character.isWhitespace(xml.charAt(pos))) {
                pos++;
            }
        }
    }
}
