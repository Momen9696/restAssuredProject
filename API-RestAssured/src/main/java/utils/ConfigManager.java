package utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager manager;

    private static final Properties properties = new Properties();

    public ConfigManager() throws IOException {
        properties.load(new FileInputStream("resources/"  + "/config" + ".properties"));
    }

    public static ConfigManager getInstance() {
        if (manager == null) {
            synchronized (ConfigManager.class) {
                try {
                    manager = new ConfigManager();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return manager;
    }

    public String getString(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }


}