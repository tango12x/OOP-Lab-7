package backend.JsonDatabaseManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UsersDatabase {

    public UsersDatabase() {
    }


    //CLASS ATTRIBUTES
    private String fileName;
    private ArrayList<Student> students;

    //CLASS CONSTRUCTOR
    public StudentDB(String fileName) {
        this.fileName = fileName.strip();
        this.students = new ArrayList<>();
    }

    //GETTERS FOR THE RECORDS
    public ArrayList<Student> getRecords() {
        return this.students;
    }

    //METHOD TO READ FROM THE FILE
    public void readFromFile() throws FileNotFoundException {
        this.students.clear();
        File file = new File(this.fileName);
        Scanner fileReader = new Scanner(file);
        while (fileReader.hasNextLine()) {
            String record = fileReader.nextLine().strip();
            String[] recordArray = record.split(",");
            int id = Integer.parseInt(recordArray[0].strip());
            String fullName = recordArray[1].strip();
            int age = Integer.parseInt(recordArray[2].strip());
            String gender = recordArray[3].strip().toLowerCase();
            String department = recordArray[4].strip();
            float gpa = Float.parseFloat(recordArray[5].strip());
            this.students.add(new Student(id, fullName, age, gender, department, gpa));
        }
        fileReader.close();
    }

    //METHOD TO TELL IF THE STUDENT IS INSIDE THE DB OR NOT USING ID
    public boolean isExistInStudentDatabase(int id) {
        for (int i = 0; i < this.students.size(); i++) {
            if (this.students.get(i).getId() == id) {
                return true;
            }
        }
        return false;
    }

    //METHOD TO RETURN THE STUDENT IF EXIST IN THE DB
    public Student getStudent(int id) {
        for (int i = 0; i < this.students.size(); i++) {
            if (this.students.get(i).getId() == id) {
                return this.students.get(i);
            }
        }
        return null;
    }

    //METHOD TO ADD THE STUDENT
    public void addStudent(Student newStudent) {
        if (!isExistInStudentDatabase(newStudent.getId())) {
            this.students.add(newStudent);
        }
    }

    //METHOD TO REMOVE THE STUDENT
    public void removeStudent(Student oldStudent) {
        if (isExistInStudentDatabase(oldStudent.getId())) {
            this.students.remove(oldStudent);
        }
    }

    //METHOD TO WRITE THE RECORDS IN THE FILE
    public void writeInFile() throws IOException {
        students.sort(Comparator.comparing(Student::getId));
        FileWriter fileWriter = new FileWriter(this.fileName);
        for (int i = 0; i < this.students.size(); i++) {
            fileWriter.write(this.students.get(i).lineRepresentation());
        }
        fileWriter.close();
    }

    //METHOD TO GENERATE A UNIQUE ID
    public int generateId() {
        int maxId = 0;
        for (int i = 0; i < this.students.size(); i++) {
            if (this.students.get(i).getId() > maxId) {
                maxId = this.students.get(i).getId();
            }
        }
        return maxId + 1;
    }

    //METHOD TO SORT THE STUDENTS BY THE ID
    public void sortById() {
        this.students.sort(Comparator.comparing(Student::getId).reversed());
    }

    //METHOD TO SORT THE STUDENTS BY THE ID
    public void sortByGPA() {
        this.students.sort(Comparator.comparing(Student::getGPA).reversed());
    }

}