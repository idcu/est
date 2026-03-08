
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class FixEncoding {

    private static final List&lt;String&gt; SUPPORTED_EXTENSIONS = Arrays.asList(".md", ".java", ".xml", ".properties");
    private static final Charset TARGET_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        String rootDir = args.length &gt; 0 ? args[0] : System.getProperty("user.dir");
        System.out.println("Start fixing file encoding, root dir: " + rootDir);

        try {
            Files.walk(Paths.get(rootDir))
                .filter(Files::isRegularFile)
                .filter(path -&gt; {
                    String fileName = path.toString().toLowerCase();
                    return SUPPORTED_EXTENSIONS.stream().anyMatch(fileName::endsWith);
                })
                .filter(path -&gt; !path.toString().contains(".git"))
                .forEach(FixEncoding::fixFileEncoding);

            System.out.println("Encoding fix complete!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fixFileEncoding(Path path) {
        try {
            System.out.println("Processing: " + path);
            byte[] bytes = Files.readAllBytes(path);

            String content = null;

            try {
                content = new String(bytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
            }

            if (content != null &amp;&amp; (content.contains("\uFFFD") || content.contains("\u951F"))) {
                System.out.println("  Garbled detected, trying to fix...");

                try {
                    String testContent = new String(content.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    if (containsValidChinese(testContent)) {
                        content = testContent;
                        System.out.println("  Fixed using method 1 (UTF-8-&gt;ISO-8859-1-&gt;UTF-8)");
                    }
                } catch (Exception e) {
                }

                if (content.contains("\uFFFD") || content.contains("\u951F")) {
                    try {
                        Charset gbkCharset = Charset.forName("GBK");
                        String testContent = new String(bytes, gbkCharset);
                        if (containsValidChinese(testContent) &amp;&amp; !testContent.contains("\uFFFD")) {
                            content = testContent;
                            System.out.println("  Fixed using method 2 (GBK-&gt;UTF-8)");
                        }
                    } catch (Exception e) {
                    }
                }
            }

            if (content != null) {
                Files.write(path, content.getBytes(TARGET_CHARSET));
                System.out.println("  Fixed: " + path);
            }

        } catch (IOException e) {
            System.err.println("Failed to process: " + path);
            e.printStackTrace();
        }
    }

    private static boolean containsValidChinese(String content) {
        int chineseCount = 0;
        for (char c : content.toCharArray()) {
            if (c &gt;= 0x4E00 &amp;&amp; c &lt;= 0x9FFF) {
                chineseCount++;
            }
        }
        return chineseCount &gt;= 5;
    }
}

