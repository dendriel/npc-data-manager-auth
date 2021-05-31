# npc-data-manager-auth
Spring boot application that provides authentication and user management.

Allows authenticating users by using username and password. Tracks user authentication by using JSON web tokens. Provides
token validation and user CRUD functionality.

## Authentication

Authentication is made by calling the following API:

- Authenticate / create a new token:
    - Request: POST ``http://localhost/authenticate``
    - Payload (body): ``{"username": "dendriel", "password": "password"}``
    - Responses:
      - Success: HTTP 200 - JSON response content: ```{"jwt": "<token>"}```
      - Failure: HTTP 401

After getting a valid token, it can be set in the Authorization header to allow the client
to make authenticated calls. Tokens last 15 minutes. The header must be set as follows:

- ``Authorization: Bearer <token>``

The token can be validated by calling the API:

- Validate a token:
    - Request: GET ``http://localhost/validate``
    - Headers
        - ``Authorization: Bearer <token>``
    - Responses:
        - Success: HTTP 200 - JSON response content: ```{"authenticated": true}```
        - Failure: HTTP 401

The validation API provides a mean to other services verify if user requests are authenticated.

## User CRUD

The user CRUD provides a way to manage users. The following APIs are available:

- Create a user:
    - Request: POST ``http://localhost``
    - Payload (body): ``{"name": "Dendriel Rozsa", "login": "dendriel", "password": "dendriel-secret", "email": "dendriel@hotmail.com"}``
    - Responses:
        - Success: HTTP 200
        - Failure: HTTP 400 - if any parameter is empty or missing: ``{"message": "Some user data is missing!"}``
        - Failure: HTTP 400 - if duplicated login: ``{"message: "User login already in use!"}``
        - Failure: HTTP 400 - if duplicated email: ``{"message: "User email already in use!"}``
- Update a user:
    - Request: PATCH ``http://localhost``
    - Payload (body): ``{"id": 2, "name": "Dendriel", "password": "new-secret"}``
        - Success: HTTP 200
        - Failure: HTTP 404 - if user not found
        - Failure: HTTP 400 - if any parameter is empty or missing: ``{"message": "Some user data is missing!"}``
- Delete a user (soft deletion):
    - Request: DELETE ``http://localhost/{id}``
    - Success: HTTP 200
    - User database entry will be set as inactive. User won't show up in get queries.
- Get a user:
    - Request: GET ``http://localhost/{id}``
    - Success: HTTP 200 - JSON response content: ``{"id": 2, "name": "Dendriel Rozsa", "login": "dendriel", "password": null, "email": "dendriel@hotmail.com", "active": true}``
    - Failure: HTTP 404 - if user not found
- Get many users:
    - Request: GET ``http://localhost?start=0&limit=10``
        - Query Parameters: start = results page start; limit = number of results per page
    - Success: HTTP 200 - JSON response content: ``[{"id": 2, "name": "Dendriel Rozsa", "login": "dendriel", "password": null, "email": "dendriel@hotmail.com", "active": true}, {...}, {...}, ...]``
- Count users:
    - Request: GET ``http://localhost/count``
    - Success: HTTP 200 - Raw response content: \<number of users\>. E.g.: ``123``


## Dockerfile

Build a docker image with:

``$ docker build -t npc-data-manager-auth .``

Run the docker image as:

``$ docker run -p 8080:8080 -e MYSQL_USER=docker -e MYSQL_PASS=pass -e MYSQL_HOST=192.168.15.9 -e MYSQL_PORT=3306 -e MYSQL_DB=the_quest npc-data-manager-auth``

All environment variables are optional and have default values. Defaults:

- MYSQL_USER=root
- MYSQL_PASS=pass
- MYSQL_HOST=localhost
- MYSQL_PORT=3306
- MYSQL_DB=npc_data

*If running a local MySQL server while testing, remember to create a user that is allowed to connect from inside the container. For instance,
you can add a user "docker" that is allowed to connect from host "%" (anywhere). Otherwise, connection issues may arise.


## Docker Compose

Docker compose allows to pre-set the container startup. Run with:

``$ docker-compose up``


## TODO

- Allow to configure token expiration time;
- Allow to refresh token expiration time;
- Create a repository impl for user.
