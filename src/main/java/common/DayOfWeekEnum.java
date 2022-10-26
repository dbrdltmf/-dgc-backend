package common;

public enum DayOfWeekEnum {
    월("0"),             // 0
    화("1"),            // 1
    수("2"),          // 2
    목("3"),           // 3
    금("4"),             // 4
    토("5"),           // 5
    일("6")              // 6
    ;

    DayOfWeekEnum(String week) {
        this.value = week;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
