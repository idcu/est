
# 诊断README.md的编码问题
with open(r'd:\os\proj\java\est2.0\README.md', 'rb') as f:
    raw = f.read(500)  # 读取前500字节

print("原始字节:")
print(raw)
print("\n")

print("尝试不同的编码解码:")
encodings = ['utf-8', 'gbk', 'gb2312', 'big5', 'iso-8859-1']
for enc in encodings:
    try:
        decoded = raw.decode(enc)
        print(f"{enc:12} -&gt; {repr(decoded[:100])}")
    except Exception as e:
        print(f"{enc:12} -&gt; 错误: {e}")

print("\n")
print("尝试修复常见的编码转换错误:")
try:
    content = raw.decode('utf-8')
    # 尝试latin-1反向转换
    fixed1 = content.encode('latin-1').decode('utf-8')
    print(f"utf-8 -&gt; latin-1 encode -&gt; utf-8 decode:")
    print(repr(fixed1[:200]))
except Exception as e:
    print(f"错误: {e}")

