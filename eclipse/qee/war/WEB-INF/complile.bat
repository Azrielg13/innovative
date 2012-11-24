@echo off
cd src
javac -Xlint:unchecked -classpath .;"../../../../common/lib/servlet-api.jar";"../../../../common/lib/commons-dbcp-1.2.1.jar";"../../../../common/lib/commons-collections-3.1.jar";"../../../../common/lib/commons-pool-1.2.jar";../lib/iText.jar -d ../classes com/digitald4/util/CompileMe.java
pause