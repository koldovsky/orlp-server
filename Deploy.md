Instuction for deploy.
==================================================
We use Ubuntu server to run our application, so here is description how to do it.

INSTALLATION
------------

1. On your server install mysql and create database orlp.
2. Also install jdk and jre.
3. If you want your application working on https, please install [letsencrypt](https://dzone.com/articles/spring-boot-secured-by-lets-encrypt).
4. On your local IDEA connect to database orlp on server, create all required tables and fill them with data.
5. Update your application.properties or application.yml mysql properties(**`username`** and **`password`** similar to those on the server).
6. As in this article [letsencrypt](https://dzone.com/articles/spring-boot-secured-by-lets-encrypt) for using **`https`** requests you need to add next lines to application.yml:
   ```
   server:
     port: 1443
     ssl:
       key-store: /etc/letsencrypt/live/infolve.com/keystore.p12
       key-store-password: your_password
       key-store-type: PKCS12
       key-alias: tomcat
   security:
     require-ssl: true
   ```
7. Execute command: `mvn clean install`. After that you can see `Spaced.Retition.jar` file on the package `/target`, which we will use to run out application.
8. Also you need to export this .jar to your server. Using Putty or Linux console:
    ```
     scp absolute_path/Spaced.Repetition.jar user_name@remote_host:/home/user_name/server

    ```
QUICK START
-----------
1. Now you can start your app:
    ```
    sudo java -jar server/Spaced.Repetition.jar
    ```
    Run on the background:
    ```
    sudo java -jar server/Spaced.Repetition.jar &
    ```
-----------

REMARKS
------
If you use social services like `re-captcha`, `facebook`, `google`, etc, please get keys for your host.

***The ORLP Developer Team***
- [**Volodymyr Tkachyk**](https://github.com/vldmr1703)
- [**Volodymyr Kurylo**](https://github.com/KuryloVolodymyr)
- [**Bohdan Dubyniak**](https://github.com/b0hdan)
- [**Igor Faryna**](https://github.com/IhorF)
- [**Pavelchak Andrii**](https://github.com/Pavelchak)
- [**Tamara Shyika**](https://github.com/Tamara20)
- [**Soluk Andrian**](https://github.com/SolukAndrian)


