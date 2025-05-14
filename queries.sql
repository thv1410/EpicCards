--- UserService.java

-- SQL query to check if user exists in the database
-- Path: /register
SELECT COUNT(*) FROM user WHERE username = ?

-- SQL query to insert user into the database
-- Path: /register
INSERT INTO user (username, password) VALUES (?, ?)

-- SQL query to get the user base on username for login
-- Path: /login
SELECT * FROM user WHERE username = ?

-- SQL query to get all users
-- Path: /users
SELECT * FROM user

-- SQL query to get user from the database matching username
-- Path: /getUser
SELECT * FROM user WHERE username = ?

-- SQL query to get user's statistics from database match userID
-- Path: /getUserStatistics
SELECT user.userID, COUNT(DISTINCT category.categoryID) AS categoriesCreated,
    COUNT(DISTINCT classroom.classroomID) AS classroomsCreated, 
    COUNT(DISTINCT enrolled.classroomID) AS classroomsJoined, 
    COUNT(DISTINCT flashcard.flashcardID) AS flashcardsCreated 
    FROM user 
    LEFT JOIN category ON user.userID = category.userID 
    LEFT JOIN classroom ON user.userID = classroom.teacherID 
    LEFT JOIN enrolled ON user.userID = enrolled.studentID 
    LEFT JOIN category AS category_for_flashcards ON category_for_flashcards.userID = user.userID 
    LEFT JOIN flashcard ON flashcard.categoryID = category_for_flashcards.categoryID 
    WHERE user.userID = ? 
    GROUP BY user.userID

-- SQL query to get all users except for the one matching userID
-- Path: /getAllOtherUsers
SELECT * FROM user WHERE userID != ?

--- ClassroomService.java

-- SQL query to check if the classroom name already exists
-- Path: /createClassroom
SELECT COUNT(*) FROM classroom WHERE name = ?

-- SQL query to insert classroom into the database
-- Path: /createClassroom
INSERT INTO classroom (name, teacherID) VALUES (?, ?)

-- SQL query to check if the classroom exists
-- Path: /deleteClassroom
SELECT COUNT(*) FROM classroom WHERE classroomID = ?

-- SQL query to delete all users enrolled in class
-- Path: /deleteClassroom
DELETE FROM enrolled WHERE classroomID = ?

-- SQL query to delete classroom from the database
-- Path: /deleteClassroom
DELETE FROM classroom WHERE classroomID = ?

-- SQL query to check if the student is already in the classroom
-- Path: /joinClassroom
SELECT COUNT(*) FROM enrolled WHERE classroomID = ? AND userID = ?

-- SQL query to insert student into the classroom
-- Path: /joinClassroom
INSERT INTO enrolled (classroomID, userID) VALUES (?, ?)

-- SQL query to check if the student is in the classroom
-- Path: /leaveClassroom
SELECT COUNT(*) FROM enrolled WHERE classroomID = ? AND userID = ?

-- SQL query to delete student from the classroom
-- Path: /leaveClassroom
DELETE FROM enrolled WHERE classroomID = ? AND userID = ?

-- SQL query to get classroom base on ID
-- Path: /getClassroom
SELECT * FROM classroom WHERE classroomID = ?

-- SQL query to get all students in the classroom
-- Path: /getAllStudentsInClassroom
SELECT u.userID, u.username, u.password
FROM enrolled e
JOIN user u ON e.studentID = u.userID
WHERE e.classroomID = ?

-- SQL query to get all classrooms for a user 
-- Path: /getAllClassroomsForUser
SELECT DISTINCT c.classroomID, c.name, c.teacherID
FROM classroom c
LEFT JOIN enrolled e ON c.classroomID = e.classroomID
WHERE c.teacherID = ? OR e.studentID = ?

-- SQL query to get all classrooms except the one that the user created
-- Path: /getAllClassrooms
SELECT * FROM classroom WHERE teacherID != ?

--- FlashcardService.java

-- SQL query to insert flashcard into the database
-- PATH: /createFlashcard
INSERT INTO flashcard (term, definition, categoryID) VALUES (?, ?, ?)

-- SQL query to delete flashcard from the database
-- PATH: /deleteFlashcard
DELETE FROM flashcard WHERE flashcardID = ?

-- SQL query to get flashcard from the database matching flashcardID
-- PATH: /getFlashcard
SELECT * FROM flashcard WHERE flashcardID = ?

-- SQL query to get all flashcards from the database matching categoryID and term
-- PATH: /getAllFlashcards
SELECT * FROM flashcard WHERE categoryID = ? AND term LIKE ?

-- SQL query to get all flashcards from the database matching categoryID
-- PATH: /getAllFlashcardsFromCategory
SELECT * FROM flashcard WHERE categoryID = ?


--- CategoryService.java

-- SQL query to insert category into the database
-- PATH: /createCategory
INSERT INTO category (title, userID) VALUES (?, ?)

-- SQL query to delete all flashcard in the category matching categoryID
-- PATH: /deleteCategory
DELETE FROM flashcard WHERE categoryID = ?

-- SQL query to delete category matching categoryID
-- PATH: /deleteCategory
DELETE FROM category WHERE categoryID = ?

-- SQL query to get category from the database matching categoryID
-- PATH: /getCategory
SELECT * FROM category WHERE categoryID = ?

-- SQL query to get categories from the database matching userID and titles
-- PATH: /getAllCategories
SELECT * FROM category WHERE userID = ? AND title LIKE ?

-- SQL query to get categories from the database matching userID
-- PATH: /getAllCategoriesFromUser
SELECT * FROM category WHERE userID = ?

-- SQL query to get categories from the database matching title
-- PATH: /getAllCategoriesFromTitle
SELECT * FROM category WHERE title LIKE ?
