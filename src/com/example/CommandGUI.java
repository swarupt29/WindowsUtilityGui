package com.example;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandGUI extends JFrame {
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> commandComboBox;
    private JTextArea outputTextArea;
    private JLabel statusLabel;
    private JButton executeButton;
    private JButton clearButton;
    private JButton customButton;
    private JButton copyButton;
    private final String[] categories = {"All", "System", "Network", "File", "Power", "Maintenance", "Disk", "Advanced", "Misc"};
    private final String[] commands = {
            "System: systeminfo",
            "System: wmic bios get serialnumber",
            "System: wmic cpu get name, MaxClockSpeed, NumberOfCores",
            "System: wmic os get Caption, Version, BuildNumber",
            "System: wmic memorychip get capacity",
            "System: wmic product get name, version",
            "System: wmic useraccount list full",
            "System: msinfo32",
            "System: driverquery /v",
            "System: ver",
            "System: winver",
            "System: hostname",
            "System: whoami /all",
            "System: gpresult /r",
            "System: set",

            "Network: ipconfig /all",
            "Network: ipconfig /release",
            "Network: ipconfig /renew",
            "Network: ipconfig /flushdns",
            "Network: ipconfig /displaydns",
            "Network: ping google.com -t",
            "Network: tracert google.com",
            "Network: pathping google.com",
            "Network: nslookup google.com",
            "Network: netstat -anb",
            "Network: getmac /v",
            "Network: arp -a",
            "Network: route print",
            "Network: netsh wlan show profiles",
            "Network: netsh wlan show drivers",
            "Network: netsh interface ipv4 show config",
            "Network: netsh advfirewall show allprofiles",

            "File: dir /A",
            "File: tree /F",
            "File: copy source.txt destination_folder",
            "File: move source.txt destination_folder",
            "File: del file.txt",
            "File: rmdir OldFolder /s /q",
            "File: xcopy C:\\Source D:\\Backup /E /H /C /I",
            "File: robocopy C:\\Source D:\\Backup /MIR",
            "File: attrib",
            "File: fc file1.txt file2.txt",
            "File: mkdir NewFolder",
            "File: type file.txt",
            "File: takeown /f somefile.txt",
            "File: icacls somefile.txt",

            "Power: powercfg /list",
            "Power: powercfg /setactive SCHEME_GUID",
            "Power: powercfg /hibernate on",
            "Power: powercfg /hibernate off",
            "Power: powercfg /requests",
            "Power: powercfg /lastwake",
            "Power: powercfg /devicequery wake_armed",
            "Power: powercfg /query",
            "Power: powercfg /energy",
            "Power: powercfg /batteryreport",
            "Power: powercfg /sleepstudy",

            "Maintenance: shutdown /s /t 60",
            "Maintenance: shutdown /r /t 60",
            "Maintenance: shutdown /l",
            "Maintenance: shutdown /h",
            "Maintenance: shutdown /a",
            "Maintenance: chkdsk C: /f /r /x",
            "Maintenance: sfc /scannow",
            "Maintenance: DISM /Online /Cleanup-Image /ScanHealth",
            "Maintenance: DISM /Online /Cleanup-Image /RestoreHealth",
            "Maintenance: tasklist /svc",
            "Maintenance: taskkill /IM process.exe /F",
            "Maintenance: gpupdate /force",
            "Maintenance: wusa /uninstall /kb:XXXXXX",
            "Maintenance: cleanmgr /sageset:1",
            "Maintenance: defrag C: /U /V",

            "Disk: diskpart",
            "Disk: format D: /FS:NTFS /Q",
            "Disk: fsutil fsinfo drives",
            "Disk: fsutil volume diskfree c:",
            "Disk: wmic logicaldisk get name,size,freespace",
            "Disk: vol",
            "Disk: label",
            "Disk: compact /c",

            "Advanced: net user",
            "Advanced: net user NewUser Password123 /add",
            "Advanced: net localgroup administrators NewUser /add",
            "Advanced: net accounts",
            "Advanced: net session",
            "Advanced: net start",
            "Advanced: net stop \"service_name\"",
            "Advanced: net share",
            "Advanced: wevtutil qe System /c:10 /rd:true /f:text",
            "Advanced: schtasks /query",
            "Advanced: schtasks /create /sc daily /tn \"My Task\" /tr \"C:\\path\\to\\program.exe\"",
            "Advanced: bcdedit",
            "Advanced: reg query HKLM\\Software\\Microsoft\\Windows\\CurrentVersion\\Run",

            "Misc: findstr /i \"error\" *.log",
            "Misc: where notepad",
            "Misc: color 0a",
            "Misc: title New Command Prompt Title",
            "Misc: start notepad.exe",
            "Misc: help",
            "Misc: date /t && time /t",
            "Misc: pause"
    };
    private final String[] fileGeneratingCommands = {
        "powercfg /energy", "powercfg /batteryreport", "powercfg /sleepstudy"
    };
    // Array of commands that make system changes and require confirmation
    private final String[] riskyCommands = {
            "del", "rmdir", "format", "xcopy", "robocopy", "move",
            "shutdown", "taskkill", "powercfg /setactive", "powercfg /hibernate on",
            "chkdsk", "sfc /scannow", "DISM", "defrag",
            "net user", "net localgroup", "net stop", "net accounts",
            "schtasks /create", "schtasks /delete", "schtasks /change",
            "wusa", "gpupdate", "bcdedit", "takeown", "icacls",
            "reg add", "reg delete", "reg import"
    };

    public CommandGUI() {
        // Set FlatLightLaf look-and-feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Frame setup
        setTitle("Command Utility");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(245, 245, 250));

        // Title panel with gradient background and styled text logo
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 120, 215), 0, getHeight(), new Color(255, 165, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel titleLabel = new JLabel("Command Utility") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Add a shadow effect
                g2d.setColor(new Color(0, 0, 0, 50)); // Semi-transparent black for shadow
                g2d.drawString(getText(), 2, 22); // Shadow offset
                // Draw the main text with gradient
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(255, 255, 255, 200));
                g2d.setPaint(gp);
                g2d.setFont(getFont().deriveFont(Font.BOLD | Font.ITALIC, 28)); // Larger, bold, italic font
                g2d.drawString(getText(), 0, 20);
                super.paintComponent(g);
            }
        };
        titleLabel.setForeground(Color.WHITE); // Default color (overridden by paintComponent)
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Control panel: Category and command selection
        JPanel controlPanel = new JPanel(new BorderLayout(15, 15));
        controlPanel.setBackground(new Color(245, 245, 250));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Selection panel: Category and command combo boxes
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Category label
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        selectionPanel.add(categoryLabel, gbc);

        // Category combo box
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        categoryComboBox.setToolTipText("Filter commands by category");
        categoryComboBox.addActionListener(e -> updateCommandComboBox());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        selectionPanel.add(categoryComboBox, gbc);

        // Command label
        JLabel commandLabel = new JLabel("Command:");
        commandLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        selectionPanel.add(commandLabel, gbc);

        // Command combo box
        commandComboBox = new JComboBox<>();
        commandComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        commandComboBox.setToolTipText("Select a Windows command to execute");
        updateCommandComboBox();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        selectionPanel.add(commandComboBox, gbc);

        controlPanel.add(selectionPanel, BorderLayout.NORTH);

        // Button panel: Centered buttons with larger size and animation
        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 248, 255), 0, getHeight(), new Color(230, 240, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(135, 206, 235), 1), // Reduced to 1 pixel
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        buttonPanel.setOpaque(false); // Allow gradient to show through

        // Execute button with hover animation
        executeButton = new JButton("Execute") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 100, 200));
                } else {
                    g2d.setColor(getBackground());
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        executeButton.setFont(new Font("Arial", Font.BOLD, 16));
        executeButton.setBackground(new Color(0, 120, 215));
        executeButton.setForeground(Color.WHITE);
        executeButton.setFocusPainted(false);
        executeButton.setToolTipText("Run the selected command");
        executeButton.setPreferredSize(new Dimension(180, 40));
        executeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Subtle internal padding
        executeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                executeButton.setBackground(new Color(0, 100, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                executeButton.setBackground(new Color(0, 120, 215));
            }
        });
        try {
            executeButton.setIcon(new ImageIcon("resources/icons/execute.png"));
        } catch (Exception e) {
            executeButton.setText("Execute");
        }
        buttonPanel.add(executeButton);

        // Clear button with hover animation
        clearButton = new JButton("Clear") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(200, 200, 200));
                } else {
                    g2d.setColor(getBackground());
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setBackground(new Color(220, 220, 220));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFocusPainted(false);
        clearButton.setToolTipText("Clear the output area");
        clearButton.setPreferredSize(new Dimension(180, 40));
        clearButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Subtle internal padding
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearButton.setBackground(new Color(200, 200, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                clearButton.setBackground(new Color(220, 220, 220));
            }
        });
        try {
            clearButton.setIcon(new ImageIcon("resources/icons/clear.png"));
        } catch (Exception e) {
            clearButton.setText("Clear");
        }
        buttonPanel.add(clearButton);

        // Custom command button with hover animation
        customButton = new JButton("Custom Command") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 130, 120));
                } else {
                    g2d.setColor(getBackground());
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        customButton.setFont(new Font("Arial", Font.BOLD, 16));
        customButton.setBackground(new Color(0, 150, 136));
        customButton.setForeground(Color.WHITE);
        customButton.setFocusPainted(false);
        customButton.setToolTipText("Enter a custom command to execute");
        customButton.setPreferredSize(new Dimension(180, 40));
        customButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Subtle internal padding
        customButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                customButton.setBackground(new Color(0, 130, 120));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                customButton.setBackground(new Color(0, 150, 136));
            }
        });
        try {
            customButton.setIcon(new ImageIcon("resources/icons/custom.png"));
        } catch (Exception e) {
            customButton.setText("Custom Command");
        }
        buttonPanel.add(customButton);

        // Copy button with hover animation
        copyButton = new JButton("Copy") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(200, 200, 200));
                } else {
                    g2d.setColor(getBackground());
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        copyButton.setFont(new Font("Arial", Font.BOLD, 16));
        copyButton.setBackground(new Color(220, 220, 220));
        copyButton.setForeground(Color.BLACK);
        copyButton.setFocusPainted(false);
        copyButton.setToolTipText("Copy output to clipboard");
        copyButton.setPreferredSize(new Dimension(180, 40));
        copyButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Subtle internal padding
        copyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                copyButton.setBackground(new Color(200, 200, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                copyButton.setBackground(new Color(220, 220, 220));
            }
        });
        try {
            copyButton.setIcon(new ImageIcon("resources/icons/copy.png"));
        } catch (Exception e) {
            copyButton.setText("Copy");
        }
        buttonPanel.add(copyButton);

        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        add(controlPanel, BorderLayout.NORTH);

        // Output panel with gradient background
        JPanel outputPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 248, 255), getWidth(), 0, new Color(245, 245, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        outputPanel.setLayout(new BorderLayout());
        outputPanel.setBackground(new Color(245, 245, 250));
        outputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(135, 206, 235), 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        // Output text area
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(null, "Command Output", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), new Color(0, 120, 215)));
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        // Status label with animated loading
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        outputPanel.add(statusLabel, BorderLayout.SOUTH);

        add(outputPanel, BorderLayout.CENTER);

        // Action listeners
        executeButton.addActionListener(e -> executeCommand((String) commandComboBox.getSelectedItem()));
        clearButton.addActionListener(e -> {
            outputTextArea.setText("");
            statusLabel.setText("Output cleared");
        });
        customButton.addActionListener(e -> promptCustomCommand());
        copyButton.addActionListener(e -> {
            String outputText = outputTextArea.getText().trim();
            if (!outputText.isEmpty()) {
                StringSelection selection = new StringSelection(outputText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                statusLabel.setText("Output copied to clipboard");
            } else {
                statusLabel.setText("No output to copy");
            }
        });
    }

    private void updateCommandComboBox() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        commandComboBox.removeAllItems();
        for (String command : commands) {
            if (selectedCategory.equals("All") || command.startsWith(selectedCategory + ":")) {
                commandComboBox.addItem(command);
            }
        }
    }

    private void executeCommand(String selectedCommand) {
        if (selectedCommand == null) {
            statusLabel.setText("No command selected");
            return;
        }
        String command = selectedCommand.startsWith("Custom: ") ? selectedCommand.substring(8) : selectedCommand.split(": ")[1];
        
        // Check if the command is in the risky list
        boolean isRisky = false;
        for (String riskyCmd : riskyCommands) {
            if (command.trim().startsWith(riskyCmd)) {
                isRisky = true;
                break;
            }
        }

        // If risky, show a confirmation dialog
        if (isRisky) {
            int response = JOptionPane.showConfirmDialog(this,
                    "Warning: This command can make significant changes to your system or requires administrator privileges.\n\"" + command + "\"\n\nAre you sure you want to proceed?",
                    "Risky Command Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (response != JOptionPane.YES_OPTION) {
                statusLabel.setText("Execution cancelled by user.");
                return; // Stop execution if user cancels
            }
        }

        outputTextArea.setText("");
        statusLabel.setText("Executing");

        // Enhanced animation with pulsing effect
        Timer timer = new Timer(400, e -> {
            String currentText = statusLabel.getText();
            switch (currentText) {
                case "Executing":
                    statusLabel.setText("Executing.");
                    break;
                case "Executing.":
                    statusLabel.setText("Executing..");
                    break;
                case "Executing..":
                    statusLabel.setText("Executing...");
                    break;
                case "Executing...":
                    statusLabel.setText("Executing");
                    break;
                default:
                    if (!currentText.startsWith("Executing")) {
                        ((Timer)e.getSource()).stop();
                    }
                    break;
            }
        });
        timer.start();

        new SwingWorker<String, Void>() {
            private int exitCode = -1;
            private String filePath = null;
            private boolean isFileGenerated = false;
            
            @Override
            protected String doInBackground() throws Exception {
                // Check if command generates a file
                for (String fileCmd : fileGeneratingCommands) {
                    if (command.startsWith(fileCmd)) {
                        filePath = command.contains("powercfg /energy") ? "energy-report.html" :
                                   command.contains("powercfg /batteryreport") ? "battery-report.html" :
                                   "sleepstudy-report.html";
                        break;
                    }
                }

                // Execute command
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
                pb.redirectErrorStream(true);
                Process process = pb.start();
                StringBuilder output = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                }
                exitCode = process.waitFor();

                if (filePath != null) {
                    Path path = Paths.get(filePath);
                    if (Files.exists(path)) {
                        isFileGenerated = true;
                    }
                }
                return output.toString();
            }

            @Override
            protected void done() {
                timer.stop();
                try {
                    String output = get();
                    outputTextArea.setText(output);
                    
                    if (exitCode == 0) {
                        statusLabel.setText("Command executed successfully.");
                    } else {
                        statusLabel.setText("Command failed (Exit Code: " + exitCode + "). Admin privileges may be required.");
                    }

                    if (filePath != null) {
                        if (isFileGenerated) {
                            Path path = Paths.get(filePath);
                            int response = JOptionPane.showConfirmDialog(CommandGUI.this,
                                    "Report generated at: " + path.toAbsolutePath() + "\nDo you want to open it now?",
                                    "File Generated", JOptionPane.YES_NO_OPTION);
                            if (response == JOptionPane.YES_OPTION) {
                                Desktop.getDesktop().open(path.toFile());
                            }
                        } else {
                             statusLabel.setText(statusLabel.getText() + " Report file was not found.");
                        }
                    }
                    
                } catch (Exception ex) {
                    outputTextArea.setText("Error: " + ex.getMessage());
                    statusLabel.setText("Error executing command.");
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void promptCustomCommand() {
        String customCommand = JOptionPane.showInputDialog(this,
                "Enter a custom Windows command:",
                "Custom Command",
                JOptionPane.PLAIN_MESSAGE);
        if (customCommand != null && !customCommand.trim().isEmpty()) {
            executeCommand("Custom: " + customCommand.trim());
        } else {
            statusLabel.setText("No custom command entered.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize and show the main frame first
            CommandGUI gui = new CommandGUI();
            gui.setVisible(true);
            
            // Show welcome popup on top as a modal dialog
            JOptionPane.showMessageDialog(gui,
                    "Welcome to Command Utility!\n\nDeveloped by: github.com/swarupt29",
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}