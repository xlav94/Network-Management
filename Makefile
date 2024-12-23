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

# Default target: Compile all sources
.PHONY: all
all:
	@mkdir -p $(OUT_DIR)
	$(JAVAC) -cp $(LIBS) -d $(OUT_DIR) $(SRC_DIR)/*.java

# Run the program
.PHONY: run
run: all
	$(JAVA) -cp .:$(LIBS):$(OUT_DIR) $(MAIN_CLASS) $(INPUT_FILE)

# Clean up compiled files
.PHONY: clean
clean:
	rm -rf $(OUT_DIR)

# Display help message
.PHONY: help
help:
	@echo "Available targets:"
	@echo "  all    - Compile the NetworkApp.java file"
	@echo "  run    - Compile and run the program with $(INPUT_FILE)"
	@echo "  clean  - Remove compiled files"
	@echo "  help   - Show this help message"
