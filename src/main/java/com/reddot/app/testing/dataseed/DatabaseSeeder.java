package com.reddot.app.testing.dataseed;

import com.reddot.app.entity.Person;
import com.reddot.app.entity.Role;
import com.reddot.app.entity.User;
import com.reddot.app.entity.enumeration.ROLENAME;
import com.reddot.app.repository.RoleRepository;
import com.reddot.app.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public DatabaseSeeder(PasswordEncoder encoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws InterruptedException {

        if (roleRepository.findAll().isEmpty()) {
            seedRole(roleRepository);
        }

        if (userRepository.findAll().isEmpty()) {
            seedUser(userRepository, roleRepository);
        }

        log.info("Fetching all roles");
        log.info("-------------------------------");
        roleRepository.findAll().forEach(
                role -> log.info(role.getName())
        );

        log.info("Fetching all users");
        log.info("-------------------------------");
        userRepository.findAll().forEach(
                user -> log.info(user.getUsername())
        );
    }

    private void seedRole(RoleRepository repository) throws InterruptedException {
        List<Role> roles = repository.findAll();
        if (!roles.isEmpty()) {
            return;
        }
        log.info("------------------------------");
        log.info("SEEDING ROLE");
        repository.save(new Role(ROLENAME.ROLE_ADMIN));
        repository.save(new Role(ROLENAME.ROLE_USER));
        repository.save(new Role(ROLENAME.ROLE_MODERATOR));
        log.info("------------------------------");
    }

    private void seedUser(UserRepository userRepository, RoleRepository roleRepository) {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {
            return;
        }

        List<User> userList = userRepository.findAll();
        if (!userList.isEmpty()) {
            return;
        }

        Role userRole = roleRepository.findByName(ROLENAME.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Role adminRole = roleRepository.findByName(ROLENAME.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        User seedUser = new User("test user", "user@localhost", encoder.encode("user"), new HashSet<>(roleList));
        // ROLE_USER for seedUser
        seedUser.setRoles(new HashSet<>(Set.of(userRole)));
        User seedAdmin = new User("test admin", "admin@localhost", encoder.encode("admin"));
        // ROLE_USER and ROLE_ADMIN for seedAdmin
        seedAdmin.setRoles(new HashSet<>(Set.of(userRole, adminRole)));

        Person p = new Person();
        p.setDisplayName("local user");
        p.setAboutMe("I am a local user.");
        p.setDob(LocalDate.now());
        p.setLastAccess(LocalDate.now());
        p.setLocation("Berlin");
        p.setWebsiteUrl("example.com");
        seedUser.setPerson(p);

        Person adPerson = new Person();
        adPerson.setDisplayName("local admin");
        adPerson.setAboutMe("I am a local admin.");
        adPerson.setDob(LocalDate.now());
        adPerson.setLastAccess(LocalDate.now());
        adPerson.setLocation("New York");
        adPerson.setWebsiteUrl("example.com");
        seedAdmin.setPerson(adPerson);

        userRepository.save(seedUser);
        userRepository.save(seedAdmin);
        log.info("{} and {} added to the database.", p.getDisplayName(), adPerson.getDisplayName());
    }
}
