package ru.gb.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.*;

import java.util.Properties;

@UtilityClass
public class ConfigUtils {

    Properties prop = new Properties();
    private static InputStream configFile;

    static {
        try {
            configFile = new FileInputStream("src/test/resources/application.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public String getBaseUrl() {
        prop.load(configFile);
        return prop.getProperty("baseUrl");
    }

}
