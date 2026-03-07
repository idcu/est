package ltd.idcu.est.migration.rules;

import ltd.idcu.est.migration.AbstractMigrationRule;
import ltd.idcu.est.migration.MigrationConfig;
import ltd.idcu.est.migration.MigrationResult;

import java.util.HashMap;
import java.util.Map;

public class SpringBootAnnotationMigrationRule extends AbstractMigrationRule {
    private static final Map<String, String> ANNOTATION_MAPPINGS = new HashMap<>();

    static {
        ANNOTATION_MAPPINGS.put("@RestController", "@RestController");
        ANNOTATION_MAPPINGS.put("@Controller", "@Controller");
        ANNOTATION_MAPPINGS.put("@GetMapping", "@GetMapping");
        ANNOTATION_MAPPINGS.put("@PostMapping", "@PostMapping");
        ANNOTATION_MAPPINGS.put("@PutMapping", "@PutMapping");
        ANNOTATION_MAPPINGS.put("@DeleteMapping", "@DeleteMapping");
        ANNOTATION_MAPPINGS.put("@RequestMapping", "@RequestMapping");
        ANNOTATION_MAPPINGS.put("@PathVariable", "@PathVariable");
        ANNOTATION_MAPPINGS.put("@RequestParam", "@RequestParam");
        ANNOTATION_MAPPINGS.put("@RequestBody", "@RequestBody");
        ANNOTATION_MAPPINGS.put("@ResponseBody", "@ResponseBody");
        ANNOTATION_MAPPINGS.put("@Component", "@Component");
        ANNOTATION_MAPPINGS.put("@Service", "@Component");
        ANNOTATION_MAPPINGS.put("@Repository", "@Repository");
        ANNOTATION_MAPPINGS.put("@Configuration", "@Configuration");
        ANNOTATION_MAPPINGS.put("@Autowired", "@Inject");
        ANNOTATION_MAPPINGS.put("@Value", "@Value");
        ANNOTATION_MAPPINGS.put("@Transactional", "@Transactional");
        ANNOTATION_MAPPINGS.put("@Scheduled", "@Scheduled");
        ANNOTATION_MAPPINGS.put("@EventListener", "@EventListener");
        ANNOTATION_MAPPINGS.put("@Cacheable", "@Cacheable");
    }

    public SpringBootAnnotationMigrationRule() {
        super(
                "spring-boot-annotation-migration",
                "Migrate Spring Boot annotations to EST Framework annotations",
                90
        );
    }

    @Override
    public boolean canApply(String content, MigrationConfig config) {
        if (config.getSourceFramework() != MigrationConfig.SourceFramework.SPRING_BOOT) {
            return false;
        }
        for (String oldAnnotation : ANNOTATION_MAPPINGS.keySet()) {
            if (content.contains(oldAnnotation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String apply(String content, MigrationConfig config, MigrationResult result) {
        String newContent = content;
        for (Map.Entry<String, String> entry : ANNOTATION_MAPPINGS.entrySet()) {
            if (!entry.getKey().equals(entry.getValue())) {
                newContent = replaceAnnotation(newContent, entry.getKey(), entry.getValue(), result);
            }
        }
        return newContent;
    }
}
