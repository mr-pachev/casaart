package casaart.emails_clients_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailsClientsDbApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailsClientsDbApplication.class, args);

        // Хард код за отваряне на локална папка от компютъра за даден проект
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(EmailsClientsDbApplication.class, args);

    }

}
