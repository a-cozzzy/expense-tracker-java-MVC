# Expense Management System

## Overview
The Expense Management System is a web-based application for tracking and managing personal or business expenses. Built with Java and Spring Boot, it features secure user authentication, intuitive UI using Thymeleaf and Bootstrap, and supports CRUD operations with powerful filtering functionality. This app is ideal for users looking to gain better control over their spending habits and financial data.

## Technologies Used
![Language](https://img.shields.io/badge/language-Java%20-blue.svg) 
![Technologies](https://img.shields.io/badge/technologies-Spring_boot%20-green.svg)
![Technologies](https://img.shields.io/badge/technologies-Spring_MVC%20-green.svg)
![Technologies](https://img.shields.io/badge/technologies-Spring_Security%20-green.svg)
![Technologies](https://img.shields.io/badge/technologies-Spring_Data_jpa%20-green.svg)
![Technologies](https://img.shields.io/badge/technologies-Thymeleaf_&_Bootstrap%20-purple.svg)

## Features
- **User Authentication & Authorization**  
  Secure login and role-based access using Spring Security.

- **Expense Tracking with CRUD Operations**  
  Users can add, update, delete, and view their expenses.

- **Category Support & Filtering**  
  Filter expenses by category, date, and description for easier analysis.

- **Modern UI**  
  Responsive interface powered by Thymeleaf templates and Bootstrap styling.



## Design Patterns Used

### Creational Patterns

**Singleton Pattern**  
All classes annotated with `@Service`, `@Repository`, and `@Controller` are managed as singletons by the Spring container.  
This ensures a single shared instance of each bean is used across the application.  
**Used in:**  
- `ExpenseService`, `CategoryService`, `UserService` (`@Service`)  
- `ExpenseRepository`, `CategoryRepository` (`@Repository`)  
- `MainController`, `LoginController` (`@Controller`)

**Factory Method Pattern**  
Used internally by Spring Framework for dependency injection. Spring creates and injects beans without exposing the creation logic.  
**Used by:**  
- Spring container to instantiate and inject dependencies marked with `@Autowired`  
- Example: `@Autowired` constructors or fields in `ExpenseService`, `CategoryService`, etc.

---

### Structural Patterns

**Facade Pattern**  
Service classes act as facades that abstract and encapsulate complex data access and business logic, providing a cleaner API to the controller layer.  
**Used in:**  
- `ExpenseService`: Provides CRUD operations and filtering logic  
- `CategoryService`: Handles category-related business operations  
- `UserService`: Manages user-related operations

**Adapter Pattern (via DTOs)**  
Data Transfer Objects (DTOs) serve as intermediaries between the domain models and the view layer (Thymeleaf templates).  
This keeps domain entities decoupled from the UI, enhancing modularity and validation handling.  
**Used in:**  
- `ExpenseDTO`, `WebUser`, `FilterDTO`  
- Mapping between DTOs and entities handled in `ExpenseService` and controller methods

---

### Behavioral Patterns

**Template Method Pattern**  
Spring Security uses this pattern in its workflow. Custom behaviors are injected by overriding specific methods.  
**Used in:**  
- `CustomAuthenticationSuccessHandler`: Overrides `onAuthenticationSuccess()` to define post-login redirection logic

**Strategy Pattern (Implied)**  
Filtering logic in `ExpenseService.findFilterResult(FilterDTO filter)` adapts its behavior based on filter criteria.  
Though not explicitly implemented as a `Strategy` interface, this method behaves like a strategy by dynamically selecting filtering logic.  
**Used in:**  
- `ExpenseService.findFilterResult(FilterDTO filter)` for dynamically applying different filtering strategies based on user input
