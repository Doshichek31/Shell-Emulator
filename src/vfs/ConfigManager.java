import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class ConfigManager {
    private String vfsPath;
    private String scriptPath;
    private String configPath;

    public ConfigManager(String[] cliArgs, String configFile) {
        this.configPath = configFile;

        try {
            loadFromYaml(configFile);
        } catch (FileNotFoundException e) {
            System.err.println("WARNING: Config file not found: " + configFile);
        }

        parseCLI(cliArgs);
        dumpConfig();
    }

    private void loadFromYaml(String configFile) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        FileInputStream input = new FileInputStream(configFile);

        @SuppressWarnings("unchecked")
        Map<String, Object> data = yaml.load(input);

        if (data != null) {
            this.vfsPath = (String) data.getOrDefault("vfs_path", this.vfsPath);
            this.scriptPath = (String) data.getOrDefault("script_path", this.scriptPath);
        }

        try {
            input.close();
        } catch (Exception e) {

        }
    }

    private void parseCLI(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--vfs") && i + 1 < args.length) {
                this.vfsPath = args[i + 1];
                i++;
            } else if (args[i].equals("--script") && i + 1 < args.length) {
                this.scriptPath = args[i + 1];
                i++;
            } else if (args[i].equals("--config") && i + 1 < args.length) {
                this.configPath = args[i + 1];
                i++;
            }
        }
    }

    public void dumpConfig() {
        System.out.println("=== Configuration ===");
        System.out.println("vfs_path: " + (vfsPath != null ? vfsPath : "not set"));
        System.out.println("script_path: " + (scriptPath != null ? scriptPath : "not set"));
        System.out.println("config_path: " + (configPath != null ? configPath : "not set"));
        System.out.println("====================");
    }

    public String getConfigDump() {
        return "=== Configuration ===\n" +
                "vfs_path: " + (vfsPath != null ? vfsPath : "not set") + "\n" +
                "script_path: " + (scriptPath != null ? scriptPath : "not set") + "\n" +
                "config_path: " + (configPath != null ? configPath : "not set") + "\n" +
                "====================";
    }

    public String getVfsPath() {
        return vfsPath;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public String getConfigPath() {
        return configPath;
    }
}
