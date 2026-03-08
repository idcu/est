#!/usr/bin/env python3
import os
import subprocess
import sys

def restore_files_from_git():
    # Get all .md files from Git
    print("Finding all .md files in Git...")
    result = subprocess.run(['git', 'ls-files'], capture_output=True, text=True, encoding='utf-8')
    all_files = result.stdout.splitlines()
    md_files = [f for f in all_files if f.endswith('.md')]
    
    print(f"Found {len(md_files)} .md files")
    
    restored_count = 0
    failed_count = 0
    
    for file_path in md_files:
        try:
            # Get content from Git
            result = subprocess.run(['git', 'show', f'HEAD:{file_path}'], 
                                  capture_output=True, text=True, encoding='utf-8')
            content = result.stdout
            
            # Write back with UTF-8 encoding
            with open(file_path, 'w', encoding='utf-8', newline='') as f:
                f.write(content)
            
            print(f"Restored: {file_path}")
            restored_count += 1
        except Exception as e:
            print(f"Failed to restore {file_path}: {e}")
            failed_count += 1
    
    print(f"\nRestore complete!")
    print(f"Restored: {restored_count} files")
    print(f"Failed: {failed_count} files")

if __name__ == "__main__":
    restore_files_from_git()
