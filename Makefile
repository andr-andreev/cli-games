.DEFAULT_GOAL := build-run

build-run: build run

build: clear
	./mvnw package

run:
	java -jar ./target/games-1.0-SNAPSHOT-jar-with-dependencies.jar

update:
	./mvnw versions:update-properties
	./mvnw versions:display-plugin-updates

clear:
	 ./mvnw clean