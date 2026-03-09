#!/bin/bash

echo "========================================"
echo "  EST Admin UI 启动脚本"
echo "========================================"
echo ""

cd "$(dirname "$0")"

echo "正在检查 Node.js 和 npm..."
node --version
if [ $? -ne 0 ]; then
    echo ""
    echo "错误：未找到 Node.js，请先安装 Node.js！"
    exit 1
fi

npm --version
echo ""

if [ ! -d "node_modules" ]; then
    echo "正在安装依赖..."
    npm install
    if [ $? -ne 0 ]; then
        echo ""
        echo "依赖安装失败！"
        exit 1
    fi
    echo ""
fi

echo "正在启动开发服务器..."
echo ""
echo "前端访问地址：http://localhost:3000"
echo "后端 API 地址：http://localhost:8080"
echo ""
echo "请确保后端服务已启动！"
echo ""

npm run dev
