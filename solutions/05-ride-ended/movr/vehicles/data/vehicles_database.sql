CREATE DATABASE movr_vehicles;

CREATE TABLE movr_vehicles.vehicles (
    id UUID PRIMARY KEY,
    battery INT8,
    last_ride_start TIMESTAMP,
    last_ride_end TIMESTAMP,
    vehicle_info JSON,
    serial_number INT
        AS ((vehicle_info->'purchase_information'->>'serial_number' )::INT8)
        STORED
);

CREATE TABLE movr_vehicles.location_history(
    id UUID PRIMARY KEY,
    vehicle_id UUID REFERENCES movr_vehicles.vehicles(id) ON DELETE CASCADE,
    ts TIMESTAMP NOT NULL,
    longitude FLOAT8 NOT NULL,
    latitude FLOAT8 NOT NULL
);

INSERT INTO movr_vehicles.vehicles (id, battery, vehicle_info) VALUES
	('e92dbdff-ee44-47ae-9ce3-b9b4eb1f7ea6', 48, '{"color": "yellow", "purchase_information": {"manufacturer": "Upright Vehicles", "purchase_date": "2020-03-04 11:01:28", "serial_number": "10040"}, "type": "scooter", "wear": "damaged"}'),
	('a4eea24e-669f-4e5f-85fc-7e88b970d1ab', 80, '{"color": "black", "purchase_information": {"manufacturer": "Scoot Life", "purchase_date": "2020-06-19 11:38:06", "serial_number": "12651"}, "type": "scooter", "wear": "damaged"}'),
	('d511f956-83e8-48ed-aa3b-4e0dd285169f', 66, '{"color": "yellow", "purchase_information": {"manufacturer": "Upright Vehicles", "purchase_date": "2020-02-01 02:07:28", "serial_number": "12766"}, "type": "scooter", "wear": "damaged"}'),
	('541f7135-41b6-484b-b201-ddb20ed25d47', 5, '{"color": "yellow", "purchase_information": {"manufacturer": "Upright Vehicles", "purchase_date": "2020-04-03 08:35:59", "serial_number": "15958"}, "type": "scooter", "wear": "light wear"}'),
	('c7092d4f-228d-4570-aa64-576574e7f2ee', 85, '{"color": "black", "purchase_information": {"manufacturer": "Scoot Life", "purchase_date": "2020-07-05 19:13:58", "serial_number": "16360"}, "type": "scooter", "wear": "mint"}');

INSERT INTO movr_vehicles.location_history (id, vehicle_id, ts, longitude, latitude) VALUES
	('0011521c-7c34-4e48-9146-8980f6202996', 'e92dbdff-ee44-47ae-9ce3-b9b4eb1f7ea6', '2020-04-29 19:21:53+00:00', (-74.03534), 40.58763),
	('00188ad3-fc76-4446-ba5e-e7517979342d', 'a4eea24e-669f-4e5f-85fc-7e88b970d1ab', '2020-04-29 23:07:49+00:00', (-73.98715), 40.74666),
	('001a5a39-4f23-4b59-b245-cad6ddcaa808', 'd511f956-83e8-48ed-aa3b-4e0dd285169f', '2020-04-29 17:37:22+00:00', (-74.20637), 40.72875),
	('002cf0db-64cd-4c93-b728-004c5b5723ef', '541f7135-41b6-484b-b201-ddb20ed25d47', '2020-04-29 20:23:31+00:00', (-74.28585), 40.54546),
	('003813dc-1f95-46b5-b4c8-efb0ba016a3f', 'c7092d4f-228d-4570-aa64-576574e7f2ee', '2020-04-30 03:23:09+00:00', (-74.48876), 40.66873);
