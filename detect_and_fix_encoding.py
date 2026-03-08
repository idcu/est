#!/usr/bin/env python3
import os
import chardet
import glob

def detect_encoding(file_path):
    """Detect the encoding of a file"""
    with open(file_path, 'rb') as f:
        raw_data = f.read()
    result = chardet.detect(raw_data)
    return result['encoding'], result['confidence'], raw_data

def fix_file_encoding(file_path):
    """Try to fix file encoding"""
    print(f"\nProcessing: {file_path}")
    
    encoding, confidence, raw_data = detect_encoding(file_path)
    print(f"  Detected encoding: {encoding} (confidence: {confidence:.2f})")
    
    # Try to decode with various encodings
    encodings_to_try = ['utf-8', 'gbk', 'gb2312', 'big5', 'utf-16']
    content = None
    used_encoding = None
    
    for enc in encodings_to_try:
        try:
            content = raw_data.decode(enc)
            used_encoding = enc
            print(f"  Successfully decoded with: {enc}")
            break
        except UnicodeDecodeError:
            continue
    
    if content is None:
        print(f"  ERROR: Could not decode file")
        return False
    
    # Write back as UTF-8
    try:
        with open(file_path, 'w', encoding='utf-8', newline='') as f:
            f.write(content)
        print(f"  Converted to UTF-8 successfully")
        return True
    except Exception as e:
        print(f"  ERROR: Could not write file: {e}")
        return False

def main():
    # Find