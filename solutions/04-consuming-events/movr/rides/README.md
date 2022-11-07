## Project Overview
This is the Java API backend implementation of the MovR rides application. 
The application demonstrates how to integrate a Java/Spring Boot 
implementation with CockroachDB. This application can run
standalone to provide a REST API, or in conjunction with a front
end implemented with React.js.
 
## Requirements

To use this application, you will need a working CockroachDB instance. In order to publish events to Kafka via CDC, you will need access to the enterprise CDC.

You will also need Java.

If you intend to publish events to Kafka via CDC, you will need a local Kafka instance running.

## Initializing the Database

To initialize the database, you will use the scripts located in the ../cockroach folder.

Follow the exercise instructions in Cockroach University to get set up.

## Running the Application

To run the application you can execute the "run.sh" script.

```
./run.sh
```

This will compile the application with Maven, run the tests, and execute.

## Interacting with the application

### Start a ride (via cURL)

```
curl -X POST --header "Content-Type: application/json" --data '{"email":"<email>", "vehicle_id":"<vehicleId>"}' 'http://localhost:36257/api/rides/start'
```

### End a ride (via cURL)

```
curl -X POST --header "Content-Type: application/json" --data '{"email":"<email>", "vehicle_id":"<vehicleId>"}' 'http://localhost:36257/api/rides/end'
```

### Retrieving the active ride by user/vehicle (via cURL)

```
curl 'http://localhost:36257/api/rides/active?email=<email>&vehicle_id=<vehicleId>'
```

### Retrieve all rides for a user (via cURL)

```
curl 'http://localhost:36257/api/rides?email=<email>'
```

### Start Ride Event Change Feed (via Cockroach Terminal)

```
SET CLUSTER SETTING kv.rangefeed.enabled = true;
CREATE CHANGEFEED FOR TABLE events INTO 'kafka://localhost:29092' WITH full_table_name;
```
