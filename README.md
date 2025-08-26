# EveryBuy API Gateway

This project serves as the API Gateway for EveryBuy, providing routing and filtering for backend microservices including authentication, user management, advertisement, chat services.

## Deployment URL

http://api-everybuy.onrender.com 

## Open Api Documentation

https://app.swaggerhub.com/apis-docs/OlesiaSmahlii/EveryBuy/1.0#/

## Routes and Services

The API Gateway currently routes requests to the following services:

- **auth-service**: 
  - *Routes requests* with path `/auth/**`
  - *Features:* JWT token generation; User authentication
  
- **user-service**: 
  - *Routes* requests with path `/user/**`
  - *Features:* User profile management
  - 
- **advertisement-service**: 
  - *Routes* requests with path `/product/**`
  - *Features:* Product listings
  
- **chat-service**: 
  - *Routes* requests with path `/chat/**`
  - *Features:* Messaging

## Architecture Diagram

```mermaid
graph TD
    A[Client] --> B[API Gateway]
    B --> C{Auth Service}
    B --> D{User Service}
    B --> E{Advertisement Service}
    B --> F{Chat Service}
    C -->|/auth/**| G[Auth Service]
    D -->|/user/**| H[User Service]
    E -->|/product/**| I[Advertisement Service]
    F -->|/chat/**| J[Chat Service]
