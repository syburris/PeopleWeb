package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static final String PEOPLE = "People.txt";

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Person> people = readFile();

        Spark.get(
                "/",
                (request, response) -> {
                    HashMap m = new HashMap();
                    if (people.size() > 20) {
                        ArrayList<Person> list = (ArrayList<Person>) people.subList(0,19);
                    }
                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()

        );

    }

    // read the people.txt file and separate it into Person objects, then add person objects to the people ArrayList

    public static ArrayList<Person> readFile() throws FileNotFoundException {
        ArrayList<Person> people = new ArrayList<>();
        File file = new File(PEOPLE);
        Scanner fileScanner = null;
        fileScanner = new Scanner(file);
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split("\\,");
            Person person = new Person(Integer.valueOf(columns[0]),columns[1],columns[2],columns[3],columns[4],columns[5]);
            people.add(person);
        }
        return people;
    }
}
