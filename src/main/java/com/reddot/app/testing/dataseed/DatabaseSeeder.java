package com.reddot.app.testing.dataseed;

import com.github.javafaker.Faker;
import com.reddot.app.dto.request.RegisterRequest;
import com.reddot.app.entity.*;
import com.reddot.app.entity.enumeration.ROLENAME;
import com.reddot.app.entity.enumeration.VOTETYPE;
import com.reddot.app.repository.*;
import com.reddot.app.service.user.UserServiceManager;
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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BadgeRepository badgeRepository;
    private final TagRepository tagRepository;
    private final VoteTypeRepository voteTypeRepository;
    private final QuestionRepository questionRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserServiceManager userServiceManager;

    public DatabaseSeeder(PasswordEncoder encoder, UserRepository userRepository, RoleRepository roleRepository, BadgeRepository badgeRepository, TagRepository tagRepository, VoteTypeRepository voteTypeRepository, QuestionRepository questionRepository, VoteRepository voteRepository, CommentRepository commentRepository, BookmarkRepository bookmarkRepository, UserServiceManager userServiceManager) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.badgeRepository = badgeRepository;
        this.tagRepository = tagRepository;
        this.voteTypeRepository = voteTypeRepository;
        this.questionRepository = questionRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.userServiceManager = userServiceManager;
    }


    @Override
    public void run(String... args) {

        if (roleRepository.findAll().isEmpty()) {
            seedRole();
        }

        if (badgeRepository.findAll().isEmpty()) {
            seedBadge();
        }

        if (tagRepository.findAll().isEmpty()) {
            seedTag();
        }

        if (voteTypeRepository.findAll().isEmpty()) {
            seedVoteType(voteTypeRepository);
        }

        if (userRepository.findAll().isEmpty()) {
            seedUser();
        }

        log.info("");
        log.info("####### SEEDING COMPLETED #######");
        log.info("<<<<<<< GETTING ALL ROLEs");
        roleRepository.findAll().forEach(role ->
                log.info(role.getName()));

        log.info("<<<<<<< GETTING ALL BADGEs");
        userRepository.findAll().forEach(user ->
                log.info(user.getUsername()));
    }

    private void seedVoteType(VoteTypeRepository repository) {
        log.info("");
        log.info("########## SEEDING VOTE TYPE ##########");
        repository.save(new VoteType(VOTETYPE.UPVOTE));
        repository.save(new VoteType(VOTETYPE.DOWNVOTE));
        log.info("2 VOTE TYPES added to the database.");
    }

    /**
     * Seed the user and related data to the database
     */
    private void seedUser() {
        log.info("");
        log.info("########## SEEDING USER ##########");
        // seed user from the service
        userServiceManager.userCreate(new RegisterRequest("dev", "toannguyen.fordev@gmail.com", "Dev1234@"));
        log.info("TEST CREDENTIALS: dev - Dev1234@");

        log.info("<<<<<<< GETTING USER ROLEs");
        Role userRole = roleRepository.findByName(ROLENAME.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Role adminRole = roleRepository.findByName(ROLENAME.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        log.info("<<<<<<< GETTING NEWBIE BADGE");
        Badge badge = badgeRepository.findByName("Newbie")
                .orElseThrow(() -> new RuntimeException("Error: Badge is not found."));

        // seed user from the repository
        log.info(">>>>>>> INITIALIZING USER DATA");
        Faker faker = new Faker();
        String username = "test user";
        String email = faker.internet().emailAddress();
        String raw = "user";
        String password = encoder.encode(raw);
        String admin = "test admin";
        String adminEmail = faker.internet().emailAddress();
        String raw2 = "admin";
        String adminPassword = encoder.encode(raw2);


        // seed the user
        log.info(">>>>>>>> SEEDING TEST USER");
        User seedUser = new User(username, email, password);
        seedUser.setRoles(new HashSet<>(Set.of(userRole)));
        seedUser.addBadge(badge);
        addPerson(faker, seedUser);

        // seed the admin user
        log.info(">>>>>>>>> SEEDING TEST ADMIN USER");
        User seedAdmin = new User(admin, adminEmail, adminPassword);
        seedAdmin.setRoles(new HashSet<>(Set.of(userRole, adminRole)));
        seedAdmin.addBadge(badge);
        addPerson(faker, seedAdmin);

        userRepository.save(seedUser);
        userRepository.save(seedAdmin);
        log.info("USERS ADDED TO THE DATABASE");
        log.warn("DO NOT USE THESE USERS IN PRODUCTION");
        log.info("{} - {} - {}", seedUser.getUsername(), seedUser.getEmail(), raw);
        log.info("{} - {} - {}", seedAdmin.getUsername(), seedAdmin.getEmail(), raw2);

        log.info("");
        log.info("########## SEEDING QUESTION ##########");
        log.info(">>>>>>> SEED USER ASKs QUESTION");
        Set<Tag> tags = new HashSet<>();
        tags.add(tagRepository.findByName("java")
                .orElseThrow(() -> new RuntimeException("Error: Tag is not found.")));
        tags.add(tagRepository.findByName("spring")
                .orElseThrow(() -> new RuntimeException("Error: Tag is not found.")));
        tags.add(tagRepository.findByName("jpa")
                .orElseThrow(() -> new RuntimeException("Error: Tag is not found.")));
        Question question = new Question();
        question.setBody(faker.lorem().paragraph());
        question.setTitle(faker.lorem().sentence() + "?");
        tags.forEach(question::addTag);
        seedUser.ask(question);
        questionRepository.save(question);

        // Bookmark
        log.info("");
        log.info("########## SEEDING BOOKMARK ##########");
        log.info(">>>>>>> USER BOOKMARKs THE QUESTION");
        Bookmark bookmark = new Bookmark(seedUser, question);
        bookmarkRepository.save(bookmark);

        log.info("");
        log.info("########## SEEDING VOTE ##########");
        log.info(">>>>>>> ADMIN VOTEs THE QUESTION");
        VoteType upvote = voteTypeRepository.findByType(VOTETYPE.UPVOTE)
                .orElseThrow(() -> new RuntimeException("Error: VoteType is not found."));
        Vote vote = new Vote();
        vote.setVoteType(upvote);
        vote.setQuestion(question);
        vote.setUser(seedAdmin);
        voteRepository.save(vote);

        log.info("");
        log.info("########## SEEDING COMMENT ##########");
        log.info(">>>>>>>>>> ADMIN COMMENT ON THIS QUESTION");
        Comment comment = new Comment();
        comment.setText(faker.lorem().sentence());
        comment.setQuestion(question);
        seedAdmin.comment(comment);
        commentRepository.save(comment);


        log.info(">>>>>>>> TEST USER FOLLOWs TEST ADMIN");
        seedUser.follow(seedAdmin);
        userRepository.save(seedUser);
    }

    /**
     * Add person information to the user
     *
     * @param faker com.github.javafaker.Faker
     * @param user  User
     */
    private void addPerson(Faker faker, User user) {
        Person person = new Person();
        person.setDisplayName(faker.name().fullName());
        person.setAboutMe(faker.lorem().sentence());
        person.setDob(LocalDate.now());
        person.setLocation(faker.address().fullAddress());
        person.setWebsiteUrl(faker.internet().url());
        user.setPerson(person);
    }

    /**
     * Seed the tag table
     */
    private void seedTag() {
        List<String> tags = List.of(
                "java", "spring", "spring-boot", "spring-security", "hibernate", "jpa", "thymeleaf", "html", "css", "javascript", "jquery", "react",
                "angular", "vuejs", "nodejs", "expressjs", "mongodb", "mysql", "postgresql", "mariadb", "docker", "kubernetes", "jenkins", "git", "github", "gitlab", "bitbucket",
                "maven", "gradle", "intellij-idea", "eclipse", "netbeans", "visual-studio-code", "atom", "sublime-text", "vim", "emacs", "android", "ios", "kotlin", "swift", "react-native",
                "flutter", "ionic", "xamarin", "phonegap", "cordova", "pwa", "webassembly", "webgl", "unity3d", "unreal-engine", "blender", "gimp", "inkscape", "photoshop", "illustrator",
                "figma", "sketch", "xd", "zeplin", "invision", "jira", "trello", "asana", "slack", "discord", "microsoft-teams", "zoom", "google-meet", "webex", "skype", "whatsapp", "telegram",
                "signal", "facebook", "twitter", "instagram", "linkedin", "pinterest", "tiktok", "snapchat", "youtube", "twitch", "netflix", "amazon-prime", "hbo", "disney-plus", "spotify",
                "apple-music", "google-play-music", "deezer", "soundcloud", "tunein", "pandora", "youtube-music", "amazon-music", "shazam", "google-play-store", "apple-app-store",
                "microsoft-store", "amazon-app-store", "google-cloud", "aws", "azure", "heroku", "digitalocean", "linode", "vultr", "upcloud", "cloudflare", "fastly", "akamai", "cloudfront",
                "s3", "rds", "ec2", "lambda", "sqs", "s3", "route-53", "elasticache", "dynamodb", "cloudwatch", "cloudtrail", "cloudformation", "cloudfront", "cloudsearch", "cloudhsm");
        log.info("");
        log.info("########## SEEDING TAG ##########");
        Faker faker = new Faker();
        tags.forEach(name -> {
            Tag tag = new Tag();
            tag.setName(name);
            tag.setDescription(faker.lorem().sentence());
            tagRepository.save(tag);
        });
        log.info("{} TAGs added to the database.", tags.size());
    }

    /**
     * Seed the badge table
     */
    private void seedBadge() {
        List<Badge> commonBadges = List.of(
                new Badge("Newbie", "Just joined the community"),
                new Badge("Beginner", "Posted your first question"),
                new Badge("Intermediate", "Posted 10 questions"),
                new Badge("Advanced", "Posted 100 questions"),
                new Badge("Expert", "Posted 1000 questions"),
                new Badge("Crazy", "Posted 10000 questions"),
                new Badge("Enthusiast", "Posted 100000 questions"),
                new Badge("Loyal", "Posted 1000000 questions"),
                new Badge("Veteran", "Posted 10000000 questions"),
                new Badge("Legend", "Posted 100000000 questions"));

        List<Badge> featureBadges = List.of(
                new Badge("Good Question", "Your question has been upvoted 10 times"),
                new Badge("Great Question", "Your question has been upvoted 100 times"),
                new Badge("Awesome Question", "Your question has been upvoted 1000 times"),
                new Badge("Good Answer", "Your answer has been upvoted 10 times"),
                new Badge("Great Answer", "Your answer has been upvoted 100 times"),
                new Badge("Awesome Answer", "Your answer has been upvoted 1000 times"),
                new Badge("Good Comment", "Your comment has been upvoted 10 times"),
                new Badge("Great Comment", "Your comment has been upvoted 100 times"),
                new Badge("Awesome Comment", "Your comment has been upvoted 1000 times"),
                new Badge("Good Contribution", "Your post has been upvoted 10 times"),
                new Badge("Great Contribution", "Your post has been upvoted 100 times"),
                new Badge("Awesome Contribution", "Your post has been upvoted 1000 times"));

        log.info("");
        log.info("N########## SEEDING BADGE ##########");
        badgeRepository.saveAll(commonBadges);
        badgeRepository.saveAll(featureBadges);
        log.info("{} BADGES added to the database.", commonBadges.size());
    }


    /**
     * Seed the role table
     */
    private void seedRole() {
        List<Role> roles = List.of(
                new Role(ROLENAME.ROLE_USER),
                new Role(ROLENAME.ROLE_MODERATOR),
                new Role(ROLENAME.ROLE_ADMIN)
        );

        log.info("");
        log.info("########## SEEDING ROLE ##########");
        roleRepository.saveAll(roles);
        log.info("3 ROLEs added to the database.");
    }
}
