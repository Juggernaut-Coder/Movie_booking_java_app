#!/bin/sh
classpath=".:./lib/jdbc/mysql-connector-j-8.1.0.jar:./lib/fontawesomefx/fontawesomefx-8.2.jar"
jfx_path="./modules/jfx/lib"
jfx_modules="javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web,javafx.swt"
compile() {
	javac -p $jfx_path --add-modules $jfx_modules -Xlint -cp $classpath Main.java
}

execute() {
	java -XX:+UseParallelGC -p $jfx_path --add-modules $jfx_modules -cp $classpath Main
}

deleteClass_compile() {
	find . -name "*.class" -type f -delete
	compile
}

if [ "$1" = "c" ]; then
	compile
	return 0
elif [ "$1" = "exec" ]; then
	execute
	return 0
elif [ "$1" = "dc" ]; then
	deleteClass_compile
	return 0
elif [ "$1" = "d" ]; then
	find . -name "*.class" -type f -delete
fi
