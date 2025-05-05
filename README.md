# 📦 Product Category API

A Spring Boot-based RESTful API for managing product categories, products, and user authentication with role-based access. Includes PostgreSQL and Redis integration, currency conversion, and JWT authentication.

---

## 🚀 Getting Started

This project is containerized using **Docker** with all dependencies included (PostgreSQL + Redis).

### 🔧 Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- Java 17+
- Maven 3.9+

---

## 📦 Project Structure

```
├── src/                      # Java source files
├── postman/                  # Postman collection
├── docker-compose.yml        # Compose services: app + postgres + redis
├── Dockerfile                # Spring Boot build image
├── .env                      # Environment variables
├── schema.sql                # DB schema (auto-run)
├── data.sql                  # Sample data (auto-run)
└── README.md                 # This file
```

---

## 🐳 Run the App with Docker

1. **Build your JAR**:

```bash
./mvnw clean package
```

2. **Start the containers**:

```bash
docker-compose up --build
```

3. **Access the app**:

```
http://localhost:8080
```

4. **Environment variables** are stored in the `.env` file (used by Docker Compose).

---

## 🛢 Database & Data Init

- `schema.sql` and `data.sql` are automatically executed when the app starts (via Spring Boot + PostgreSQL config).
- If you rebuild or reset the DB container, these scripts will recreate the schema and preload sample categories, products, and users.

---

## 🔐 Authentication

- Use the `/api/users/register` and `/api/users/login` endpoints to create and login users.
- The login endpoint returns a JWT token.
- Include this token in `Authorization: Bearer <your_token>` for all protected endpoints.

---

## 📬 Postman Collection

A ready-to-use Postman collection is included:

> 📁 `postman/product-category.postman_collection.json`

### 🔄 Steps to Import:
1. Open [Postman](https://www.postman.com/).
2. Click **Import**, select the `.json` file.
3. Set these environment variables:
   - `baseUrl` → `http://localhost:8080/api`
   - `token` → Once login is successful, it will be auto-filled.

It includes:
- User registration/login
- Product & category creation, update, delete
- Currency conversion validations

---

## ⚙️ Useful Endpoints (Base: `/api`)

| Method | Endpoint                  | Access         |
|--------|---------------------------|----------------|
| POST   | `/users/register`         | Public         |
| POST   | `/users/login`            | Public         |
| GET    | `/products`               | USER, ADMIN    |
| POST   | `/products`               | ADMIN only     |
| GET    | `/categories`             | USER, ADMIN    |
| DELETE | `/categories/{id}`        | ADMIN only     |

---

## 🧩 Tech Stack

- Spring Boot 3.3
- PostgreSQL
- Redis
- MapStruct
- JWT
- Docker / Docker Compose
- JPA / Hibernate
- Maven

---

## 🧠 Extra

- DB migrations: Handled via `schema.sql` + `data.sql`.
- Soft deletes for products.
- Automatic currency conversion using Fixer API.

---

## 👨‍💻 Author

Roman Ivanov  
Feel free to fork or contribute!