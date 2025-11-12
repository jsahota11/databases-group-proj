.PHONY := build


Queries.class: Queries.java
	javac Queries.java

build: Queries.class

run: build
	java -cp .:sqlite-jdbc-3.39.3.0.jar Queries

clean:
	rm *.class
