# git-stats-api
Web application for Measurement of Individual Contribution in Software Projects using Git Technology

## Setup
This is java spring boot project using gradle.
Import this project in IDE of your choice and build using gradle

## Running
Run this project in your IDE after building. To make sure project is running, test 
"ping" api with below request. You you get valid "pong" response, you project is running successfully in your local system.

##### GET REQUEST:
`` 
GET http://localhost:8080/auth/ping 
``

##### RESPONSE
``
pong
``

## API's

All API's can be accessed via swagger UI. When application is running in your local, you can access via below URL.
http://localhost:8080/swagger-ui.html