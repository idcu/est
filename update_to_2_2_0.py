import os
import re
import glob

base_dir = r"d:\os\proj\java\est2.0"

def update_version_in_file(file_path, old_version, new_version):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        old_pattern = re.escape(old_version)
        new_content = re.sub(r'<version>' + old_pattern + r'</version>', 
                            f'<version>{new_version}</version>', content)
        
        if new_content != content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print(f"Updated: {file_path}")
            return True
        return False
    except Exception as e:
        print(f"Error updating {file_path}: {e}")
        return False

def update_property_in_file(file_path, property_name, old_version, new_version):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        old_pattern = re.escape(old_version)
        property_pattern = f'<{property_name}>' + old_pattern + f'</{property_name}>'
        new_property = f'<{property_name}>{new_version}</{property_name}>'
        
        new_content = re.sub(property_pattern, new_property, content)
        
        if new_content != content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print(f"Updated property {property_name} in: {file_path}")
            return True
        return False
    except Exception as e:
        print(f"Error updating property in {file_path}: {e}")
        return False

def find_all_pom_files(directory):
    return glob.glob(os.path.join(directory, "**", "pom.xml"), recursive=True)

old_version = "2.1.0"
new_version = "2.2.0"

print(f"Updating version from {old_version} to {new_version}...")

pom_files = find_all_pom_files(base_dir)
print(f"Found {len(pom_files)} pom.xml files")

updated_count = 0

for pom_file in pom_files:
    if update_version_in_file(pom_file, old_version, new_version):
        updated_count += 1

main_pom = os.path.join(base_dir, "pom.xml")
if os.path.exists(main_pom):
    if update_property_in_file(main_pom, "project.version", old_version, new_version):
        updated_count += 1

print(f"\nDone! Updated {updated_count} files.")
