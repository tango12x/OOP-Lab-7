/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend.ProgramFunctions.AdminManagement;
import backend.ProgramFunctions.UserAccountManagement.User;
/**
 *
 * @author ahmme
 */
public class Admin extends User {

    // Constructor when ID is given
    public Admin(String userId, String username, String email, String passwordHash) {
        super(userId, "admin", username, email, passwordHash);
    }

    // Constructor when ID is NOT given
    public Admin(String username, String email, String passwordHash) {
        super("admin", username, email, passwordHash);
    }

}    

