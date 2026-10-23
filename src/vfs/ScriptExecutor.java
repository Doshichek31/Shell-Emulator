import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScriptExecutor {
    private CommandExecutor commandExecutor;
    private ShellEmulator shellEmulator;

    public ScriptExecutor(CommandExecutor commandExecutor, ShellEmulator shellEmulator) {
        this.commandExecutor = commandExecutor;
        this.shellEmulator = shellEmulator;
    }

    // Выполняет скрипт из файла
    // Останавливается при первой ошибке
    public boolean executeScript(String scriptPath) {
        if (scriptPath == null || scriptPath.isEmpty()) {
            System.out.println("No script to execute");
            return true;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Пропускаем пустые строки и комментарии
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Выводим команду в GUI
                shellEmulator.appendOutput("> " + line + "\n");

                // Выполняем команду
                try {
                    String[] parts = line.split("\\s+");
                    String command = parts[0];

                    String result = commandExecutor.execute(command, parts);
                    shellEmulator.appendOutput(result + "\n");

                    // Если команда - exit, останавливаем выполнение
                    if (command.equals("exit")) {
                        return true;
                    }

                } catch (Exception e) {
                    // При ошибке выводим её и останавливаемся
                    shellEmulator.appendOutput("ERROR: " + e.getMessage() + "\n");
                    System.err.println("Script execution stopped due to error: " + e.getMessage());
                    return false;
                }
            }

            return true;

        } catch (IOException e) {
            shellEmulator.appendOutput("ERROR: Failed to read script: " + scriptPath + "\n");
            System.err.println("ERROR: Failed to read script: " + e.getMessage());
            return false;
        }
    }
}