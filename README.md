# Traffic-event-api
Traffic Management Springboot API 

Spring boot rest api to store and process traffic events with the help of Apache geode (in-memory data structure) as a data management.

1. Using docker image for apache geode
 
 please run the following command in gitbash or terminal
 
  a. pull apache geode docker image
     
	 docker pull apachegeode/geode   
  b. run geode docker image 
	
	docker run -it -p 10334:10334 -p 40404:40404 -p 1099:1099 -p 7070:7070 -p 7575:7575 apachegeode/geode
	
the above command will reach gsfh mode, where please run the below 2 commands

  c. Start locator
  
	start locator --name=LocatorOne --log-level=config --hostname-for-clients=localhost
	
  d. Start server
  
	start server --name=ServerOne --log-level=config --hostname-for-clients=localhost
	
2. Run Springboot application

	Please go to the project folder

	mvn spring-boot:run 
	or
	java -jar event-0.0.1-SNAPSHOT.jar (I already have the jar of the application path)
	

Please visit the swagger page to find out the REST-API endpoints http://localhost:9191/swagger-ui/index.html

1. http://localhost:9191/events/save to store and process the events
2. http://localhost:9191/violations/summary 
3. http://localhost:8080/violations/payFine/{violationId} to cross check the fineSummary

Technologies involved,

1. SpringBoot Rest Application with Async 
2. Used Spring geode
3. Used Spring validation,
4. Used global exception handling
5. Used Spring AOP
6. Used OpenAPI for Swagger
7. Used Docker file and Docker compose for containerization
8. Developed unit test cases using Mockito

FYI :
I already implement the docker file and docker-compose.yaml , you can find in the project , where I found some issues with ssl configurations.
I push the project and working on to fix the issue, then

Just run the below commands to run the application from the project directory

Build docker image : $ docker-compose build

Run API: $ docker-compose up
