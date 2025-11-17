package backend.JsonDatabaseManager;

import backend.ProgramFunctions.StudentManagement.Student;
import backend.ProgramFunctions.UserAccountManagement.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class UsersDatabase {

    private String fileName;
    private ArrayList<User> users;

    public UsersDatabase(String fileName) {
        this.fileName = fileName.strip();
        this.users = new ArrayList<>();
    }

    public ArrayList<User> getRecords() {
        return this.users;
    }

    public void readFromFile() throws FileNotFoundException {
        this.users.clear();
        File file = new File(this.fileName);
        Scanner fileReader = new Scanner(file);
        while (fileReader.hasNextLine()) {
            String record = fileReader.nextLine().strip();
            if (record.isEmpty()) {
                continue;
            }
            String[] recordArray = record.split(",");
            String userId = recordArray[0].strip();
            String role = recordArray[1].strip();
            String username = recordArray[2].strip();
            String email = recordArray[3].strip();
            String passwordHash = recordArray[4].strip();

            if ("student".equalsIgnoreCase(role)) {
                this.users.add(new Student(userId, username, email, passwordHash));
            }
        }
        fileReader.close();
    }

    public boolean isExistInDatabase(String userId) {
        for (User user : this.users) {
            if (user.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public User getUser(String userId) {
        for (User user : this.users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User newUser) {
        if (!isExistInDatabase(newUser.getUserId())) {
            this.users.add(newUser);
        }
    }

    public void removeUser(User oldUser) {
        users.removeIf(user -> user.getUserId().equals(oldUser.getUserId()));
    }

    public void writeInFile() throws IOException {
        try {
            this.users.sort(Comparator.comparing(user -> Integer.parseInt(user.getUserId())));
        } catch (NumberFormatException e) {
            this.users.sort(Comparator.comparing(User::getUserId));
        }

        FileWriter fileWriter = new FileWriter(this.fileName, false); // Overwrite file
        for (User user : this.users) {
            fileWriter.write(userToLineRepresentation(user) + "\n");
        }
        fileWriter.close();
    }

    private String userToLineRepresentation(User user) {
        return String.join(",",
                user.getUserId(),
                user.getRole(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash()
        );
    }

    public int generateId() {
        int maxId = 0;
        for (User user : this.users) {
            try {
                int currentId = Integer.parseInt(user.getUserId());
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                // Ignore non-numeric userIds for ID generation
            }
        }
        return maxId + 1;
    }

    public void sortById() {
         try {
            this.users.sort(Comparator.comparing(user -> Integer.parseInt(user.getUserId()), Comparator.reverseOrder()));
        } catch (NumberFormatException e) {
            this.users.sort(Comparator.comparing(User::getUserId).reversed());
        }
    }
}
