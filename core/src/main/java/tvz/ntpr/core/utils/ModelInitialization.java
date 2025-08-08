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
    public static void initialize(Model model, String currentContextPath) {
        TimeApi timeService = new TimeApi();
        String[] dateTime = timeService.getCurrentTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                .split("T");
        model.addAttribute("currentDate", dateTime[0].trim());
        model.addAttribute("currentTime", dateTime[1].trim());
        String lang = readRegistryValue(CHANGE_LANGUAGE_REGISTRY_PATH, LANGUAGE_VALUE_NAME);
        model.addAttribute("language", lang);
        model.addAttribute("currentContextPath", currentContextPath);
    }
}
