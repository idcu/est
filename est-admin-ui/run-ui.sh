#!/bin/bash

echo "========================================"
echo "  EST Admin UI - 管理后台前端"
echo "========================================"
echo ""

echo "正在检查依赖..."
if [ ! -d "node_modules" ]; then
    echo ""
    echo "依赖未安装，正在安装..."
    echo ""
    npm install
    if [ $? -ne 0 ]; then
        echo ""
        echo "========================================"
        echo "  依赖安装失败!"
        echo "  请确保已安装 Node.js 16+"
        echo "========================================"
        echo ""
        exit 1
    fi
fi

echo ""
echo "正在启动开发服务器..."
echo ""
echo "前端将在 http://localhost:3000 启动"
echo "后端 API 代理到 http://localhost:8080"
echo ""
echo "========================================"
echo ""

npm run dev

if [ $? -ne 0 ]; then
    echo ""
    echo "========================================"
    echo "  启动失败!"
    echo "========================================"
    echo ""
fi
