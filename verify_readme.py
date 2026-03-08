#!/usr/bin/env python3
import subprocess

# Read README.md from Git
result = subprocess.run(['git', 'show', 'HEAD:README.md'], capture_output=True, text=True, encoding='utf-8')
print("=== README.md from Git ===")
print(result.stdout[:2000])  # Show first 2000 characters
print("\n=== End of Preview ===")

# Also write it to a temp file for verification
with open("README_VERIFICATION.md", "w", encoding="utf-8") as f:
    f.write(result.stdout)
print("\nVerification file written to: README_VERIFICATION.md")
