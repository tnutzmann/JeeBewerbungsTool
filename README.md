# JeeBewerbungsTool

## How to start the application

You can either start the application locally or remote. 
The database of the remote application is already with test data.

### Local

1. Start the Database
```shell
docker-compose -f docker-compose-dev-local.yml up -d
```
2. Start the application
```shell
mvn spring-boot:run
```

### Remote

1. Connect to the THB-Vpn
2. Start the application
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=dev-remote
```

__Test Credentials:__

| Email             | Password  | Role     |
|-------------------|-----------|----------|
| alice@test.test   | Test1234! | USER     |
| bob@test.test     | Test1234! | USER     |
| company@test.test | Test1234! | COMPANY  |


