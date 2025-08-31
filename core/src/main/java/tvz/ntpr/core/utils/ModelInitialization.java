package tvz.ntpr.core.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import tvz.ntpr.core.rest.TimeApi;

import java.time.format.DateTimeFormatter;

import static tvz.ntpr.core.utils.WinRegistry.*;

@Component
@NoArgsConstructor
public class ModelInitialization {
    private static final TimeApi TIME_API = new TimeApi();

    public static void initialize(Model model, String currentContextPath) {
        String[] dateTime = TIME_API.getCurrentTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                .split("T");
        model.addAttribute("currentDate", dateTime[0].trim());
        model.addAttribute("currentTime", dateTime[1].trim());
        String lang;
        if (isWindows())
            lang = readRegistryValue(CHANGE_LANGUAGE_REGISTRY_PATH, LANGUAGE_VALUE_NAME);
        else
            lang = readLocaleFromFile();
        model.addAttribute("language", lang);
        model.addAttribute("currentContextPath", currentContextPath);
    }
}
