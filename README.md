# ✈️ Travel Booking Manager

A command-line Java application for searching, booking, and managing travel tickets, all from the terminal.

![Language](https://img.shields.io/badge/language-Java-orange)
![Interface](https://img.shields.io/badge/interface-CLI-lightgrey)
![Build](https://img.shields.io/badge/build-Gradle-blue)

## 📖 Overview

Travel Booking Manager is a console-based application that lets users sign up, log in, search for available trips, book tickets, and manage their bookings through a simple text menu. [Add one or two sentences about why you built it, e.g. a learning project for OOP, file handling, or JSON storage.]

## ✨ Features

- User sign up and login [remove if not implemented]
- Search trips by source and destination
- Book a ticket and select a seat
- View all your bookings
- Cancel a booking
- Data saved locally [in JSON files / text files / a database]
- [Add any other features]

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java [17+] |
| Interface | Command Line (Terminal) |
| Build Tool | Gradle |
| Data Storage | [JSON files (Jackson) / File I/O / MySQL] |
| Libraries | [e.g. Jackson, jBCrypt, remove if unused] |

## 📂 Project Structure

```
Travel-Booking-Manager/
├── app/
│   ├── build.gradle
│   └── src/main/java/ticket/booking/
│       ├── App.java            # Entry point (main menu)
│       ├── entities/           # Models: User, Ticket, Trip, etc.
│       ├── service/            # Business logic
│       └── util/               # Helper classes
├── gradle/
├── gradlew
├── gradlew.bat
└── settings.gradle
```

> Update the folders and file names above to match your actual `ticket/booking` package.

## 🚀 Getting Started

### Prerequisites

- [JDK 17](https://adoptium.net/) or higher (check with `java -version`)
- Git

### Installation

```bash
# Clone the repository
git clone https://github.com/Jugal1011/Travel-Booking-Manager.git

# Move into the project folder
cd Travel-Booking-Manager
```

### Run the application

**Linux / macOS**
```bash
./gradlew run --console=plain
```

**Windows**
```bash
gradlew.bat run --console=plain
```

> `--console=plain` keeps Gradle's output from interfering with the app's input prompts.

### Build only

```bash
./gradlew build
```

## 💻 Usage

When the app starts, you'll see a menu like this:

```
Running Travel Booking Manager
Choose option:
1. Sign up
2. Login
3. Search Trips
4. Book a Ticket
5. View My Bookings
6. Cancel Booking
7. Exit
```

Type the number of your choice and press **Enter**. Follow the on-screen prompts.

### Example flow

1. Choose **Sign up** and create an account.
2. Choose **Login** with your credentials.
3. Choose **Search Trips**, then enter source and destination.
4. Choose **Book a Ticket** and pick a seat.
5. Choose **View My Bookings** to confirm.

## 🔮 Future Improvements

- Database integration (MySQL / SQLite)
- Payment simulation
- Admin panel for managing trips
- Input validation and better error handling
- Unit tests with JUnit

**Jugal**
- GitHub: [@Jugal1011](https://github.com/Jugal1011)
