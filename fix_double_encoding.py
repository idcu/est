#!/usr/bin/env python3
import os
import glob

def fix_double_encoding(file_path):
    """Fix files that have double encoding (UTF-8 misread as GBK then saved as UTF-8)"""
    try:
        # Read the file as bytes
        with open(file_path, 'rb') as f:
            raw_bytes = f.read()
        
        # Try strategy 1: The file is actually correct UTF-8, try to read it
        try:
            content = raw_bytes.decode('utf-8')
            print(f"  Already valid UTF-8: {file_path}")
            # No need to change
            return True
        except UnicodeDecodeError:
            pass
        
        # Try strategy 2: File was GBK encoded, needs to be saved as UTF-8
        try:
            content = raw_bytes.decode('gbk')
            with open(file_path, 'w', encoding='utf-8', newline='') as f:
                f.write(content)
            print(f"  Converted from GBK to UTF-8: {file_path}")
            return True
        except UnicodeDecodeError:
            pass
        
        # Try strategy 3: Double encoding - GBK bytes misinterpreted as ISO-8859-1 then saved as UTF-8
        try:
            # First decode as ISO-8859-1 to get the original GBK bytes
            intermediate = raw_bytes.decode('iso-8859-1')
            # Then encode back to bytes (as ISO-8859-1) to get GBK bytes
            gbk_bytes = intermediate.encode('iso-8859-1')
            # Now decode as GBK
            content = gbk_bytes.decode('gbk')
            # Write as UTF-8
            with open(file_path, 'w', encoding='utf-8', newline='') as f:
                f.write(content)
            print(f"  Fixed double encoding (ISO-8859-1->GBK->UTF-8): {file_path}")
            return True
        except Exception as e:
            pass
        
        print(f"  ERROR: Could not decode: {file_path}")
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
        if fix_double_encoding(file_path):
            success_count += 1
        else:
            fail_count += 1
    
    print(f"\n=== Summary ===")
    print(f"Successfully processed: {success_count}")
    print(f"Failed: {fail_count}")

if __name__ == "__main__":
    main()
