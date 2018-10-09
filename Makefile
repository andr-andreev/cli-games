.DEFAULT_GOAL := build-run

build-run: build run

build: clear
	./mvnw package

run:
	java -jar ./target/games-1.0-SNAPSHOT-jar-with-dependencies.jar

clear:
	 ./mvnw clean