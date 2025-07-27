---

## Windows Utility GUI 🖥️✨

Welcome to the Windows Utility GUI project! 🚀 This is a modern, user-friendly Java-based desktop application designed to execute a wide range of Windows command-line utilities with an intuitive graphical interface. This tool simplifies system administration, network diagnostics, file management, and more. 💻

## 📋 Table of Contents

##### 1. Features
##### 2. Technology Stack
##### 3. Getting Started
##### 4. Screenshots
##### 5. Usage
##### 6. Project Structure
##### 7. Contributing

## ⚡ Features

- **Category-Based Commands**: Organize commands into categories like System, Network, File, Power, Maintenance, Disk, Advanced, and Misc. 📂
- **Interactive UI**: Sleek design with gradient backgrounds, hover animations, and a responsive layout built using Java Swing and FlatLaf. 🎨
- **Command Execution**: Run commands like `ipconfig`, `chkdsk`, `powercfg`, and custom inputs with real-time output display. ⚡
- **Safety Checks**: Prompts confirmation for potentially risky commands (e.g., `del`, `format`, `shutdown`). 🔒
- **File Generation Support**: Handles commands that generate reports (e.g., `powercfg /batteryreport`) with options to open files. 📄
- **Clipboard Integration**: Copy command output to the clipboard with a single click. 📋
- **Custom Commands**: Input and execute any valid Windows command. 🎛️
- **Welcome Popup**: Displays a credit popup on launch, developed by github.com/swarupt29. 🎉

## 💻 Technology Stack

### Core Technologies:

- **Java**: The primary programming language (JDK 11 or higher recommended). ☕
- **Java Swing**: For building the graphical user interface. 🖱️
- **FlatLaf**: A modern look-and-feel library for enhanced UI aesthetics. 🌈

### Dependencies:

- **FlatLaf Library**: Required for the custom UI theme. Download from [FlatLaf GitHub](https://github.com/JFormDesigner/FlatLaf). 📦

### Operating System:

- **Windows**: Designed and tested for Windows environments. 🪟

## 🚀 Getting Started

Follow these steps to set up the Windows Utility GUI on your local machine.

### 1. Clone the Repository
Clone this repository to your local machine using the following command:
```plaintext
git clone https://github.com/swarupt29/WindowsUtilityGui.git
```
### 2\. Install Dependencies

**FlatLaf**: Add the FlatLaf JAR to your project. Download from FlatLaf Releases and include it in your IDE or classpath.

### 3\. Build and Run the Application

1.  **Using an IDE**: Open the project in IntelliJ, Eclipse, or your preferred IDE, and run 'CommandGUI.java' as a Java application.

2.  **Via Command Line**:

```plaintext
javac -cp path/to/flatlaf.jar src/com/example/CommandGUI.java
```
```plaintext
java -cp path/to/flatlaf.jar com.example.CommandGUI
```

### 4\. Launch the Application

Upon running, the main window will appear with a welcome popup on top. Dismiss the popup to start using the tool. ✅

## 📸 Screenshots

Here are some screenshots to showcase the application. 

### Welcome

![Image](https://github.com/user-attachments/assets/dd12194c-9a62-4d3f-8f9b-fe6333ace8ce)

### Main Dashboard

![Image](https://github.com/user-attachments/assets/f55a3c0b-c262-4f87-8dde-8aacc392c353)

### Categories

![Image](https://github.com/user-attachments/assets/2322c918-8a9b-45cc-a736-ef4d66651e3f)

### Commands

![Image](https://github.com/user-attachments/assets/ed030c41-9f6a-4eaf-a194-b338c1352540)

### Sample Execution

![Image](https://github.com/user-attachments/assets/17356e14-090c-465b-abf0-a25dfe6af3d0)

## 📱 Usage

##### **1. Launch the App:** Run the application, and the welcome popup will appear on top of the main window. 🎉

##### **2. Select Category:** Choose a category (e.g., "Network" or "System") from the dropdown menu to filter available commands. 📋

##### **3. Execute Commands:** Select a command from the list or use the "Custom Command" button to input a command, then click "Execute" to view the output in the text area. 🔧

##### **4. Interact with Output:** Use the "Clear" button to reset the output area, the "Copy" button to copy the output to the clipboard, or explore additional features as needed. 📋

##### **5. Safety Confirmation:** For risky commands (e.g., `del`, `format`), a confirmation dialog will appear—proceed only if you are certain. 🔐

## 🗂️ Project Structure

Here is the structure of the project:

```plaintext
WindowsUtilityGui/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── CommandGUI.java
├── README.md
```

## 🤝 Contributing

Contributions are always welcome!
