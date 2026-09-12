# FoodMenu

FoodMenu is a web application for managing daily meal orders in a company.

It allows customers to view the available meals and place their daily orders. Administrators can manage customers, meals, menus, and orders through a dedicated administration interface.

## Technologies

* **Backend:** Java / Spring Boot
* **Frontend:** Angular
* **Database:** PostgreSQL
* **Deployment:** Docker Compose

## Project Structure

```text
+-- backend
|   +-- src
|       +-- main
|           +-- java/com/backendsoft/foodmenu
|               +-- auth
|               +-- customer
|               +-- meal
|               +-- menu
|               +-- order
|               +-- handlers
|               +-- utils
|           +-- resources
|
+-- frontend
|   +-- src
|       +-- app
|           +-- components
|           +-- guards
|           +-- interceptors
|           +-- models
|           +-- pipes
|           +-- services
|           +-- utils
|       +-- environments
|
+-- database
+-- docker-compose.yaml
+-- .env
+-- .gitignore
+-- README.md
```

## Deployment

The application is deployed using **Docker Compose**.

### 1. Configure environment variables

Create a `.env` file at the project root.

Example:

```env
ACTIVE_PROFILE=prod

POSTGRES_URL=jdbc:postgresql://postgres:5432/foodmenu
POSTGRES_USERNAME=foodmenu
POSTGRES_PASSWORD=your_password
POSTGRES_HIBERNATE_DLL=validate

RSA_PRIVATE_KEY=your_private_key
RSA_PUBLIC_KEY=your_public_key

CORS_ALLOWED_ORIGIN=https://your-domain.com
```

### 2. Start the application

From the project root:

```bash
docker compose -f docker-compose.yaml up -d --build
```

## Default User

A default administrator user is provided in the database initialization for demonstration purposes.

Use the following credentials to access the administration interface:

```text
Username: admin001
Password: dNstQ21M5ot
```

## Configuration

The backend uses environment variables for database, JWT, Spring profile, and CORS configuration.

Sensitive values such as database passwords and RSA private keys must be provided through environment variables and must not be committed to Git.
