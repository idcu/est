
import os
import chardet

def detect_encoding(file_path):
    with open(file_path, 'rb') as f:
        raw_data = f.read()
        result = chardet.detect(raw_data)
        return result['encoding'], result['confidence']

def fix_file_encoding(file_path):
    try:
        with open(file_path, 'rb') as f:
            raw_data = f.read()
        
        # 尝试多种编码方案
        encodings_to_try = ['utf-8', 'gbk', 'gb2312', 'big5', 'iso-8859-1']
        content = None
        used_encoding = None
        
        for encoding in encodings_to_try:
            try:
                content = raw_data.decode(encoding)
                used_encoding = encoding
                print(f"  成功使用 {encoding} 解码")
                break
            except UnicodeDecodeError:
                continue
        
        if content is None:
            print(f"  无法解码文件: {file_path}")
            return False
        
        # 检查是否需要修复（常见的UTF-8被错误保存为GBK的情况）
        # 尝试将内容作为UTF-8重新编码，看看是否能得到合理的中文
        try:
            # 尝试修复：内容看起来像是GBK编码被错误解释为ISO-8859-1
            test_content = content.encode('latin-1').decode('utf-8')
            # 检查是否包含有效中文字符
            has_chinese = any('\u4e00' &lt;= c &lt;= '\u9fff' for c in test_content)
            if has_chinese:
                content = test_content
                print(f"  发现并修复了编码转换问题")
        except:
            pass
        
        # 保存为UTF-8
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"  已修复: {file_path}")
        return True
        
    except Exception as e:
        print(f"  处理文件失败 {file_path}: {e}")
        return False

def main():
    root_dir = '.'
    extensions = ['.md', '.java', '.xml', '.properties']
    
    print(f"开始扫描目录: {os.path.abspath(root_dir)}")
    print(f"处理文件类型: {extensions}")
    
    fixed_count = 0
    total_count = 0
    
    for dirpath, dirnames, filenames in os.walk(root_dir):
        # 跳过.git目录
        if '.git' in dirpath:
            continue
            
        for filename in filenames:
            ext = os.path.splitext(filename)[1].lower()
            if ext in extensions:
                file_path = os.path.join(dirpath, filename)
                total_count += 1
                print(f"\n处理文件 {total_count}: {file_path}")
                if fix_file_encoding(file_path):
                    fixed_count += 1
    
    print(f"\n完成！共处理 {total_count} 个文件，修复了 {fixed_count} 个文件。")

if __name__ == '__main__':
    main()

