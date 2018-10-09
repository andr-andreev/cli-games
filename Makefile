.DEFAULT_GOAL := build-run

build-run: build run

build: compile
	jar cfe ./target/games.jar games.Slot -C ./target/classes .

compile: clear
	mkdir -p ./target/classes
	javac -d ./target/classes ./src/main/java/games/Slot.java

run:
	java -cp ./target/classes games.Slot

clear:
	rm -rf ./target