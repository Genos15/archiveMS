## UPDATE SQL MIGRATION USING LIQUIBASE
First of all, access the root folder of the Spring services you want to update

Always re-build the maven project before update the changeSet
RUN ``$mvn liquibase:update``

BETTER RUN ``$mvn liquibase:updateSQL -Dliquibase.changesToApply=2``

## GENERATE SQL MIGRATION USING LIQUIBASE
First of all, access the root folder of the Spring services you want to update

RUN ``$mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/generated.sql``

Then will have all changes made.

Follow the [Springboot Official video](https://www.youtube.com/watch?v=YhicwD489xQ) on LIQUIBASE

## DROP ALL
``mvn liquibase:dropAll``



``mvn initialize liquibase:update``
``mvn initialize -Pliquibase``