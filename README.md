# EveryBuy API Gateway

This project serves as the API Gateway for EveryBuy, providing routing and filtering for backend microservices including authentication, user management, advertisement, chat services.

## Deployment URL

http://api-everybuy.onrender.com 

## Open Api Documentation

https://app.swaggerhub.com/apis-docs/OlesiaSmahlii/EveryBuy/1.0#/

## Routes and Services

The API Gateway currently routes requests to the following services:

- **auth-service**: Authentication service hosted at `https://service-authorization-b1jx.onrender.com`
  - Routes requests with path `/auth/**`

- **user-service**: User service hosted at `https://service-user-qxpc.onrender.com`
  - Routes requests with path `/user/**`

- **advertisement-service**: Advertisement service hosted at `https://service-advertisement-r8dt.onrender.com`
  - Routes requests with path `/ad/**`
