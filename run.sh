#!/bin/bash
# ──────────────────────────────────────────────────────────────────────────────
#  Student Management System – Build & Run (Linux / macOS)
# ──────────────────────────────────────────────────────────────────────────────

LIB=lib
OUT=out
SRC=src
MAIN=com.sms.Main
JAR="$LIB/sqlite-jdbc-3.45.0.0.jar"
SLF4J_JAR="$LIB/slf4j-api-2.0.16.jar"

echo "=== Student Management System ==="

# 1. Check Java
if ! command -v java &>/dev/null; then
    echo "ERROR: Java not found. Install JDK 11+ first."
    exit 1
fi

# 2. Download required JDBC/runtime JARs if missing
mkdir -p "$LIB"
if [ ! -f "$JAR" ]; then
    echo "Downloading SQLite JDBC driver..."
    curl -fsSL "https://github.com/xerial/sqlite-jdbc/releases/download/3.45.0.0/sqlite-jdbc-3.45.0.0.jar" \
         -o "$JAR"
    if [ $? -ne 0 ]; then
        echo "ERROR: Download failed. Place sqlite-jdbc-3.45.0.0.jar in $LIB/ manually."
        exit 1
    fi
    echo "Downloaded: $JAR"
fi

if [ ! -f "$SLF4J_JAR" ]; then
    echo "Downloading SLF4J API..."
    curl -fsSL "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.16/slf4j-api-2.0.16.jar" \
         -o "$SLF4J_JAR"
    if [ $? -ne 0 ]; then
        echo "ERROR: Download failed. Place slf4j-api-2.0.16.jar in $LIB/ manually."
        exit 1
    fi
    echo "Downloaded: $SLF4J_JAR"
fi

# 3. Compile
echo "Compiling..."
mkdir -p $OUT
find $SRC -name "*.java" > sources.txt
javac -cp "$JAR:$SLF4J_JAR" -d $OUT @sources.txt
if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed."
    rm -f sources.txt
    exit 1
fi
rm -f sources.txt
echo "Compilation successful."

# 4. Run
echo "Starting application..."
echo ""
java -cp "$OUT:$JAR:$SLF4J_JAR" $MAIN
