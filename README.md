# EmployeeCertificationSolution
This is a simple demo of CRUD app with relational DB (in memory here) build with Spring Boot.
It is a full demo consisting of the deployment to live server. The build stage is on Github Actions.

### Running the app
#### 1. locally
Add env variables listed with placeholders in `application.yml` 
then:
```bash
./gradlew build
./gradlew bootRun  -Dadmin.db.password=<password> -Dadmin.db.username=<username> -Dadmin.h2.enabled=true -Dadmin.security.allowedOrigin1=http://localhost/ -Dadmin.security.apiKey=<yourkey>
```
#### 2. on server
here we use docker, so the steps are the following:
- build the image and push to registry (github) in `build-and-push.yml` script
- set up token in github and release the package
- ssh to your server and pull the image and run:
```bash
docker pull ghcr.io/kuba-04/zalex:{imageId}
docker run -e "admin.db.password=<password>" -e "admin.db.username=<username>" -e "admin.h2.enabled=true" -e "admin.security.allowedOrigin1=*" -e "admin.security.apiKey=<your_apiKey>" --name zalex -d -p <your_server_exposed_port>:8080  <imageId>
```
### Usage
Use the requests from the example collection:
`zalex-local.postman_collection.json`