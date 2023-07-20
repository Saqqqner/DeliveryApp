FROM openjdk:17-jdk-alpine
COPY target/DeliveryApp-0.0.1-SNAPSHOT.jar /DeliveryApp-0.0.1-SNAPSHOT.jar

# Добавьте следующую строку для копирования SQL-скрипта
COPY docker-scripts/init.sql /docker-entrypoint-initdb.d/

CMD ["java", "-jar", "DeliveryApp-0.0.1-SNAPSHOT.jar"]