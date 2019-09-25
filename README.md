# Social-Network
[![Build Status](https://travis-ci.org/Train4Game/Social-Network.svg?branch=master)](https://travis-ci.org/Train4Game/Social-Network)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/65bb5369231c42dd81c61e8adb49bc3f)](https://www.codacy.com/manual/Train4Game/Social-Network?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Train4Game/Social-Network&amp;utm_campaign=Badge_Grade)

[DEVELOPING] Social Network on Java using Spring

# Instalation
1) Set environment variable `SOCIAL_ROOT` with link on root folder of project.
2) Setup PostgreSQL 11 or more.
3) Configure settings in `src/main/resources/settings.properties` and `src/main/resources/db/postgres.properties` **OR** setup tomcat properties:

    Property | Description
    -------- | -----------
    postgresUsername | Database username
    postgresPassword | Database password
    email | Email login
    password | password login
    recaptchaSecret | Recaptcha Secret code
    recaptchaHtml | Recaptcha HTML code

4) To launch app you need Maven and Java 11.
5) Run in root folder commands:
    ```
    $ mvn package
    $ mvn cargo:run
    ```
