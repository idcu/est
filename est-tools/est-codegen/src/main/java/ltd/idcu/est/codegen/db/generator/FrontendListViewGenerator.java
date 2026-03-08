package ltd.idcu.est.codegen.db.generator;

import ltd.idcu.est.codegen.db.config.GeneratorConfig;
import ltd.idcu.est.codegen.db.metadata.Column;
import ltd.idcu.est.codegen.db.metadata.Table;

public class FrontendListViewGenerator {
    
    private final GeneratorConfig config;
    
    public FrontendListViewGenerator(GeneratorConfig config) {
        this.config = config;
    }
    
    public String generate(Table table) {
        String className = table.getClassName();
        String varName = toCamelCase(className);
        String kebabName = toKebabCase(className);
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("<template>\n");
        sb.append("  <div class=\"").append(kebabName).append("-list\">\n");
        sb.append("    <el-card>\n");
        sb.append("      <template #header>\n");
        sb.append("        <div class=\"card-header\">\n");
        sb.append("          <span>").append(className).append("管理</span>\n");
        sb.append("          <el-button type=\"primary\" @click=\"handleAdd\">新增</el-button>\n");
        sb.append("        </div>\n");
        sb.append("      </template>\n\n");
        
        sb.append("      <el-form :inline=\"true\" :model=\"queryParams\" class=\"search-form\">\n");
        for (Column column : table.getColumns()) {
            String fieldName = toCamelCase(column.getName());
            sb.append("        <el-form-item label=\"").append(column.getRemarks() != null ? column.getRemarks() : fieldName).append("\">\n");
            sb.append("          <el-input v-model=\"queryParams.").append(fieldName).append("\" placeholder=\"请输入\" clearable />\n");
            sb.append("        </el-form-item>\n");
        }
        sb.append("        <el-form-item>\n");
        sb.append("          <el-button type=\"primary\" @click=\"handleSearch\">查询</el-button>\n");
        sb.append("          <el-button @click=\"handleReset\">重置</el-button>\n");
        sb.append("        </el-form-item>\n");
        sb.append("      </el-form>\n\n");
        
        sb.append("      <el-table :data=\"tableData\" border style=\"width: 100%; margin-top: 20px\">\n");
        for (Column column : table.getColumns()) {
            String fieldName = toCamelCase(column.getName());
            sb.append("        <el-table-column prop=\"").append(fieldName).append("\" label=\"").append(column.getRemarks() != null ? column.getRemarks() : fieldName).append("\" />\n");
        }
        sb.append("        <el-table-column label=\"操作\" width=\"200\">\n");
        sb.append("          <template #default=\"scope\">\n");
        sb.append("            <el-button type=\"primary\" size=\"small\" @click=\"handleEdit(scope.row)\">编辑</el-button>\n");
        sb.append("            <el-button type=\"danger\" size=\"small\" @click=\"handleDelete(scope.row)\">删除</el-button>\n");
        sb.append("          </template>\n");
        sb.append("        </el-table-column>\n");
        sb.append("      </el-table>\n\n");
        
        sb.append("      <el-pagination\n");
        sb.append("        v-model:current-page=\"queryParams.page\"\n");
        sb.append("        v-model:page-size=\"queryParams.pageSize\"\n");
        sb.append("        :page-sizes=\"[10, 20, 50, 100]\"\n");
        sb.append("        :total=\"total\"\n");
        sb.append("        layout=\"total, sizes, prev, pager, next, jumper\"\n");
        sb.append("        @size-change=\"handleSearch\"\n");
        sb.append("        @current-change=\"handleSearch\"\n");
        sb.append("        style=\"margin-top: 20px; justify-content: flex-end\"\n");
        sb.append("      />\n");
        sb.append("    </el-card>\n");
        sb.append("  </div>\n");
        sb.append("</template>\n\n");
        
        sb.append("<script setup lang=\"ts\">\n");
        sb.append("import { ref, reactive, onMounted } from 'vue'\n");
        sb.append("import { ElMessage, ElMessageBox } from 'element-plus'\n");
        sb.append("import { get").append(className).append("List, delete").append(className).append(", type ").append(className).append("Info, type ").append(className).append("Query } from '@/api/").append(kebabName).append("'\n\n");
        
        sb.append("const tableData = ref<").append(className).append("Info[]>([])\n");
        sb.append("const total = ref(0)\n\n");
        
        sb.append("const queryParams = reactive<").append(className).append("Query>({\n");
        sb.append("  page: 1,\n");
        sb.append("  pageSize: 10\n");
        for (Column column : table.getColumns()) {
            String fieldName = toCamelCase(column.getName());
            sb.append("  ").append(fieldName).append(": undefined\n");
        }
        sb.append("})\n\n");
        
        sb.append("async function fetchData() {\n");
        sb.append("  try {\n");
        sb.append("    const res = await get").append(className).append("List(queryParams)\n");
        sb.append("    tableData.value = res.data?.list || []\n");
        sb.append("    total.value = res.data?.total || 0\n");
        sb.append("  } catch (error) {\n");
        sb.append("    ElMessage.error('获取数据失败')\n");
        sb.append("  }\n");
        sb.append("}\n\n");
        
        sb.append("function handleSearch() {\n");
        sb.append("  queryParams.page = 1\n");
        sb.append("  fetchData()\n");
        sb.append("}\n\n");
        
        sb.append("function handleReset() {\n");
        sb.append("  queryParams.page = 1\n");
        sb.append("  queryParams.pageSize = 10\n");
        for (Column column : table.getColumns()) {
            String fieldName = toCamelCase(column.getName());
            sb.append("  queryParams.").append(fieldName).append(" = undefined\n");
        }
        sb.append("  fetchData()\n");
        sb.append("}\n\n");
        
        sb.append("function handleAdd() {\n");
        sb.append("  ElMessage.info('新增功能待实�?)\n");
        sb.append("}\n\n");
        
        sb.append("function handleEdit(row: ").append(className).append("Info) {\n");
        sb.append("  ElMessage.info('编辑功能待实�?)\n");
        sb.append("}\n\n");
        
        sb.append("async function handleDelete(row: ").append(className).append("Info) {\n");
        sb.append("  try {\n");
        sb.append("    await ElMessageBox.confirm('确定要删除吗?', '提示', {\n");
        sb.append("      confirmButtonText: '确定',\n");
        sb.append("      cancelButtonText: '取消',\n");
        sb.append("      type: 'warning'\n");
        sb.append("    })\n");
        sb.append("    await delete").append(className).append("(row.id!)\n");
        sb.append("    ElMessage.success('删除成功')\n");
        sb.append("    fetchData()\n");
        sb.append("  } catch (error) {\n");
        sb.append("    if (error !== 'cancel') {\n");
        sb.append("      ElMessage.error('删除失败')\n");
        sb.append("    }\n");
        sb.append("  }\n");
        sb.append("}\n\n");
        
        sb.append("onMounted(() => {\n");
        sb.append("  fetchData()\n");
        sb.append("})\n");
        sb.append("</script>\n\n");
        
        sb.append("<style scoped lang=\"scss\">\n");
        sb.append(".").append(kebabName).append("-list {\n");
        sb.append("  .card-header {\n");
        sb.append("    display: flex;\n");
        sb.append("    justify-content: space-between;\n");
        sb.append("    align-items: center;\n");
        sb.append("  }\n");
        sb.append("  \n");
        sb.append("  .search-form {\n");
        sb.append("    margin-bottom: 20px;\n");
        sb.append("  }\n");
        sb.append("}\n");
        sb.append("</style>\n");
        
        return sb.toString();
    }
    
    private String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] parts = input.split("_");
        StringBuilder result = new StringBuilder();
        result.append(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            result.append(Character.toUpperCase(parts[i].charAt(0)))
                  .append(parts[i].substring(1).toLowerCase());
        }
        return result.toString();
    }
    
    private String toKebabCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append("-");
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
