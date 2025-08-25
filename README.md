# Final Project - Java Web App

A Spring Boot multi-module project consisting of three related applications in one repository:

- **api** — a REST API that acts as the interface to the database.  
- **core** — the core functionality.
- **client** — a client-side app for executing tasks on users machine.

---

## Table of Contents

1. [Project Structure](#project-structure)
2. [Features](#features)
3. [Technologies](#technologies)
4. [Prerequisites](#prerequisites)
5. [Configuration](#configuration)
6. [Notes](#notes)

---

## Project Structure

```
JavaWebAppNTPR/
├── api/
│ ├── src/
│ └── pom.xml
├── core/
│ ├── src/
│ └── pom.xml
└── client/
  ├── source/
  ├── bin/
  └── compile scripts/
```

Each module has its own `pom.xml` and can be run independently.
`client` is not a module but a directory containing the client-side app for remote-prompted local execution.

---

## Features

- REST API exposing endpoints for application data.
  - Produces and uses JWTs for authenticated access to the database.
- Core services that:
  - Host web pages as user interfaces.
    - Login/Signup
    - Student overview
    - Professor overview
    - Administrative actions
      - Professor authorization
      - User account manipulation
      - New course input
    - New Grade input
  - Use Cron scheduler for periodic job execution.
  - Generate PDFs with detached digital signatures using a PKCS#12 keystore.
    - Download generated PDFs along with their detached signature as a .zip archive
- Client-side app
  - If ran by an implemented custom protocol prompts user to input a bandwidth limit and downloads files from a URL supplied by the protocol.
  - If ran by user prompts them to select a PDF file and it's detached signature to verify validity.

---

## Technologies

- Spring Boot
- Thymeleaf
- Maven
- iText
- jUnit
- jsoup
- JSON Web Token
- Lombok
- MySQL
- H2
- MyBatis
- Flyway
- Tkinter
- curl
- OpenSSL

---

## Prerequisites

- Java 17+
- Maven 3.6+
- Python 3.0+
- Windows 8+

---

## Configuration

Typical configuration points:

- `application.properties` for each app (ports, database connection, etc.).
- Different apps use different server ports:

    #### api/src/main/resources/application.properties
    `server.port=8081`

    #### core/src/main/resources/application.properties
    `server.port=8080`

- A PKCS#12 keystore under `core/src/main/resources/other`.
- A corresponding `cert.pem` file under `client/bin`.

---

## Notes

### Client-side `ntpr://` protocol handler

- **Handler & install path:** `ntpr_handler.exe` is installed, by default, to `C:\Program Files (x86)\NTPR\ntpr_handler.exe` alongside `cert.pem`.  
- **Protocol format:**  
  `ntpr://download?url=<URL>` — `<URL>` must be percent-encoded.
- **Registry / launch:** Installer must register the protocol so the command is exactly:  
  `"<path>\protocol_handler.exe" "%1"`  
  Wrong quoting or registering under the wrong hive will break launches.
- **Native DLL** The EXE uses a native DLL requiring minimum version of Windows: Windows 8 (WINVER, _WIN32_WINNT = 0x0602).
- **Quick local test:**
  `"C:\Program Files (x86)\NTPR\ntpr_handler.exe" "ntpr://download?url=..."`

---

© 2025 Tim Pavić
