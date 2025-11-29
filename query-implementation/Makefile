.PHONY := build


Queries.class: Queries.java
	javac Queries.java

build: Queries.class

run: build
	java -cp .:mssql-jdbc-11.2.0.jre18.jar Queries

clean:
	rm *.class
