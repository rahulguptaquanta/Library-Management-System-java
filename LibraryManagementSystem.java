import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Library LIBRARY = new Library("Skill Nexis Learning Library");

    public static void main(String[] args) {
        seedSampleBooks();
        printWelcome();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option (1-7): ");

            switch (choice) {
                case 1:
                    LIBRARY.displayBooks();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    registerUser();
                    break;
                case 6:
                    LIBRARY.displayUsers();
                    break;
                case 7:
                    running = false;
                    System.out.println("\nThank you for using the Library Management System!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose from 1 to 7.");
            }
        }
        SCANNER.close();
    }

    private static void printWelcome() {
        System.out.println("================================================");
        System.out.println("          LIBRARY MANAGEMENT SYSTEM");
        System.out.println("================================================");
        System.out.println("Welcome to " + LIBRARY.getName() + "!");
    }

    private static void printMenu() {
        System.out.println("\n------------------- MAIN MENU ------------------");
        System.out.println("1. View all books");
        System.out.println("2. Add a book");
        System.out.println("3. Borrow a book");
        System.out.println("4. Return a book");
        System.out.println("5. Register a library user");
        System.out.println("6. View registered users");
        System.out.println("7. Exit");
        System.out.println("------------------------------------------------");
    }

    private static void seedSampleBooks() {
        LIBRARY.addBook(new Book("B101", "Effective Java", "Joshua Bloch"));
        LIBRARY.addBook(new Book("B102", "Head First Java", "Kathy Sierra"));
        LIBRARY.addBook(new Book("B103", "Clean Code", "Robert C. Martin"));
    }

    private static void addBook() {
        System.out.println("\n--- Add Book ---");
        String id = readNonEmpty("Book ID: ");
        if (LIBRARY.findBook(id) != null) {
            System.out.println("A book with this ID already exists.");
            return;
        }
        String title = readNonEmpty("Book title: ");
        String author = readNonEmpty("Author name: ");
        LIBRARY.addBook(new Book(id, title, author));
        System.out.println("Book added successfully.");
    }

    private static void borrowBook() {
        System.out.println("\n--- Borrow Book ---");
        String userId = readNonEmpty("Registered user ID: ");
        User user = LIBRARY.findUser(userId);
        if (user == null) {
            System.out.println("User not found. Please register the user first (option 5).");
            return;
        }

        String bookId = readNonEmpty("Book ID to borrow: ");
        Book book = LIBRARY.findBook(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (book.borrow(user)) {
            user.addBorrowedBook(book);
            System.out.println("Book borrowed successfully by " + user.getName() + ".");
        } else {
            System.out.println("This book is currently unavailable.");
        }
    }

    private static void returnBook() {
        System.out.println("\n--- Return Book ---");
        String userId = readNonEmpty("Registered user ID: ");
        User user = LIBRARY.findUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        String bookId = readNonEmpty("Book ID to return: ");
        Book book = LIBRARY.findBook(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (book.returnBook(user)) {
            user.removeBorrowedBook(book);
            System.out.println("Book returned successfully. Thank you, " + user.getName() + ".");
        } else {
            System.out.println("This book is not currently borrowed by this user.");
        }
    }

    private static void registerUser() {
        System.out.println("\n--- Register User ---");
        String id = readNonEmpty("User ID (e.g. U101): ");
        if (LIBRARY.findUser(id) != null) {
            System.out.println("A user with this ID already exists.");
            return;
        }
        String name = readNonEmpty("Full name: ");
        String email = readNonEmpty("Email: ");
        LIBRARY.addUser(new User(id, name, email));
        System.out.println("User registered successfully.");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }
}

interface Borrowable {
    boolean borrow(User user);
    boolean returnBook(User user);
}

abstract class LibraryItem {
    private final String id;
    private final String title;

    protected LibraryItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public abstract void displayDetails();
}

class Book extends LibraryItem implements Borrowable {
    private final String author;
    private User borrowedBy;

    public Book(String id, String title, String author) {
        super(id, title);
        this.author = author;
        this.borrowedBy = null;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return borrowedBy == null;
    }

    @Override
    public boolean borrow(User user) {
        if (!isAvailable()) {
            return false;
        }
        borrowedBy = user;
        return true;
    }

    @Override
    public boolean returnBook(User user) {
        if (borrowedBy == null || !borrowedBy.getId().equals(user.getId())) {
            return false;
        }
        borrowedBy = null;
        return true;
    }

    @Override
    public void displayDetails() {
        String status = isAvailable() ? "Available" : "Borrowed by " + borrowedBy.getName();
        System.out.printf("%-8s | %-25s | %-20s | %s%n",
                getId(), getTitle(), author, status);
    }
}

class User {
    private final String id;
    private final String name;
    private final String email;
    private final List<Book> borrowedBooks = new ArrayList<>();

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }

    public void removeBorrowedBook(Book book) {
        borrowedBooks.remove(book);
    }

    public void displayDetails() {
        System.out.printf("%-10s | %-25s | %-30s | Books borrowed: %d%n",
                id, name, email, borrowedBooks.size());
    }
}

class Library {
    private final String name;
    private final List<Book> books = new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    public Library(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public Book findBook(String id) {
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }
        return null;
    }

    public User findUser(String id) {
        for (User user : users) {
            if (user.getId().equalsIgnoreCase(id)) {
                return user;
            }
        }
        return null;
    }

    public void displayBooks() {
        System.out.println("\n----------------------------- BOOK CATALOG -----------------------------");
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.printf("%-8s | %-25s | %-20s | %s%n", "Book ID", "Title", "Author", "Status");
        System.out.println("------------------------------------------------------------------------");
        for (Book book : books) {
            book.displayDetails();
        }
    }

    public void displayUsers() {
        System.out.println("\n---------------------------- REGISTERED USERS -------------------------");
        if (users.isEmpty()) {
            System.out.println("No users registered yet.");
            return;
        }
        System.out.printf("%-10s | %-25s | %-30s | %s%n",
                "User ID", "Name", "Email", "Borrowed");
        System.out.println("------------------------------------------------------------------------");
        for (User user : users) {
            user.displayDetails();
        }
    }
}
