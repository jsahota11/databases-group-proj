.PHONY: build jar run clean

# Source files
LIBSRC = com/database/queries/Database.java
MAINSRC = QueryUI.java

# Output directories and files
OUT = out
JAR = $(OUT)/database-lib.jar
JDBC = mssql-jdbc-11.2.0.jre18.jar

# Compile library classes
$(OUT)/com/database/queries/Database.class: $(LIBSRC)
	mkdir -p $(OUT)
	javac -d $(OUT) $(LIBSRC)

# Build the JAR
jar: $(OUT)/com/database/queries/Database.class
	jar cf $(JAR) -C $(OUT) com/database/queries/Database.class

# Compile main program
build: jar
	javac -cp $(JAR) $(MAINSRC)

# Run the program
run: build
	java -cp .:$(JAR):$(JDBC) QueryUI

# Clean everything
clean:
	rm -rf $(OUT) *.class

