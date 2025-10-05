<img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/233e910f-eba5-4798-80a5-1822ee6ea9b7" />

Daily Dashboard
A sleek and modern desktop application built with JavaFX to help you organize your daily life. It provides a central place to view the weather, manage your tasks, take quick notes, and track your productivity, all wrapped in a custom, transparent window frame.

✨ Features
Personalized Welcome: A setup screen for first-time users to enter their name.

At-a-Glance Home Screen:

Displays a personalized greeting and the current date.

Shows current weather for Bristol, UK.

Summarizes daily task completion.

Visualizes your productivity over the last week with a chart.

Task Management:

Add new tasks to your to-do list.

Mark tasks as complete.

Filter tasks by status (All, Pending, Completed).

Delete tasks via a context menu.

Quick Notes: A dedicated area to jot down thoughts and reminders.

Productivity Analytics:

View total tasks completed, overall completion rate, and your most productive day of the week.

Persistent Data: All your tasks, notes, and productivity history are saved locally to a data.json file in your user home directory.

Modern UI:

A custom, transparent title bar with minimize, maximize, and close controls.

Smooth fade-in animations for a fluid user experience.

Clean, card-based layout.

🛠️ Technologies Used
Language: Java 17

Framework: JavaFX 17

UI Library: JFoenix 9.0.10

Build Tool: Apache Maven

JSON Parsing: Google Gson 2.10.1

APIs: OpenWeatherMap API for weather data.

🚀 Getting Started
Prerequisites
Java Development Kit (JDK) 17 or later.

Apache Maven.

An API key from OpenWeatherMap (the current one is included for testing purposes but should be replaced).

Installation & Setup
Clone the repository:

git clone [https://github.com/abdi-suufi/dailydashboard.git](https://github.com/abdi-suufi/dailydashboard.git)
cd DailyDashboard-3f01a7ce1179f79a10560ec33fcf22c3c80731ef

(Optional) Update API Key:
Open src/main/java/com/example/dailydashboard/WeatherService.java and replace the placeholder API_KEY with your own.

Build the project:

mvn clean install

Running the Application
Execute the following Maven command from the project's root directory:

mvn javafx:run

⚙️ How to Use
First Run: On your first launch, you'll be prompted to enter your name.

Main Dashboard: This is your home screen. Add tasks in the "To-Do List" card and write notes in the "Quick Notes" card. Your changes are saved automatically.

Navigation: Use the buttons on the left sidebar to navigate between Home, Analytics, Tasks, and Settings.

Settings: You can change your display name or reset all application data from the settings page. Warning: Resetting data is irreversible!
