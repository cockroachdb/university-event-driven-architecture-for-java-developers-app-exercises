## Project Overview
This is the Java API backend implementation of the MovR users application. 
The application demonstrates how to integrate a Java/Spring Boot 
implementation with CockroachDB. This application can run
standalone to provide a REST API, or in conjunction with a front
end implemented with React.js. 

## Requirements

To use this application, you will need a working CockroachDB instance.

You will also need Java.

## Initializing the Database

To initialize the database, you will use the scripts located in the ../cockroach folder.

Follow the exercise instructions in Cockroach University to get set up.

## Running the Application

To run the application you can execute the `run.sh` script.

```
./run.sh
```

This will compile the application with Maven, run the tests, and execute.


## Interacting with the application

### Registering a user (via cURL)

```
curl -X POST --header "Content-Type: application/json" \
 --data '{"email":"someone@email.com", "last_name":"Smith", "first_name":"Jane", "phone_numbers":["555-1234"]}' \
 http://localhost:36257/api/users
```

### Retrieving a user (via cURL)

```
curl 'http://localhost:36257/api/users/someone@email.com'
```

### Deleting a user (via cURL)

```
curl -X DELETE 'http://localhost:36257/api/users/someone@email.com'
```

### Log in a user (via cURL)

```
curl -X POST --header "Content-Type: application/json" --data '{"email":"someone@email.com"}' http://localhost:36257/api/auth/login
```

### Log out a user (via cURL)

```
curl -X POST 'http://localhost:36257/api/auth/logout/someone@email.com'
```
