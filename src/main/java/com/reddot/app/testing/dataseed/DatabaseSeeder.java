package com.reddot.app.testing.dataseed;

import com.reddot.app.entity.Person;
import com.reddot.app.entity.Role;
import com.reddot.app.entity.User;
import com.reddot.app.entity.enumeration.GENDER;
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

        seedRole(roleRepository);
        seedUser(userRepository, roleRepository);

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
        SeedingStatus("ROLE");
        log.info("------------------------------");
    }

    private void seedUser(UserRepository userRepository, RoleRepository roleRepository) throws InterruptedException {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {
            return;
        }

        List<User> userList = userRepository.findAll();
        if (!userList.isEmpty()) {
            return;
        }
        User seedUser = new User("test user", "user@localhost", encoder.encode("user"));
        User seedAdmin = new User("test admin", "admin@localhost", encoder.encode("admin"));

        Set<Role> roles = roleList.stream().collect(HashSet::new, HashSet::add, HashSet::addAll);
        seedAdmin.setRoles(roles);

        Person p = new Person();
        p.setDisplayName("local user");
        p.setBirthDate(LocalDate.now());
        p.setGender(GENDER.MALE);
        p.setPhone("1234567890");
        p.setAddress("Ho Chi Minh City, Vietnam");
        p.setBio("Reddot powerful");

        seedUser.setPerson(p);
        userRepository.save(seedUser);

        Person adPerson;
        adPerson = p;
        adPerson.setDisplayName("local admin");
        seedAdmin.setPerson(adPerson);

        userRepository.save(seedAdmin);
        SeedingStatus("USER");
        log.info("Users added to the database.");
    }

    private void SeedingStatus(String entity) throws InterruptedException {
        int totalSteps = 40;  // Total steps to complete installation
        System.out.println("SEEDING [" + entity + "] PROCESS");

        // Simulate installation process
        for (int step = 0; step <= totalSteps; step++) {
            // Print the progress bar with '#' symbols
            System.out.print("\r[");
            for (int i = 0; i < step; i++) {
                System.out.print("#");
            }
            for (int i = step; i < totalSteps; i++) {
                System.out.print(" ");
            }
            System.out.print("] " + (step * 100) / totalSteps + "%");

            // Simulate time taken for each step
            Thread.sleep(100);
        }

        System.out.println("SEEDING [" + entity + "] PROCESS COMPLETED");
    }
}
