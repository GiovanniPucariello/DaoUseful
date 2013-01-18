package daouseful.useful;

/**
 *
 * @author Daniel Röhers Moura
 */
public class Useful {

    public static void exceptionMessageConsole(Exception e) {
        System.out.println(" ========== EXCEPTION ========== ");
        System.out.println("Message: " + e.getMessage());
        System.out.println("Cause: " + e.getCause());
    }
}
