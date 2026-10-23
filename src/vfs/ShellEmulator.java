import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShellEmulator extends JFrame {

    private JTextArea outputArea;
    private JTextField inputField;
    private CommandExecutor executor;
    private ConfigManager config;
    private ScriptExecutor scriptExecutor;

    public ShellEmulator(String[] args) {
        String configFile = "config.yaml";
        config = new ConfigManager(args, configFile);

        setTitle("VFX - Shell Emulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        outputArea.setText("Welcome to VFS Shell Emulator\n" +
                "Type your commands below\n" +
                "Type 'exit' to quit\n\n");
        JScrollPane scrollPane = new JScrollPane(outputArea);

        inputField = new JTextField();
        inputField.setFont(new Font("Courier New", Font.PLAIN, 12));

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String commandLine = inputField.getText();

                if (!commandLine.isEmpty()) {
                    processCommand(commandLine);
                    inputField.setText("");
                }
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.SOUTH);
        add(mainPanel);

        executor = new CommandExecutor(this, config);
        scriptExecutor = new ScriptExecutor(executor, this);
        setVisible(true);
        String scriptPath = config.getScriptPath();
        if (scriptPath != null && !scriptPath.isEmpty()) {
            outputArea.append("Executing startup script: " + scriptPath + "\n\n");
            boolean success = scriptExecutor.executeScript(scriptPath);
            if (!success) {
                outputArea.append("\nScript execution failed, continuing in interactive mode\n");
            }
        }
    }

    private void processCommand(String commandLine) {
        outputArea.append("> " + commandLine + "\n");

        try {
            String[] parts = commandLine.trim().split("\\s+");
            if (parts.length == 0) {
                return;
            }

            String command = parts[0];
            String result = executor.execute(command, parts);
            outputArea.append(result + "\n");

        } catch (Exception e) {
            outputArea.append("ERROR: " + e.getMessage() + "\n");
        }
    }

    public void appendOutput(String text) {
        outputArea.append(text);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShellEmulator(args));
    }
}
