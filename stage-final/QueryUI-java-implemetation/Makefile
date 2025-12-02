.PHONY: build run clean

SRC=$(shell find . -name "*.java" -not -path "./out/*")
OUT=out

build:
	mkdir -p $(OUT)
	javac -d $(OUT) $(SRC)

run: build
	java -cp $(OUT):mssql-jdbc-11.2.0.jre18.jar QueryUI

clean:
	rm -rf $(OUT)

