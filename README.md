## Travel System
Project Assumption
1. The input csv size should be able to fit the compute instance memory, if not, we need a database for storing the file data.
2. The data has an ascending order on DateTimeUTC field.
3. The csv data may have missing fields or invalid/duplicated data.
4. The program is a simple command line program written in Java.

How to run
The project has a Dockerfile already made to easily run
```
    docker build --no-cache -t travelsystem:latest .
    docker run --name travelsystem  travelsystem:latest
    
    # for running tests
    docker run --rm -it --entrypoint /bin/bash travelsystem
    mvn test
```