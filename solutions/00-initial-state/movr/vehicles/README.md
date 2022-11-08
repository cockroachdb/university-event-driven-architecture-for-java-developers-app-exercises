## Project Overview
This is the Java API backend implementation of the MovR vehicle application. 
The application demonstrates how to integrate a Java/Spring Boot 
implementation with CockroachDB. This application can run
standalone to provide a REST API, or in conjunction with a front
end implemented with React.js. 

## Requirements

To use this application, you will need a working CockroachDB instance.

You will also need Java.

If you intend to consume events from Kafka, you will need a local Kafka instance running.

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

### Adding a new vehicle (via cURL)

```
curl -X POST --header "Content-Type: application/json" \
--data '{"vehicle_type":"Scooter","color":"Red","manufacturer":"Dynamex","serial_number":"1234","wear":"light","purchase_date":"2021-05-27","battery":"100","longitude":"75.5","latitude":"75.6"}' \
http://localhost:36257/api/vehicles
```

### Retrieving a vehicle (via cURL)

```
curl http://localhost:36257/api/vehicles/<UUID>
```

### Retreiving multiple vehicles (via cURL)

```
curl 'http://localhost:36257/api/vehicles?max_vehicles=20'
```

### Deleting a vehicle (via cURL)

```
curl -X DELETE http://localhost:36257/api/vehicles/<UUID>
```