public class CommandExecutor {
    private ShellEmulator gui;
    private ConfigManager config;

    public CommandExecutor(ShellEmulator gui) {
        this(gui, null);
    }
    public CommandExecutor(ShellEmulator gui, ConfigManager config) {
        this.gui = gui;
        this.config = config;
    }
    public void setConfigManager(ConfigManager config) {
        this.config = config;
    }

    public String execute(String command, String[] parts) {
        switch (command.toLowerCase()) {
            case "ls":
                return executeLs(parts);
            case "cd":
                return executeCd(parts);
            case "conf-dump":
                return executeConfDump(parts);
            case "exit":
                return executeExit(parts);
            default:
                return "ERROR: Unknown command: " + command;
        }
    }

    private String executeLs(String[] parts) {
        if (parts.length > 1) {
            StringBuilder args = new StringBuilder();

            for (int i = 1; i < parts.length; i++) {
                args.append(parts[i]);
                if (i < parts.length - 1) {
                    args.append(" ");
                }
            }
            return "ls: " + args.toString();
        } else {
            return "file1.txt  file2.txt  folder1";
        }
    }

    private String executeCd(String[] parts) {
        if (parts.length <= 1) {
            return "ERROR: cd requires a directory argument\n" +
                    "Usage: cd <directory>";
        }

        String directory = parts[1];
        return "cd: " + directory;
    }

    private String executeConfDump(String[] parts) {
        if (config != null) {
            return config.getConfigDump();
        } else {
            return "Configuration not loaded";
        }
    }

    private String executeExit(String[] parts) {
        String message = "Exiting VFS Shell Emulator...";
        System.out.println(message);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        System.exit(0);
        return "";
    }
}