package it.uniroma3.siw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private CredentialsService credentialsService;

    @Override
    public void run(String... args) throws Exception {
        if (!credentialsService.adminExists()) {
            User adminUser = new User();
            adminUser.setName("Admin");
            adminUser.setSurname("Admin");
            adminUser.setEmail("admin@example.com");

            Credentials credentials = new Credentials();
            credentials.setUsername("admin");
            credentials.setPassword("admin");
            credentials.setRole(Credentials.ADMIN_ROLE);
            credentials.setUser(adminUser);
            credentials.setMustChange(true); // Forza il cambio al primo login

            credentialsService.saveCredentials(credentials);
            
            System.out.println(credentials.getRole());
            System.out.println(Credentials.ADMIN_ROLE);
            System.out.println(Credentials.DEFAULT_ROLE);

            System.out.println("Admin creato con username/password: admin/admin");
        } else {
            System.out.println("Admin già presente, nessuna azione necessaria.");
        }
    }
}
