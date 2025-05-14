-- Make the database
create database if not exists FlashcardsWeb;
use FlashcardsWeb;
-- Create the user table
CREATE TABLE IF NOT exists user (
    userID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
-- Create the classroom table
CREATE TABLE IF NOT exists classroom (
    classroomID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL, 
    teacherID INT UNSIGNED NOT NULL, 
    FOREIGN KEY (teacherID) REFERENCES user(userID)
);
-- Create the enrolled table
CREATE TABLE IF NOT exists enrolled (
    classroomID INT UNSIGNED NOT NULL,
    studentID INT UNSIGNED NOT NULL,
    PRIMARY KEY (classroomID, studentID),
    FOREIGN KEY (classroomID) REFERENCES classroom(classroomID),
    FOREIGN KEY (studentID) REFERENCES user(userID)
);
-- Create the category table
CREATE TABLE IF NOT exists category (
    categoryID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    userID INT UNSIGNED NOT NULL,
    FOREIGN KEY (userID) REFERENCES user(userID)
);
-- Create the flashcard table
CREATE TABLE IF NOT exists flashcard (
    flashcardID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    term VARCHAR(100) NOT NULL,
    definition VARCHAR(255) NOT NULL, 
    categoryID INT UNSIGNED NOT NULL,
    FOREIGN KEY (categoryID) REFERENCES category(categoryID)
);
-- Create indexing for user table on username
CREATE INDEX username_index on user(username);
-- Create indexing for flashcard table on term
CREATE INDEX name_index on classroom(name);
-- Create indexing for category table on title
CREATE INDEX title_index on category(title);
