# Movie Application
**Assignment 2 + Term Project** for Programming Principles 2 course

## Introduction
This application provides a comprehensive solution for managing a personal movie database and watchlist. It features functionalities like watchlist management, data persistence, sorting/filtering options, robust exception handling, and efficient data management using advanced Java features.

## Key Features

### Watchlist Management and File Reading/Writing (4 points)
The application allows users to maintain a user-specific watchlist, persisting data across sessions. 
- **Implementation**: Utilized `WatchListDatabase` class for managing user-specific watchlists. Integrated file I/O operations to read and write watchlists, ensuring data persistence.
- **Technologies**: Java I/O streams, `FileReader` and `FileWriter` classes for handling file operations.

### Sorting/Filtering (2 points)
Implemented advanced sorting and filtering features to enhance user experience.
- **Implementation**: Sorting functionality in `BrowseMoviesPanel` allows users to sort movies based on different criteria. Filtering methods enable users to search movies within the database.
- **Technologies**: Comparator interfaces and custom sorting logic.

### Exception Handling (2 points)
Efficiently managed various error scenarios using exception handling mechanisms.
- **Implementation**: Incorporated try-catch blocks in file operations and user data handling to manage exceptions like `IOException` and `NumberFormatException`.
- **Technologies**: Java Exception Handling.

### Stream API and Lambda Functions (2 points)
Made use of Stream API and lambda expressions for data processing.
- **Implementation**: Applied Stream API in watchlist and movie database manipulations for filter, map, and reduce operations. Lambda functions used for concise and efficient data processing.
- **Technologies**: Java Stream API, lambda expressions.

### Collections Framework (2 points)
Effectively used the Collections framework for data storage and manipulation.
- **Implementation**: Utilized `ArrayList`, `HashMap`, and other collections for storing movies, users, and watchlists. Ensured efficient data access and manipulation.
- **Technologies**: Java Collections Framework.

## Test Cases Overview

The `MovieAppTest` class contains a suite of unit tests ensuring the correct functioning of various components in the Movie Application. Below is an overview of these tests:

#### Application Initialization Test
- `testAppInitialization`: Validates the proper loading of the `MovieDatabase` and the visibility of the `LoginFrame`, ensuring the application initializes as expected.

#### Movie Class Tests
- `testMovieCreation`: Confirms that a `Movie` object is correctly instantiated with the given parameters.
- `createMovie_InvalidYear_ShouldThrowException`: Expects an `IllegalArgumentException` for creating a `Movie` with an invalid release year.

#### User Class Tests
- `testUserLogin`: Checks the login functionality for both valid and invalid user credentials.
- `testUserCreation_EmptyPassword_ShouldThrowException`: Ensures that creating a `User` with an empty password throws an `IllegalArgumentException`.
- `testUserCreation_NullUsername_ShouldThrowException`: Verifies that the application throws an `IllegalArgumentException` when creating a `User` with a null username.

#### MovieDatabase Class Tests
- `testMovieDatabase`: Tests the addition and retrieval of a `Movie` in the `MovieDatabase`.
- `testRemoveMovie`: Assesses the removal functionality of a `Movie` from the `MovieDatabase`.
- `testDuplicateMovieAddition`: Ensures duplicate movies cannot be added to the `MovieDatabase`.

#### WatchListDatabase Class Tests
- `testWatchListDatabase`: Verifies the addition of movies to a user's watchlist.
- `testRemoveFromWatchlist`: Tests the removal of movies from a user's watchlist, including UI interactions with a simulated `JTable`.
- `testAddDuplicateToWatchlist`: Checks for prevention of duplicate movies in a user's watchlist.

#### CSVLoader Test
- `testCSVLoader`: Confirms the CSV file loader's functionality to populate the `MovieDatabase`.
- `testCSVLoaderFileValidation`: Verifies the CSVLoader correctly loads data from a valid CSV file into the `MovieDatabase`.

These tests play a critical role in maintaining the reliability and stability of the application, providing thorough coverage of its core functionalities.


## Contribution

| Contributor | Contributions | Contribution |
| ----------- | ------------- |  ------------- |
| Javid Magsudov    | Team Leader. Developed sorting/filtering features, integrated Stream API and lambda functions, and managed the Collections framework usage. Worked on JPanels and JFrames | 36%  |
| Sama Zeynalli  | Focused on the development of the MainFrame, various entity classes, and the CSVLoader for data import. Wrote this Report. | 32% |
| Ahad Hasanli  | Worked on the initial setup of the project and entities, implemented the file reading/writing functionality, and handled exception scenarios. Added Javadoc comments. | 32% |

## Getting Started
- Clone the repository: `git clone https://github.com/MJakoo/PP2_AS2.git`
- Navigate to the project directory and compile the code.
- Run `MovieApp` to start the application.

## Links
This is link to our [Youtube Video](https://youtu.be/vLetKfRJ8eI)
