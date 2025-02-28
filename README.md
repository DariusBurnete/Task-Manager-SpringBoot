# Task Manager SpringBoot
## With PostgreSQL
 
1. Create the SpringBoot project using [Spring Initializr](https://start.spring.io/)

2. Copy the files you don't have from src > main to your project's src > main

3. In src > main > resources there is a file "application.properties" in which to put the following setup :

      spring.application.name=name-of-application -> name of the folder in which is src;  
      spring.datasource.url=jdbc:postgresql://localhost:5432/name-of-database?currentSchema=name-of-schema  
      spring.datasource.username=database-username  
      spring.datasource.password=database-password  
      spring.datasource.driver-class-name=org.postgresql.Driver  
      spring.jpa.properties.hibernate.default_schema=name-of-schema  
      spring.jpa.hibernate.ddl-auto=update  
      spring.jpa.show-sql=true  
      spring.thymeleaf.prefix=classpath:/templates/  
      spring.thymeleaf.suffix=.html  

4. Make sure that in the main folder (manager in this case), in pom.xml, you have all the dependencies.

5. Run KeyGen in src > main > java > com.task.manager > jwt once to create your secret key.

6. Directly in the main folder (manager in this case), create a .env file that contains JWT_SECRET_KEY=your_secret_key

7. .gitignore must contain main_folder_name(manager in this case)/.env
