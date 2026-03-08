
import os

def fix_file(file_path):
    try:
        # 读取文件字节
        with open(file_path, 'rb') as f:
            raw_bytes = f.read()
        
        # 问题分析：这些文件是UTF-8编码，但被错误地用GBK解码后又存回了
        # 我们需要尝试反向修复
        content = None
        
        # 方案1：尝试直接用UTF-8解码
        try:
            content = raw_bytes.decode('utf-8')
        except:
            pass
        
        # 方案2：如果直接解码看起来有问题，尝试常见的编码转换错误修复
        if content and ('�' in content or '锟' in content):
            print(f"  检测到乱码，尝试修复: {file_path}")
            
            # 尝试将内容编码为latin-1，然后用UTF-8解码（常见的错误转换修复）
            try:
                test_content = content.encode('latin-1').decode('utf-8')
                # 检查是否包含中文字符
                has_chinese = any('\u4e00' &lt;= c &lt;= '\u9fff' for c in test_content)
                if has_chinese:
                    content = test_content
                    print(f"  成功修复编码转换问题")
            except:
                pass
            
            # 尝试另一种方案：GBK编码 → UTF-8解码
            try:
                test_content = raw_bytes.decode('gbk').encode('utf-8').decode('utf-8')
                has_chinese = any('\u4e00' &lt;= c &lt;= '\u9fff' for c in test_content)
                if has_chinese and ('�' not in test_content):
                    content = test_content
                    print(f"  使用GBK→UTF-8修复成功")
            except:
                pass
        
        if content:
            # 保存为UTF-8
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        else:
            return False
            
    except Exception as e:
        print(f"  错误: {file_path}: {e}")
        return False

def main():
    root_dir = '.'
    extensions = ['.md', '.java', '.xml', '.properties']
    
    print(f"开始修复文档编码...")
    
    fixed = 0
    total = 0
    
    for dirpath, dirnames, filenames in os.walk(root_dir):
        if '.git' in dirpath:
            continue
            
        for filename in filenames:
            ext = os.path.splitext(filename)[1].lower()
            if ext in extensions:
                filepath = os.path.join(dirpath, filename)
                total += 1
                print(f"处理: {filepath}")
                if fix_file(filepath):
                    fixed += 1
    
    print(f"\n完成！处理了 {total} 个文件，修复了 {fixed} 个文件。")

if __name__ == '__main__':
    main()

