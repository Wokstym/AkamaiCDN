# AkamaiCDN

## Config

On linux uncomment 2 lines specified in `docker-compose.yml`

## Build

Build project with `mvn clean install` or `mvn clean install -DskipTests` to skip tests

## Run

In postgres directory run `./run.sh` script to start database

To start application run `./run.sh` in a main directory. To debug application load located in `.run` directory
`Attach Debug.run.xml`, start with earlier mentioned script application and click Debug button in right upper corner in
Intellij

In Postman send get request for `localhost:8090/ping` to ping server, date should be stored in database

