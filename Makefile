# Compiler
JAVAC = javac
JAVA = java

# Libraries
LIBS = gson-2.11.0.jar

# Directories
SRC_DIR = src
OUT_DIR = out

# Main class
MAIN_CLASS = NetworkApp

# Input file for the program
INPUT_FILE = TestCase1.txt

# Java source files
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Compiled class files
CLASSES = $(patsubst $(SRC_DIR)/%.java, $(OUT_DIR)/%.class, $(SOURCES))

# Default target: Compile all sources
all: $(CLASSES)

# Rule to compile .java files into the output directory with classpath
$(OUT_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(OUT_DIR)
	$(JAVAC) -cp $(LIBS) -d $(OUT_DIR) $<

# Run the program
run: all
	$(JAVA) -cp .:$(LIBS):$(OUT_DIR) $(MAIN_CLASS) $(INPUT_FILE)

# Clean up compiled files
clean:
	rm -rf $(OUT_DIR)

# Help target for instructions
help:
	@echo "Available targets:"
	@echo "  all    - Compile all source files"
	@echo "  run    - Compile and run the program with $(INPUT_FILE)"
	@echo "  clean  - Remove compiled files"
	@echo "  help   - Show this help message"
