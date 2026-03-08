import os
import shutil
import re

def fix_package_paths(base_dir, module_name, features_name=None):
    if features_name is None:
        features_name = module_name
    
    print(f"\n{'='*50}")
    print(f"Processing module: est-{module_name}")
    print(f"{'='*50}\n")
    
    module_path = os.path.join(base_dir, f"est-{module_name}")
    
    if not os.path.exists(module_path):
        print(f"Module path not found: {module_path}")
        return
    
    # 遍历模块下的所有子模块
    for submodule in os.listdir(module_path):
        submodule_path = os.path.join(module_path, submodule)
        if not os.path.isdir(submodule_path):
            continue
        
        print(f"Processing submodule: {submodule}")
        
        # 处理 main 源代码
        src_main_path = os.path.join(submodule_path, "src", "main", "java", "ltd", "idcu", "est", "features", features_name)
        if os.path.exists(src_main_path):
            print(f"  Found main source at: {src_main_path}")
            
            dest_main_path = os.path.join(submodule_path, "src", "main", "java", "ltd", "idcu", "est", module_name)
            
            # 移动文件
            if os.path.exists(dest_main_path):
                shutil.rmtree(dest_main_path)
            
            shutil.move(src_main_path, dest_main_path)
            print(f"    Moved to: {dest_main_path}")
            
            # 更新包名和导入
            for root, dirs, files in os.walk(dest_main_path):
                for file in files:
                    if file.endswith(".java"):
                        file_path = os.path.join(root, file)
                        with open(file_path, 'r', encoding='utf-8') as f:
                            content = f.read()
                        
                        # 替换 package 声明
                        content = re.sub(
                            rf'package ltd\.idcu\.est\.features\.{re.escape(features_name)}',
                            f'package ltd.idcu.est.{module_name}',
                            content
                        )
                        
                        # 替换 import 语句
                        content = re.sub(
                            rf'import ltd\.idcu\.est\.features\.{re.escape(features_name)}',
                            f'import ltd.idcu.est.{module_name}',
                            content
                        )
                        
                        with open(file_path, 'w', encoding='utf-8') as f:
                            f.write(content)
            
            print(f"    Updated package names")
        
        # 处理 test 源代码
        src_test_path = os.path.join(submodule_path, "src", "test", "java", "ltd", "idcu", "est", "features", features_name)
        if os.path.exists(src_test_path):
            print(f"  Found test source at: {src_test_path}")
            
            dest_test_path = os.path.join(submodule_path, "src", "test", "java", "ltd", "idcu", "est", module_name)
            
            # 移动文件
            if os.path.exists(dest_test_path):
                shutil.rmtree(dest_test_path)
            
            shutil.move(src_test_path, dest_test_path)
            print(f"    Moved to: {dest_test_path}")
            
            # 更新包名和导入
            for root, dirs, files in os.walk(dest_test_path):
                for file in files:
                    if file.endswith(".java"):
                        file_path = os.path.join(root, file)
                        with open(file_path, 'r', encoding='utf-8') as f:
                            content = f.read()
                        
                        # 替换 package 声明
                        content = re.sub(
                            rf'package ltd\.idcu\.est\.features\.{re.escape(features_name)}',
                            f'package ltd.idcu.est.{module_name}',
                            content
                        )
                        
                        # 替换 import 语句
                        content = re.sub(
                            rf'import ltd\.idcu\.est\.features\.{re.escape(features_name)}',
                            f'import ltd.idcu.est.{module_name}',
                            content
                        )
                        
                        with open(file_path, 'w', encoding='utf-8') as f:
                            f.write(content)
            
            print(f"    Updated package names")
    
    print(f"\nest-{module_name} module processing completed!")

if __name__ == "__main__":
    base_dir = r"d:\os\proj\java\est2.0\est-modules"
    
    modules_to_fix = [
        ("data", "data"),
        ("monitor", "monitor"),
        ("performance", "performance"),
        ("scheduler", "scheduler"),
        ("security", "security"),
        ("discovery", "discovery"),
        ("config", "config"),
        ("circuitbreaker", "circuitbreaker"),
        ("cache", "cache"),
    ]
    
    for module_name, features_name in modules_to_fix:
        fix_package_paths(base_dir, module_name, features_name)
    
    print("\n" + "="*50)
    print("All modules processed!")
    print("="*50)
