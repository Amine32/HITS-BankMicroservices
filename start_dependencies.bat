@echo off
echo Starting PostgreSQL...
"C:\Program Files\PostgreSQL\15\bin\pg_ctl.exe" -D "D:\postgreData-Bank-System" -o "-p 5433" start

echo Starting Zookeeper...
start "Zookeeper" /D "C:\Users\Amine\kafka" .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

timeout /t 10

echo Starting Kafka...
start "Kafka" /D "C:\Users\Amine\kafka" .\bin\windows\kafka-server-start.bat .\config\server.properties
