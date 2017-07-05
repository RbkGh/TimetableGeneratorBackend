#Timetable Generator 

**ROOT in this context means the application root directory**

**1.Create securityconfig.properties file with the following content:**

``` 
jwt.secret.key=WHATEVERKEYYOUWANT
```

**and copy into ROOT/src/main/resources directory** 

**2.download and extract timetableTestEnvironment1.7z from ROOT/db-backups directory**

**3.Extracted Files directory looks like this :**
```
8DEPTS_6CORE_DEPTS/
  |- timetableTestEnvironment1/
     |-the .bson and .json files are located in here .... 
  
```

**4.cd into 8DEPTS_6CORE_DEPTS directory and run the following command :**

```
mongorestore -d timetableTestEnvironment1 timetableTestEnvironment1/
```

**2.run the following command:**
```
gradle build
```

**3.finally run :**
```$xslt
gradle bootRun
```

application is now live on 

```
http://localhost:8085
```

#default username and password is:
 
 ```
 username : Kotech
 password : pass1234
 ```
