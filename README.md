Open Repetition Learning Platform API-Server (ORLP)
==================================================


Thank you for choosing ORLP - a high-performance platform for spaced repetition learning.


INSTALLATION
------------

Please download & unpack zip-file under a Web-accessible directory. You shall see the following files and directories:

| Element |Description |
| ------ | ------ |
| src/ |source-code|
|.gitignore|file with list of ignored files for uploading to the git|
|pom.xml|Maven's project object model file|
|README.md|this file|


REQUIREMENTS
------------

The minimum requirement by ORLP is that your Web server supports [Apache Tomcat 8.5.11](https://tomcat.apache.org/download-80.cgi) or above, 
[MySQL Server 5.7](https://dev.mysql.com/downloads/mysql/) or above. 


QUICK START
-----------

1. You should import this project in your IDE as a maven-project;
2. Wait until IDE will import all maven dependencies to this project;
3. Open MySQL Workbench and change the password for user **`root`** to **`123456`**;

Now you can work with the API.

DB Migration Tool
-----------------

This project uses [Flyway DB Migration Tool](https://flywaydb.org/).
All migrations are located in the package **`resource/db/migrations`**.
Manual how to use migrations:
[https://flywaydb.org/documentation/migrations](https://flywaydb.org/documentation/migrations#sql-based-migrationsx)

WHAT'S NEXT
-----------

You can check all opportunities of the API in the Swagger (it was included to the project) by using next URL:
[http://127.0.0.1:8080/swagger-ui.html](http://127.0.0.1:8080/swagger-ui.html)

-----------

***The ORLP Developer Team***
- [**Petro Zadorovskyi**](https://github.com/zadorovskyi)
- [**Yevhen Palamarchuk**](https://github.com/YevhenPalamarchuk/)
- [**Sasha Verenka**](https://github.com/OleksandrVerenka)
- [**Yaroslav Varshavskyj**](https://github.com/jarkinV)
- [**Orest Potsiluiko**](https://github.com/OrestPotsiluiko)
- [**Vlad Kuhivchak**](https://github.com/Vkiro)
- [**Askold Baran**](https://github.com/askoldbaran)
- [**Taras Predzymyrskyi**](https://github.com/tararas124)

***The DevOps Team***
- [**Oleksandr Holovko**](https://github.com/oholovko)
- [**Zakhar Salamin**](https://github.com/zakharSal)
- [**Sofia Mykytuk**](https://github.com/SofiiaMyk)
- [**Zenoviy Kharkhalis**](https://github.com/ZenykK)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Node JS](https://nodejs.org/uk/) - The web framework used
* [Wercker](http://www.wercker.com/) - CI/CD framework used
