package frontend;

import backend.ProgramService.ProgramService;

/**
 * The main entry point for the application.
 * Creates the ProgramService and the first UI frame.
 */
public class Main {
    
    public static void main(String[] args) {

        ProgramService service = new ProgramService();


        java.awt.EventQueue.invokeLater(() -> {
            new Login(service).setVisible(true);
        });
    }
}