SRC_DIR="src"
OUT_DIR="out"
MAIN_CLASS="com.vfs.ShellEmulator"

if [ ! -d "$SRC_DIR" ]; then
    echo "ERROR: Source directory '$SRC_DIR' not found"
    exit 1
fi

mkdir -p "$OUT_DIR"

echo "Compiling Java files..."

javac -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")

if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed"
    exit 1
fi

echo "Compilation successful!"
echo ""

echo "Test 1: Running with default config..."
java -cp "$OUT_DIR" "$MAIN_CLASS"

echo ""
echo "Test 2: Running with custom config..."
java -cp "$OUT_DIR" "$MAIN_CLASS" --config custom.yaml --vfs /tmp/vfs

echo ""
echo "Tests completed!"