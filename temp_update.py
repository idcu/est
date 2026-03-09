import os
import glob

base_dir = r"d:\os\proj\java\est2.0"
target_version = "2.1.0"

# 查找所有 pom.xml 文件
pom_files = glob.glob(os.path.join(base_dir, "**", "pom.xml"), recursive=True)

count = 0

for filepath in pom_files:
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original = content
        
        # 替换 <version>2</version>
        content = content.replace('<version>2</version>', f'<version>{target_version}</version>')
        
        # 替换 <version>2.0.0</version>
        content = content.replace('<version>2.0.0</version>', f'<version>{target_version}</version>')
        
        if content != original:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            relative_path = os.path.relpath(filepath, base_dir)
            print(f"Updated: {relative_path}")
            count += 1
        else:
            relative_path = os.path.relpath(filepath, base_dir)
            print(f"No change: {relative_path}")
    
    except Exception as e:
        print(f"Error processing {filepath}: {e}")

print(f"\nTotal updated: {count}")
print("Done!")
