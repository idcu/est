import os
import re

base_dir = r"d:\os\proj\java\est2.0"
target_version = "2.1.0"

# 使用 Glob 工具获取所有 pom.xml 文件
import glob
pom_files = glob.glob(os.path.join(base_dir, "**", "pom.xml"), recursive=True)

count = 0

for filepath in pom_files:
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 更新 <version> 标签（项目版本）
        new_content = re.sub(r'(<groupId>ltd\.idcu</groupId>\s*<artifactId>[^<]+</artifactId>\s*)<version>[^<]+</version>', 
                             rf'\1<version>{target_version}</version>', content)
        
        # 更新 parent 中的版本号
        new_content = re.sub(r'(<parent>\s*<groupId>ltd\.idcu</groupId>\s*<artifactId>[^<]+</artifactId>\s*)<version>[^<]+</version>', 
                             rf'\1<version>{target_version}</version>', new_content)
        
        # 更新根 pom.xml 的 version 标签
        if filepath == os.path.join(base_dir, 'pom.xml'):
            new_content = re.sub(r'(<groupId>ltd\.idcu</groupId>\s*<artifactId>est</artifactId>\s*)<version>[^<]+</version>', 
                                 rf'\1<version>{target_version}</version>', new_content)
        
        if new_content != content:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(new_content)
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
