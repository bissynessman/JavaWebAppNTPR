# NTPR Project - Java Web App

A Spring Boot multi-module project consisting of three related applications in one repository:

- **api** — a REST API that acts as the interface to the database.  
- **core** — the core functionality.
- **client** — a client-side app for managing downloads via a DLL.

---

## Table of Contents

1. [Project Structure](#project-structure)  
2. [Features](#features)  
3. [Prerequisites](#prerequisites)  
4. [Build & Run](#build--run)  
5. [Dependencies](#dependencies)  
6. [Configuration](#configuration)  
9. [Troubleshooting & Notes](#troubleshooting--notes)  

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
`client` is not a module but a directory containing the client-side app for managing downloads.

---

## Features

- REST API exposing endpoints for application data.
  - Produces and uses JWTs for authenticated access to the database.
- Core services that:
  - Host web pages as user interfaces.
  - Use Cron scheduler for periodic job execution.
  - Produce digitally-signed PDFs using a PKCS#12 keystore.
- Client-side app for managing downloads.
  - Uses dynamically linked library for downloading files given a URL and bandwidth limit.

---

## Technologies

- Spring Boot
- Thymeleaf
- Maven
- iText
- Bouncy Castle
- jUnit
- jsoup
- JSON Web Token
- Lombok
- MySQL
- H2
- MyBatis
- Flyway

---

## Prerequisites

- Java 17+
- Maven 3.6+
- IntelliJ IDEA (recommended) — enable annotation processing for Lombok
- A PKCS#12 keystore (`.p12` or `.pfx`) that contains a private key and certificate chain for signing

---

## Configuration

Typical configuration points:

- `application.properties` / `application.yml` for each app (ports, database connection, keystore path, etc.).
- Different apps use different server ports (if running simultaneously):

    #### api/src/main/resources/application.properties
    `server.port=8081`

    #### core/src/main/resources/application.properties
    `server.port=8080`

- A PKCS#12 keystore under `core/src/main/resources/other`.

---

## Notes

### Client-side `ntpr://` protocol handler

- **Handler & install path:** `protocol_handler.exe` (Python→PyInstaller) is installed, by default, to `C:\Program Files (x86)\NtprProtocolHandler\protocol_handler.exe`.  
- **Protocol format:**  
  `ntpr://download?url=<percent-encoded-URL>&bandwidth=<number>` — `url` must be percent-encoded; `bandwidth` is a numeric limit (in bytes per second).
- **Registry / launch:** Installer must register the protocol so the command is exactly:  
  `"<path>\protocol_handler.exe" "%1"`  
  Wrong quoting or registering under the wrong hive will break launches.
- **Native DLL** The EXE uses a native DLL.
- **Quick local test:**
  `"C:\Program Files (x86)\ntprprotocolhandler\protocol_handler.exe" "ntpr://download?url=...&bandwidth=..."`

---

© 2025 Tim Pavić
