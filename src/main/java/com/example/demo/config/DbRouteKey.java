package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DbRouteKey {

    private final String targetId;

    private final Database database;

    private final static Integer RADIX = 24;

    public DbRouteKey() {
        this.database = Database.HOME;
        this.targetId = "0";
    }

    public DataSourceType connectionKey() {
        if (database.name().equals("COMMON") || database.name().equals("VISIT") || database.name().equals("MINI")) {
            return DataSourceType.valueOf(String.format("%s%s", database.name()));
        }

        String setNo = "001";

        long tid = getTidDeciaml(targetId);
        long mod = tid % 20 + 1;

        setNo = String.format("%03d", mod);

        return DataSourceType.valueOf(String.format("%s%s", database.name(), setNo));
    }

    private long getTidDeciaml(String targetId) {
        long total = 0L;

        if (targetId.matches("\\d+")) {
            //순수 숫자이면 변환 없다.
            total = Long.parseLong(targetId);
        } else {
            targetId = targetId.toUpperCase();
            String validValues = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int lastIndex = targetId.length() - 1;
            byte[] letters = targetId.getBytes();
            for (int i = 0; i < letters.length; i++) {
                int actualValue = validValues.lastIndexOf(letters[i]);
                if (actualValue >= 0) {
                    //System.out.println(actualValue);
                    total += (long) ((actualValue + 1) * Math.pow(RADIX, lastIndex));
                    //System.out.println(total);
                }
                lastIndex--;
            }
        }
        return total;
    }
}
