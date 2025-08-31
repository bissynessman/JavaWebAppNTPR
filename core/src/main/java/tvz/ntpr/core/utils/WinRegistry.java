package tvz.ntpr.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class WinRegistry {
    public static final String CHANGE_LANGUAGE_REGISTRY_PATH = "HKEY_CURRENT_USER\\SOFTWARE\\NTPR\\JavaApp\\Language";
    public static final String LANGUAGE_VALUE_NAME = "lang";
    private static final String DEFAULT_LOCALE = "ENG";

    public static String readRegistryValue(String regPath, String valueName) {
        try {
            Process process = new ProcessBuilder("reg", "query", regPath, "/v", valueName)
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith(valueName)) {
                        String[] parts = line.trim().split("\\s+");
                        return parts[parts.length - 1];
                    }
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error reading registry. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean writeRegistryValue(String regPath, String valueName, String value) {
        try {
            Process process = new ProcessBuilder("reg", "add", regPath, "/v", valueName, "/t", "REG_SZ", "/d", value, "/f")
                    .redirectErrorStream(true)
                    .start();
            int exitCode = process.waitFor();

            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String initLocaleFile() {
        File targetDir = new File("target");
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        writeLocaleToFile(DEFAULT_LOCALE);
        return new File(targetDir, "locale.txt").getAbsolutePath();
    }

    private static String getLocaleFile() {
        File targetDir = new File("target");
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        return new File(targetDir, "locale.txt").getAbsolutePath();
    }

    public static String readLocaleFromFile() {
        File file = new File(getLocaleFile());
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            initLocaleFile();
        }
        return null;
    }

    public static void writeLocaleToFile(String lang) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getLocaleFile()))) {
            writer.write(lang);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
