Open Repetition Learning Platform API-Server (ORLP)
==================================================


Thank you for choosing ORLP - a high-performance platform for spased repetition learning.


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
2. Wait untill IDE will import all maven dependencies to this project;
3. Open MySQL Workbench and change the password for user **`root`** to **`123456`**;
4. Create database **`orlp`** using the next command:

```sql
    CREATE DATABASE orlp;
```
5. Create tables in the database and fill them with data - in the IDE:
-  change the parameter `jpa\hibernate\`**`ddl-auto`** in the `src\main\resources\`**`application.yml`** to **`create`**
-  **Run** the Application
-  **Run** sql script `src\main\resources\data\TestData.sql` - this will fill tables with test-data
-  change the parameter `jpa\hibernate\`**`ddl-auto`** in the `src\main\resources\`**`application.yml`** to **`validate`**

Now you can work with the API.


WHAT'S NEXT
-----------

You can check all opportunities of the API in the Swagger (it was included to the project) by using next URL:
[http://127.0.0.1:8080/swagger-ui.html](http://127.0.0.1:8080/swagger-ui.html)

-----------

***The ORLP Developer Team***
