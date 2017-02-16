# Relay

   Relay is a web api server that parse your request and fetch result from online HTTP APIs .Relay can atuo generate source for you,all you need to do is define the api definition 
configuration files.

##What it for
 It use to make online API service into as your local service for speed up and simplified 
call, also save your money by cutting down online API service call counts.

##Requirement
  This project use redis for cache.
  
##How to use

###1. Clone the project and import it to intelliJ IDEA.
###2. Define your api configuration file.
Define your api configuration file like weather.json file under /resources directory.
and append it to /src/main/java/cn/readen/relay/ApiGenerator.java  ApiGenerator.apiNames array.
###3. Generate source by run the ApiGenerator.main method.
The generated source file will be in the $baseDir/$packageName you defined.
###4. Run the server by enter "gradle appRun" in terminal.
Make sure you have a redis server run at localhost,otherwise change the redisHost 
value in the /resources/config.txt file.
###5. Enjoy.
You can make any change to  BaseController.handle method to handle with request
and response.Such as cache, saving in database and authorization.


