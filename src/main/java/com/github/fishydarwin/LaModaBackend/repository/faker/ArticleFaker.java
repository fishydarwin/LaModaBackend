package com.github.fishydarwin.LaModaBackend.repository.faker;

import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.domain.ArticleAttachment;
import com.github.fishydarwin.LaModaBackend.util.imgur.ImgurBrowser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ArticleFaker {

    private static final List<String> possibleWords =
            List.of("Pullover", "Tricou", "Bluza", "Haine", "Pantofi", "Bratara", "Cercel", "Cercei",
                    "Tricouri", "Pantaloni", "Blugi", "Machiaj", "Unghii", "Lac", "Eyeliner", "Culori", "Fashion");
    private static final List<String> possibleConnectors =
            List.of("pentru", "de", "pt");
    private static final List<String> possibleAdjectives =
            List.of("Vara", "Primavara", "Toamna", "Iarna", "Barbati", "Femei", "Petrecere", "Afara",
            "Parc", "Schii", "Sport", "Sala", "Bal", "Vedete", "Relaxare", "Sportivi", "Tineri", "Seniori");

    private static final String lipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Cras rutrum, nisi a rutrum mattis, lacus massa faucibus lacus, vel elementum lorem mauris " +
            "nec erat. Donec efficitur ipsum lectus, vitae rhoncus est eleifend sed. In hac habitasse " +
            "platea dictumst. Vestibulum enim lorem, feugiat eu augue eget, feugiat fermentum arcu. " +
            "Praesent feugiat a tortor congue consequat. Aenean condimentum lacus id lacus hendrerit " +
            "luctus. Quisque eget auctor nibh. Nulla sed est magna.\n" +
            "\n" +
            "Nunc convallis sem nec blandit malesuada. Mauris scelerisque, ex a rutrum lobortis, " +
            "felis erat dapibus tellus, sit amet feugiat nibh quam sed ex. Ut sollicitudin tortor " +
            "et tortor eleifend ultrices. Mauris ac ipsum nisi. Morbi feugiat felis nec nisl placerat " +
            "dignissim. Donec sit amet purus id elit scelerisque hendrerit sit amet et lectus. Duis " +
            "magna nisi, aliquam non dolor non, dignissim condimentum nisl.\n" +
            "\n" +
            "Nullam ac lacus nec lectus ultricies egestas. Sed ac porta purus. Nunc pretium purus massa, " +
            "eget dapibus risus pellentesque vel. Nam elementum dui purus, at venenatis augue suscipit quis. " +
            "Fusce eget dolor lectus. Phasellus vestibulum consequat risus, id fermentum orci sodales in. " +
            "Donec pharetra egestas arcu eu tincidunt. Nulla leo urna, placerat quis sodales nec, ultricies " +
            "id arcu. In at nunc dapibus, posuere ante sit amet, posuere turpis. Nunc non erat bibendum, " +
            "euismod dolor vitae, dictum velit. Pellentesque sagittis nunc in ante porttitor efficitur. Cras " +
            "efficitur, felis pulvinar dignissim aliquet, eros turpis aliquet nisl, sit amet pretium libero " +
            "lorem vitae orci. Ut sed tellus sed lectus accumsan tempor. Phasellus arcu sapien, finibus sit " +
            "amet leo vel, dapibus ornare elit.\n" +
            "\n" +
            "Etiam vel nisl vel augue dictum rhoncus. Nunc pharetra mauris non vehicula tempus. " +
            "Pellentesque in nisl quam. Ut vehicula vestibulum dui, in porttitor dolor consequat sed. " +
            "Nulla a molestie lacus. Integer porta, turpis ut cursus luctus, urna arcu fermentum nibh, " +
            "nec vehicula nibh arcu id quam. Mauris ut felis orci. Ut diam augue, tincidunt nec nisi " +
            "vitae, lacinia dignissim libero. Proin a odio volutpat, posuere lacus id, imperdiet sem. " +
            "Nulla gravida commodo lorem vitae interdum. Proin quis nunc at felis ullamcorper rutrum. " +
            "Mauris et eros a libero sodales fermentum vel quis nisi.\n" +
            "\n" +
            "Sed at tempor risus, sed tempus enim. Suspendisse vel convallis turpis. In at vulputate " +
            "diam. Etiam id lobortis metus. Etiam elementum quam nec mi convallis tempus. Vestibulum " +
            "erat lorem, malesuada eu pulvinar at, auctor et nibh. Sed efficitur pulvinar enim a " +
            "scelerisque. Ut ultricies maximus luctus. Donec viverra dictum nisi ac aliquam. Phasellus " +
            "sit amet sem vel lectus feugiat luctus posuere quis erat. Etiam magna ligula, bibendum eu " +
            "dapibus sit amet, malesuada nec metus. Vestibulum ante ipsum primis in faucibus orci luctus " +
            "et ultrices posuere cubilia curae; Phasellus dolor orci, imperdiet id hendrerit eu, " +
            "facilisis vitae ipsum. Mauris viverra arcu sed augue pharetra tincidunt. Donec id tellus " +
            "mollis elit viverra tincidunt at nec nibh.";
    private static List<String> lipsumList = new ArrayList<>();

    public static void init() throws URISyntaxException, IOException, ParseException, InterruptedException {
        ImgurBrowser.grabImgurImages("fashion&sp;ideas");
        lipsumList = Arrays.stream(lipsum.split("\n"))
                .filter((item) -> item.strip().length() > 0)
                .map((item) -> item.substring(0, 128))
                .collect(Collectors.toList());
    }

    public static String randomFromList(Random random, List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

    private static final Random random = new Random();

    public static Article generateRandomArticle()
            throws URISyntaxException, IOException, ParseException, InterruptedException {
        int randomId = random.nextInt(1, 5);
        int randomCategory = random.nextInt(1, 6);
        String randomTitle = randomFromList(random, possibleWords) + " "
                + randomFromList(random, possibleConnectors) + " "
                + randomFromList(random, possibleAdjectives);

        String randomLipsum = randomFromList(random, lipsumList);

        return new Article(
                -1, randomId, randomCategory, randomTitle, randomLipsum,
                List.of(
                        new ArticleAttachment(-1, -1,
                                "https://i.imgur.com" + ImgurBrowser.getRandomImageURL().getFile()),
                        new ArticleAttachment(-1, -1,
                                "https://i.imgur.com" + ImgurBrowser.getRandomImageURL().getFile()),
                        new ArticleAttachment(-1, -1,
                                "https://i.imgur.com" + ImgurBrowser.getRandomImageURL().getFile())
                )
        );
    }

}
