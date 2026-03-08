#!/usr/bin/env python3
import subprocess
import os

def main():
    print("Getting list of .md files from Git...")
    result = subprocess.run(
        ['git', 'ls-files'],
        capture_output=True,
        text=True,
        encoding='utf-8'
    )
    all_files = result.stdout.splitlines()
    md_files = [f for f in all_files if f.endswith('.md')]
    print(f"Found {len(md_files)} .md files")
    
    success = 0
    failed = 0
    
    for file_path in md_files:
        try:
            print(f"Processing: {file_path}")
            
            # Get the file content from Git using raw bytes
            result = subprocess.run(
                ['git', 'show', f'HEAD:{file_path}'],
                capture_output=True
            )
            
            if result.returncode != 0:
                print(f"  ERROR: git show failed for {file_path}")
                failed += 1
                continue
            
            # Ensure directory exists
            dir_path = os.path.dirname(file_path)
            if dir_path and not os.path.exists(dir_path):
                os.makedirs(dir_path, exist_ok=True)
            
            # Write the raw bytes back as-is
            with open(file_path, 'wb') as f:
                f.write(result.stdout)
            
            print(f"  OK: Restored {file_path}")
            success += 1
            
        except Exception as e:
            print(f"  ERROR: {e}")
            failed += 1
    
    print(f"\nDone! Success: {success}, Failed: {failed}")

if __name__ == "__main__":
    main()
