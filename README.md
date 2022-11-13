# perfect-number-api

## Run Locally 
To run this api locally run 'mvn jetty:run'. Currently it is configured at port 8080.
It can be changed in pom.xml for different port

```` bash 
./callTokenGenerator.sh -k<key> -a<applicationName> -o<HttpMethod>
 example --- ./callTokenGenerator.sh -k abc -a test -o GET
 
will yield a bearer token and expiry time for the token

The secret key for this application is currently located in file SecurityFilter 
and should be moved to keypass.
 
````

## Api's 

All the given api's requires a valid token with headers as

1) Authorization
2) Token-Expiry
3) Application-Name

The 

** API to check if the number is perfect number or not 


** API to list the perfect number list for the given range



The tech stack for this api is 

Jersey : 2.26  
Java : 1.8  
Embedded jetty : 9.4
