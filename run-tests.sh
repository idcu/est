#!/bin/bash
echo "========================================"
echo "EST Framework Test Runner"
echo "========================================"
echo

echo "Compiling project first..."
mvn clean compile -DskipTests
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi
echo

echo "========================================"
echo "Compilation successful!"
echo "========================================"
echo
echo "Now you can run specific tests:"
echo
echo "1. To run core container tests:"
echo "   mvn exec:java -Dexec.mainClass=\"ltd.idcu.est.test.TestLauncher\" -pl est-base/est-test/est-test-impl"
echo
echo "2. To run est-admin:"
echo "   cd est-app/est-admin"
echo "   ./run-admin.sh"
echo
echo "========================================"
