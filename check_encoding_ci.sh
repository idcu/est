#!/bin/bash

# CI/CD encoding check script
echo "Checking file encodings..."

# Find all text files
FILES=$(find . -type f -name "*.java" -o -name "*.xml" -o -name "*.properties" -o -name "*.md")

ERROR_COUNT=0

for FILE in $FILES; do
    # Check if file is UTF-8 encoded
    if ! file -i "$FILE" | grep -q "charset=utf-8"; then
        echo "ERROR: $FILE is not UTF-8 encoded"
        ERROR_COUNT=$((ERROR_COUNT + 1))
    fi
done

if [ $ERROR_COUNT -eq 0 ]; then
    echo "All files are UTF-8 encoded!"
    exit 0
else
    echo "Found $ERROR_COUNT files with non-UTF-8 encoding"
    exit 1
fi
