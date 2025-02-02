# Aplicación inicial ToDoList

Aplicación ToDoList para la asignatura de Acceso a Datos usando Spring Boot y plantillas Thymeleaf. Este repositorio se ha creado gracias al template repository del excelente profesor Domingo Gallardo de la Universidad de Alicante para la asignatura de [MADS](https://github.com/domingogallardo/mads-todolist-inicial).

## Requisitos

Necesitas tener instalado en tu sistema:

- Java 8

## Ejecución

Puedes ejecutar la aplicación usando el _goal_ `run` del _plugin_ Maven 
de Spring Boot:

```
$ ./mvnw spring-boot:run 
```   

También puedes generar un `jar` y ejecutarlo:

```
$ ./mvnw package
$ java -jar target/mads-todolist-inicial-0.0.1-SNAPSHOT.jar 
```

Una vez lanzada la aplicación puedes abrir un navegador y probar la página de inicio:

- [http://localhost:8080/login](http://localhost:8080/login)
