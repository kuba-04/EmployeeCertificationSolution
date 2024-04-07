FROM eclipse-temurin:21 as build
COPY . .
RUN ./gradlew assemble

FROM eclipse-temurin:21
COPY --from=build build/libs/EmployeeCertificationSolution-*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar"]
