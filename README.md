# Library Management System (Java OOP)

Week 2 Java Programming mini project using object-oriented programming.

## Features
- View all books and availability
- Add books with unique IDs
- Register library users
- Borrow available books
- Return books only by the user who borrowed them
- View registered users and borrowed-book counts
- Input validation

## OOP concepts
- Classes and objects: `Book`, `Library`, `User`
- Constructors
- Inheritance: `Book extends LibraryItem`
- Polymorphism: overridden `displayDetails()`
- Abstraction: abstract class `LibraryItem`
- Interface: `Borrowable`
- Encapsulation: private fields and methods
- Access modifiers: `private`, `protected`, `public`

## Requirements
- JDK 8 or newer
- VS Code, IntelliJ IDEA, Eclipse, or terminal

## Run
```bash
javac LibraryManagementSystem.java
java LibraryManagementSystem
```

## Suggested test
1. Choose `1` to view sample books.
2. Choose `5` to register a user, for example ID `U101`.
3. Choose `3`, enter `U101`, then book ID `B101`.
4. Choose `1` to check the book's borrowed status.
5. Choose `4` to return `B101` as `U101`.
6. Choose `6` to view registered users.
7. Choose `7` to exit.
