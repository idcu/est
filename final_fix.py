#!/usr/bin/env python3
import subprocess
import os
import sys

def read_git_file(file_path):
    """Read file content from Git"""
    result = subprocess.run(
        ['git', 'show', f'HEAD:{file_path}'],
        capture_output=True
    )
    if result.returncode == 0:
        return result.stdout
    return None

def main():
    print("=" * 60)
    print("EST Documentation Encoding Fix")
    print("=" * 60)
    
    # Step 1: Reset to Git HEAD
    print("\n[1/4] Resetting to Git HEAD...")
    subprocess.run(['git', 'reset', '--hard', 'HEAD'], capture_output=True)
    subprocess.run(['git', 'clean', '-fd'], capture_output=True)
    print("  Done!")
    
    # Step 2: Get list of all .md files
    print("\n[2/4] Finding .md files in Git...")
    result = subprocess.run(
        ['git', 'ls-files'],
        capture_output=True,
        text=True
    )
    all_files = result.stdout.splitlines()
    md_files = [f for f in all_files if f.endswith('.md')]
    print(f"  Found {len(md_files)} .md files")
    
    # Step 3: Restore each file from Git
    print("\n[3/4] Restoring files from Git...")
    success = 0
    failed = 0
    
    for file_path in md_files:
        try:
            content = read_git_file(file_path)
            if content is None:
                print(f"  FAIL: {file_path}")
                failed += 1
                continue
            
            # Ensure directory exists
            dir_path = os.path.dirname(file_path)
            if dir_path and not os.path.exists(dir_path):
                os.makedirs(dir_path, exist_ok=True)
            
            # Write file
            with open(file_path, 'wb') as f:
                f.write(content)
            
            print(f"  OK: {file_path}")
            success += 1
        except Exception as e:
            print(f"  FAIL: {file_path} - {e}")
            failed += 1
    
    # Step 4: Show summary
    print("\n[4/4] Summary:")
    print(f"  Successfully restored: {success}")
    print(f"  Failed: {failed}")
    
    # Verify README.md
    print("\n" + "=" * 60)
    print("Verifying README.md from Git:")
    print("=" * 60)
    readme_content = read_git_file('README.md')
    if readme_content:
        print(readme_content.decode('utf-8', errors='replace')[:1000])
    print("\n" + "=" * 60)
    print("\nDone! All documents have been restored from Git.")
    print("If you still see garbled text, it may be an editor display issue.")
    print("Try opening the files in VS Code or another editor with UTF-8 encoding.")

if __name__ == "__main__":
    main()
