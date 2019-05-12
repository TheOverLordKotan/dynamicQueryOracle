# Spring Boot RESTful, Oracle REF CURSOR and Parallel Processing

Project shows how to use Oracle REF CURSOR to dynamically gather data from Oracle. Oracle PL/SQL code is in sql/ws_ref_cursor.pkb file. 

Main features
- REF CURSOR usage gives possibility to dynamically generate output data
- Response objects are dynamically generating by Javassist (Java Programming Assistant). This solution give use possiblity to change PL/SQL code without touching Java part
- Parallel processing accelerates data gathering from database
- In test code we se how to check this type of heterogeneous solution


# How to install

Under any user in Oracle database please install package WS_REF_CURSOR_SQL (sql directory)
Setup connection to Oracle database in resources/application.yml file - url, username and password


# How to use

``` 
curl -X POST http://localhost:8770/isAlive -d OK

curl -X POST http://localhost:8770/getObjectsByName -H 'content-type: application/json' -d '[{"objectName":"DUAL"},{"objectName":"SYSTEM"}]'

curl -X POST http://localhost:8770/getObjectsByOwner -H 'content-type: application/json' -d '[{"objectOwner":"SYSTEM"}]'
``` 
