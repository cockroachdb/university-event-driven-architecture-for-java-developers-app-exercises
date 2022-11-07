package io.roach.movrapi.util;

import io.roach.movrapi.dto.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestHelpers {
    private static Random rnd = new Random();

    public static String createString(String prefix) {
        return prefix + rnd.nextInt(100000);
    }

    public static String createEmail() {
        return createString("email")+"@fake.com";
    }

    public static UUID createVehicleId() {
        return UUID.randomUUID();
    }

    public static MessagesDTO createMessagesDTO() {
        return new MessagesDTO(new String[]{createString("Message")});
    }

    public static Map<String, Object> createArbitraryMap() {
        Map<String, Object> map = new HashMap<>();
        int numElems = rnd.nextInt(5);

        if(numElems == 0) {
            map.put("innerMap", createArbitraryMap());
        } else {
            for (int i = 0; i < numElems; i++) {
                map.put("key" + i, "value" + i);
            }
        }
        return map;
    }
}
