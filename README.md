# VaadinDemoApp
Simple app to show a few Vaadin functionalities with Many-to-Many relation.

The app uses:
* Spring Boot
* Spring JPA
* Hibernate
* Vaadin
* MySQL

App includes Customer and Meeting entities that are connected with a Many-to-Many relation.
It allows you to see a list of Customers (editable with double click on grid item), of which you are able to see their Meetings
(also editable with double click) or add a new one. Each meeting has a result (None / New meeting / Contract / Bill).

## Installation instructions
1. Clone this git repository or download it as a .zip and unpack.
2. Open it as a project in your favorite IDE. I used IntelliJ, where you can select **File > Open** and select the pom.xml of this project.
3. Set up a database in **MySQL Workbench**. The data for the database is set in **application.properties** file. You can either use my settings:
* **URL:** localhost:3306
* **Database name:** demo
* **Username:** root
* **Password:** user

... or set your own settings in **application.properties**.

4. You can now **Run** the DemoApplication in the IDE. If it builds correctly, you will find the working app on address **localhost:8080**.
