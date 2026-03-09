#!/bin/bash

echo "========================================"
echo "  EST Monomer - 单体应用启动器"
echo "========================================"
echo ""

echo "正在启动 EST Monomer..."
echo ""
echo "应用将在 http://localhost:8080 启动"
echo "按 Ctrl+C 可以停止应用"
echo ""
echo "========================================"
echo ""

mvn exec:java -Dexec.mainClass="ltd.idcu.est.starter.monomer.EstMonomerApplication"

if [ $? -ne 0 ]; then
    echo ""
    echo "========================================"
    echo "  启动失败!"
    echo "  请确保已先运行 mvn clean install"
    echo "========================================"
    echo ""
fi
