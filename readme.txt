Методы:
1. giveBook(long bookId, long cardId) - Выдача книги читателю
2. validateCard(long cardId) - Проверка актуальности читательского билета
3. calculateReturnDate(int startYear, int startMonth, int startDay) - Расчёт даты возврата книги
4. watchExpiringRents(long rentId) - Просмотр записей об аренды со сроком возврата менее 2 недель
5. expireRentPeriod(long rentId) - Продлить срок аренды книги
6. watchExpiringCards(long cardId) - Просмотр временных читательских билетов с оставшимся сроком действия менее 2 недель
7. expireCardPeriod(long cardId) - Продлить срок действия временного билета


Параметры:
Для environment.properties: -Denv
Для логгера: -Dlog4j2.configurationFile

Типы Датасорсов:
CSV
XML
JDBC

Примеры команд запуска:
java -jar -Denv=./environment.properties -Dlog4j2.configurationFile=./log4j2.xml ./BiblioHub.jar XML giveBook 11 21
java -jar -Denv=./environment.properties -Dlog4j2.configurationFile=./log4j2.xml ./BiblioHub.jar XML validateCard 21
java -jar -Denv=./environment.properties -Dlog4j2.configurationFile=./log4j2.xml ./BiblioHub.jar CSV calculateReturnDate 2022 12 23
java -jar -Denv=./environment.properties -Dlog4j2.configurationFile=./log4j2.xml ./BiblioHub.jar CSV watchExpiringRents 41
java -jar -Denv=./environment.properties -Dlog4j2.configurationFile=./log4j2.xml ./BiblioHub.jar CSV expireRentPeriod 41
java -jar -Denv=./environment.properties -Dlog4j2.configurationFile=./log4j2.xml ./BiblioHub.jar JDBC watchExpiringCards 31
java -jar -Denv=./environment.properties -Dlog4j2.configurationFile=./log4j2.xml ./BiblioHub.jar JDBC expireCardPeriod 31