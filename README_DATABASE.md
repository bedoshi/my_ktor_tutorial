# Database Setup Guide

## Prerequisites
- Docker and Docker Compose installed
- PostgreSQL client (optional, for direct database access)

## Quick Start

1. Copy the environment file:
```bash
cp .env.example .env
```

2. Start the database containers:
```bash
docker-compose up -d
```

3. Verify the containers are running:
```bash
docker-compose ps
```

## Services

### PostgreSQL Database
- **Host**: localhost
- **Port**: 5432
- **Database**: ktor_tutorial_db
- **Username**: ktor_user
- **Password**: ktor_password

### Adminer (Database Web UI)
- **URL**: http://localhost:8081
- **System**: PostgreSQL
- **Server**: postgres
- **Username**: ktor_user
- **Password**: ktor_password
- **Database**: ktor_tutorial_db

## Common Commands

### Start services:
```bash
docker-compose up -d
```

### Stop services:
```bash
docker-compose down
```

### Stop and remove all data:
```bash
docker-compose down -v
```

### View logs:
```bash
docker-compose logs -f postgres
```

### Access PostgreSQL CLI:
```bash
docker-compose exec postgres psql -U ktor_user -d ktor_tutorial_db
```

## Database Schema

The database is automatically initialized with the following tables:

### users table
- id (SERIAL PRIMARY KEY)
- name (VARCHAR(255) NOT NULL)
- email (VARCHAR(255) NOT NULL UNIQUE)
- age (INTEGER NOT NULL CHECK > 0)
- created_at (TIMESTAMP WITH TIME ZONE)
- updated_at (TIMESTAMP WITH TIME ZONE)

### sample_data table
- id (SERIAL PRIMARY KEY)
- data (TEXT NOT NULL)
- created_at (TIMESTAMP WITH TIME ZONE)

## Troubleshooting

### Container won't start
Check if port 5432 is already in use:
```bash
lsof -i :5432
```

### Can't connect to database
1. Check container status: `docker-compose ps`
2. Check logs: `docker-compose logs postgres`
3. Ensure .env file exists with correct values

### Reset database
```bash
docker-compose down -v
docker-compose up -d
```