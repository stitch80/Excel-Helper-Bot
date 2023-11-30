FROM amd64/amazoncorretto:17

LABEL authors="stitch80"

EXPOSE 8080

WORKDIR /usr/local/bin/

COPY build/libs/ExcelHelperBot-0.0.1-SNAPSHOT.jar telegram-bot.jar
COPY src/main/resources/RE-INV.000.xlsx RE-INV.000.xlsx
COPY healthcheck.sh healthcheck.sh

CMD ["java", "-jar", "telegram-bot.jar"]