package io.roach.movrapi.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Map of vehicle info fields used to take flat map of fields on input and remap to nested JSON as stored in the database
 */

public class VehicleInfoDTO {

    ObjectMapper objectMapper = new ObjectMapper();
    /*
    format in database:
        {
           "color":"red",
           "purchase_information":{
              "manufacturer":"Scoot Life",
              "purchase_date":"2019-10-31 01:45:18",
              "serial_number":"11847"
           },
           "type":"scooter",
           "wear":"mint"
        }
     */

    public static final String COLOR = "color";
    public static final String PURCHASE_INFORMATION = "purchase_information";
    public static final String MANUFACTURER = "manufacturer";
    public static final String PURCHASE_DATE = "purchase_date";
    public static final String SERIAL_NUMBER = "serial_number";
    public static final String VEHICLE_TYPE = "type";
    public static final String WEAR = "wear";

    Map<String, Object> fields = new HashMap<>();


    public VehicleInfoDTO(NewVehicleDTO newVehicleDTO) {

        Map<String, Object> purchaseInfo = new HashMap<>();
        purchaseInfo.put(MANUFACTURER, newVehicleDTO.getManufacturer());
        purchaseInfo.put(PURCHASE_DATE, newVehicleDTO.getPurchaseDate());
        purchaseInfo.put(SERIAL_NUMBER, newVehicleDTO.getSerialNumber());

        put(COLOR, newVehicleDTO.getColor());
        put(VEHICLE_TYPE, newVehicleDTO.getVehicleType());
        put(WEAR, newVehicleDTO.getWear());
        put(PURCHASE_INFORMATION, purchaseInfo);

    }

    public Object put(String key, Object value) {
        return fields.put(key, value);
    }

    public VehicleInfoDTO(Map<String, Object> vehicleInfo) {
        fields = vehicleInfo;
    }

    public String getAsJsonString() {
        try {
            return objectMapper.writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
