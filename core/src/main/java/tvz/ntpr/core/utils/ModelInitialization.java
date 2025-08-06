package tvz.ntpr.core.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import tvz.ntpr.core.rest.TimeAPI;

import static tvz.ntpr.core.utils.WinRegistry.*;

@Component
@NoArgsConstructor
public class ModelInitialization {
    public static void initialize(Model model, String currentContextPath) {
        TimeAPI timeService = new TimeAPI();
        String[] dateTime = timeService.getCurrentTime().split("\n");
        model.addAttribute("currentDate", dateTime[0].trim());
        model.addAttribute("currentTime", dateTime[1].trim());
        String lang = readRegistryValue(CHANGE_LANGUAGE_REGISTRY_PATH, LANGUAGE_VALUE_NAME);
        model.addAttribute("language", lang);
        model.addAttribute("currentContextPath", currentContextPath);
    }
}
