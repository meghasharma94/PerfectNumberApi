# perfect-number-api

## Run Locally 

1) Download/clone the project
2) Import in Eclipse
3) Run Configuration -> Goals "clean install jetty:run"
```` bash 
./callTokenGenerator.sh -k<key> -a<applicationName> -o<HttpMethod>
 example --- ./callTokenGenerator.sh -k abc -a test -o GET
 
will yield a bearer token and expiry time for the token

The secret key for this application is currently located in file SecurityFilter 
and should be moved to keypass.
 
````

## Api's 

All the given api's requires a valid token with headers as

1) Authorization Bearer **************************************
2) Token-Expiry 2022-11-11T18:03:49Z
3) Application-Name ******
 

** API to check if the number is perfect number or not
http://localhost:8081/api/perfect-number/validate/10

** API to list the perfect number list for the given range
http://localhost:8081/api/perfect-number/listAllPerfectNumbers?from=1&to=20

The tech stack for this api is 

Jersey : 2.26  
Java : 1.8  
Embedded jetty : 9.4
