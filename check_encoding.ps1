# 检查文件编码和乱码的脚本

$rootDir = $PWD.Path
$extensions = @('.java', '.xml', '.properties', '.md')
$garbledPatterns = @('示例�?', '入门�?', '现代写法�?', 'MVC 架构�?', '包含中间件、会话、模板等�?', '最简 Hello World�?', '了解EST Web 框架的核心概念', '代码步骤�?', '欢迎使用 EST Web 框架�?', '服务器启动成功！', '请在浏览器中访问�?', 'Ctrl+C 停止服务�?', 'Lambda 表达式路�?', '使用 Lambda 表达式可以更灵活地定义路�?', '带路径参数的问�?', '带查询参�?', '欢迎使用 EST Web 框架�?/h1>', '这是一个使�?Lambda 表达式定义路由的示例�?/p>', '问候小�?/a>', '问候小�?/a>', '问�?/title>', '你好�?s %s�?/h1>', '欢迎来到 EST Web 世界�?/p>', '控制器模式（MVC 架构�?', '控制器示�?', '访问地址�?', '提供标准�?CRUD 操作接口', 'REST API 服务器启动成功！', '可用�?API 端点�?', '完整示�?', '中间件（日志、性能监控�?', '静态文件服�?', '完整 Web 应用启动成功�?', '欢迎来到首页�?/h1>', '这是使用控制器模式构建的 Web 应用�?/p>', 'EST 是一个零依赖的现�?Java Web 框架�?/p>', '未命名用�?', '查看 API 状�?/a>', '本演示将运行�?1 个示例', '我的第一�?Web 应用')

Write-Host "开始检查文件编码和乱码，根目录: $rootDir"
Write-Host "要检查的文件类型: $($extensions -join ', ')"
Write-Host ""

$files = Get-ChildItem -Path $rootDir -Recurse -File | Where-Object {
    $ext = $_.Extension.ToLower()
    $extensions -contains $ext
}

Write-Host "找到 $($files.Count) 个文件需要检查"
Write-Host ""

$problemFiles = @()
$processedCount = 0

foreach ($file in $files) {
    $processedCount++
    if ($processedCount % 100 -eq 0) {
        Write-Host "已处理 $processedCount / $($files.Count) 文件"
    }
    
    try {
        $content = Get-Content -Path $file.FullName -Raw -Encoding UTF8
        
        foreach ($pattern in $garbledPatterns) {
            if ($content -match $pattern) {
                $problemFiles += $file.FullName
                Write-Host "发现乱码: $($file.FullName)"
                break
            }
        }
    } catch {
        Write-Error "处理文件失败: $($file.FullName)"
        Write-Error $_.Exception.Message
    }
}

Write-Host ""
Write-Host "检查完成！"
Write-Host "已处理 $processedCount 个文件"
Write-Host "发现 $($problemFiles.Count) 个文件存在乱码"

if ($problemFiles.Count -gt 0) {
    Write-Host ""
    Write-Host "存在乱码的文件："
    $problemFiles | ForEach-Object {
        Write-Host "  - $_"
    }
} else {
    Write-Host ""
    Write-Host "所有文件均无乱码问题！"
}
