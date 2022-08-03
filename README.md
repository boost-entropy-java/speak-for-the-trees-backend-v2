# Speak for the Trees Backend

[![Build Status](https://travis-ci.org/Code-4-Community/backend-scaffold.svg?branch=master)](https://travis-ci.org/Code-4-Community/backend-scaffold)
[![Coverage Status](https://coveralls.io/repos/github/Code-4-Community/speak-for-the-trees-backend-v2/badge.svg?branch=master)](https://coveralls.io/github/Code-4-Community/speak-for-the-trees-backend-v2?branch=master)

This is the Java backend API for the [Speak for the Trees frontend](https://github.com/Code-4-Community/speak-for-the-trees-frontend). 

## Setup :wrench:
First follow all the steps for setting up the general development environment which can be found [here](https://docs.c4cneu.com/getting-started/setup-local-dev/). This will walk you through:
- Installing, creating, and running a local PostgreSQL database (`speak-for-the-trees`)
- Configuring IntelliJ
- Installing Maven
- Installing Java 8
- Configuring project properties files
- Compiling and running the API

Following these steps, all that is left is to import data into the database. This has been made easy through import routes. You can find these import routes in the Postman collection posted below. Follow the steps below in order to import all the relevant data:
- Call `POST api/v1/user/signup` to create a super admin user with a JSON body that follows the format of:
  ```json 
  {
      "username": "someUsername",
      "email": "some@email.com",
      "password": "somePassword",
      "firstName": "someFirstName",
      "lastName": "someLastName"
  }
  ```
  - Using your preferred method for postgres (Intellij, PgAdmin, postgres consol, etc.) edit the user row you just created so that their `privilege_level` column is equal to `SUPER_ADMIN`
  - Call `POST /api/v1/user/login` to login to your admin user account with a JSON body that follows the format of:
  ```json
  {
      "email": "your@email.com",
      "password": "yourPassword"
  }
  ```
  - Copy the "accessToken" returned by that call and add it as the _value_ of a header called 'X-Access-Token' for all seeding API calls
- Import data is stored in the `SFTT.import.data` S3 bucket. Ask one of the team leads for the .json files
    - Call `POST api/v1/protected/import/neighborhoods` with the contents of `neighborhoods.json` as the body
    - Call `POST api/v1/protected/import/blocks` with the contents of `blocks.json` as the body. At this point your database has been populated with neighborhood and block data. The following steps are only necessary if you need to test something related to reservations.
    - Request a SQL script that imports users in the SFTT Slack channel. This is not public due to the sensitivity of the information.
    - Call `POST api/v1/protected/import/reservations` with the contents of `reservations.json` as the body. 
    - Call `POST api/v1/protected/import/sites` with the contents of `sites.json` as the body. 

At this point your database is fully set up and contains real data for blocks, neighborhoods, users and reservations. 

## Running the API :robot:
The `ServiceMain.java` class has the main method for running the code, this can be run directly in IntelliJ. Alternatively: `mvn install` creates a jar file at: `service/target/service-1.0-SNAPSHOT-jar-with-dependencies.jar`. This can be run from the command line with the command `java -jar service-1.0-SNA....`. The API will then be available at `http://localhost:8081` by default.

## Postman Collection :rocket:
Our Postman collection is an ever expanding list of routes that can easily be called through Postman. You can find our latest collection [here](https://www.getpostman.com/collections/2f26c24b1306cecdfa56). Make sure the API is running when you try to call a route. 

## API Specification :book:
The official API specification for this backend can be found at https://docs.c4cneu.com/sftt/sftt-api-spec/. This lists every single route that is callable, explains what it does, shows the associated request body and lists the possible responses. When making changes to the backend developers should create a matching PR for the API spec reflecting their changes. 

## Database Schema :pencil:
The database schema can be found here: https://lucid.app/lucidchart/2e440718-aca6-4efa-adb1-4fc6112bcf42/view. This is an up-to-date overview of what fields the database contains. 

