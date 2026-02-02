# Helmes Sectors Application

A full-stack application for managing sector-based applications with user authentication

## Tech Stack

### Backend (sectors-api)
- Java 25
- Spring Boot 4
- Liquibase
- PostgreSQL 18
- Gradle 9.2.1
- JWT Authentication
- TestContainers

### Frontend (sectors-ui)
- Angular 21
- TypeScript ~5.9.2

---

## Environment Variables

Create a `.env` file in the project root based on `.env.example`:

```env
# Database
POSTGRES_DB=helmes
POSTGRES_DEFAULT_SCHEMA=public
POSTGRES_USERNAME=your_username
POSTGRES_PASSWORD=your_password

# JWT
# Generate with: openssl rand -base64 32
JWT_SECRET=your-secret-key-min-32-characters-long

# Spring Profile (local | docker)
SPRING_PROFILES_ACTIVE=local
```

#### Export env variables (for local development)

Linux/macOS
```bash
export $(xargs < .env)
```

Windows (PowerShell):
```powershell
Get-Content .env | ForEach-Object { if ($_ -match '^([^=]+)=(.*)$') { [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2]) } }
```

---

## Local Development

### Prerequisites

- JDK 25
- Node.js 22+
- PostgreSQL 18
- npm

### Backend (sectors-api)

#### Build

```bash
cd sectors-api
./gradlew clean build
```

#### Run

```bash
cd sectors-api
./gradlew bootRun --args='--spring.profiles.active=local'
```

#### Tests

##### Unit tests
```bash
cd sectors-api
./gradlew clean test
```

##### Integration tests
```bash
cd sectors-api
./gradlew clean integrationTest
```

##### All tests
```bash
./gradlew check
```

#### Liquibase

To create a new changeset run createChangeDir with the next command:
```bash
# (NB! Set -Pid value with your own)
./gradlew createChangeDir -Pid=test
```

API Documentation: http://localhost:8080/swagger-ui/index.html

### Frontend (sectors-ui)

#### Install dependencies

```bash
cd sectors-ui
npm i
```

#### Run

```bash
cd sectors-ui
npm start
```

Application: http://localhost:4200

---

## Docker

### Prerequisites

- Docker
- Docker Compose

### Run all services

```bash
# Copy environment variables
cp .env.example .env

# Edit .env with your values
nano .env
 
# Start all containers
docker-compose up --build
```

### Stop services
```bash
docker-compose down
```

### Stop and remove volumes (clears database)
```bash
docker-compose down -v
```

---

## Project Structure

```
helmes-sectors/
├── .github/worflows
├── docker-compose.yml
├── .env.example
├── sectors-api/          # Spring Boot backend
│   ├── Dockerfile
│   ├── build.gradle
│   └── src/
└── sectors-ui/           # Angular frontend
    ├── Dockerfile
    ├── package.json
    └── src/
```
