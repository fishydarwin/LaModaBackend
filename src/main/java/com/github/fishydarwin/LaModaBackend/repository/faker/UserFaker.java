package com.github.fishydarwin.LaModaBackend.repository.faker;

import com.github.fishydarwin.LaModaBackend.domain.User;
import com.github.fishydarwin.LaModaBackend.domain.UserRole;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UserFaker {

    private static final List<String> possibleFirstNames =
            List.of("James", "Robert", "John", "Michael", "David", "William", "Richard", "Joseph",
                    "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Jessica", "Sarah",
                    "Charles", "Daniel", "Matthew", "Anthony", "Mark", "Donald", "Steven", "Andrew",
                    "Ashley", "Kimberly", "Emily", "Donna", "Michelle", "Carol", "Amanda", "Melissa");
    private static final List<String> possibleLastNames =
            List.of("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
                    "Rodriguez", "Martinez", "Hernandez", "Wilson", "Anderson", "Thomas", "Taylor",
                    "Moore", "Jackson", "Martin", "Lee", "Thompson", "White", "Harris", "Robinson",
                    "Walker", "Young", "Allen", "Torres", "Green", "Adams", "Rivera", "Carter");

    private static final String initials = "ABCDEFGHIJKLMNOPRSTWYZ";

    public static String randomFromList(Random random, List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

    private static final Random random = new Random();
    public static User generateRandomUser() {

        String randomFirstName = randomFromList(random, possibleFirstNames);
        String randomLastName = randomFromList(random, possibleLastNames);
        String randomMiddleName = initials.charAt(random.nextInt(initials.length())) + ".";
        String randomName = randomFirstName + " " + randomMiddleName + " " + randomLastName;

        String randomEmailIdentifier = Integer.toString(random.nextInt(0, 1000000));
        String randomEmail = randomFirstName.toLowerCase() + "." + randomLastName + randomEmailIdentifier + "@fake.ro";

        return new User(-1, randomName, UUID.randomUUID().toString(), randomEmail, UserRole.USER);
    }

}
