#!/bin/bash

echo "========================================"
echo "  EST Admin Console 启动脚本"
echo "========================================"
echo ""

cd "$(dirname "$0")"

echo "正在编译项目..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "编译失败！请检查错误信息。"
    exit 1
fi

echo ""
echo "编译成功！"
echo ""
echo "正在启动 EST Admin Console..."
echo ""

cd est-admin-impl
java -jar target/est-admin-impl-2.0.0.jar
