import database.Database;

import java.sql.Connection;

public class Application {
    public static void main(String[] args) {
        Database database = Database.getInstance();
    }
}
