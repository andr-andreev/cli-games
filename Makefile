.DEFAULT_GOAL := build-run

build-run: build run

build:
	./mvnw clean package

run:
	java -jar ./target/games-1.0-SNAPSHOT-jar-with-dependencies.jar

update:
	./mvnw versions:update-properties versions:update-properties