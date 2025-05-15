package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataUtils {

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static String formatarData(LocalDateTime dataHora) {
        return dataHora.format(FORMATADOR);
    }

    public static LocalDateTime parseData(String dataHoraStr) {
        return LocalDateTime.parse(dataHoraStr, FORMATADOR);
    }
}
