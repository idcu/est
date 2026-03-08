#!/usr/bin/env python3
import os
import glob

def convert_file(file_path):
    """Convert file from GBK to UTF-8"""
    try:
        # Read with GBK
        with open(file_path, 'r', encoding='gbk') as f:
            content = f.read()
        
        # Write with UTF-8
        with open(file_path, 'w', encoding='utf-8', newline='') as f:
            f.write(content)
        
        print(f"Converted: {file_path}")
        return True
    except UnicodeDecodeError:
        print(f"  Not GBK, trying UTF-8: {file_path}")
        try:
            # Try reading as UTF-8
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            # If it works, no need to change
            print(f"  Already UTF-8: {file_path}")
            return True
        except Exception as e:
            print(f"  ERROR: {file_path}: {e}")
            return False
    except Exception as e:
        print(f"  ERROR: {file_path}: {e}")
        return False

def main():
    # Find all .md files
    md_files = glob.glob('**/*.md', recursive=True)
    print(f"Found {len(md_files)} .md files")
    
    success_count = 0
    fail_count = 0
    
    for file_path in md_files:
        if convert_file(file_path):
            success_count += 1
        else:
            fail_count += 1
    
    print(f"\n=== Summary ===")
    print(f"Successfully processed: {success_count}")
    print(f"Failed: {fail_count}")

if __name__ == "__main__":
    main()
