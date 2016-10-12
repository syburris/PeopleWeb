package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    static final String PEOPLE = "People.txt";
    static final int OFFSET = 20;

    public static void main(String[] args) throws FileNotFoundException {
        HashMap<String, ArrayList<Person>> peopleMap = new HashMap<>();
        ArrayList<Person> people;
        people = readFile();
        addTooPeopleMap(people,peopleMap);
        sortPeople(peopleMap);
        System.out.println(peopleMap);


        Spark.get(
                "/",
                (request, response) -> {
                    int offset = 0;
                    String offsetString = request.queryParams("offset");
                    if (offsetString !=) {
                        offset = Integer.valueOf(offsetString);
                    }
                    ArrayList subList = new ArrayList(people.subList(offset, offset + 20));
                    HashMap m = new HashMap();
                    m.put("people",subList);
                    m.put("offsetPrevious", subList - OFFSET);
                    m.put("offsetNext", subList+20);
                    System.out.println(people);
//                    int i = 0;
//
//                    while (people.size() < 1000) {
//                        m.put("person",people.get(i).firstName);
//                        i++;
//                    }

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

    //create HashMap

    public static void addTooPeopleMap(ArrayList<Person> people, HashMap<String, ArrayList<Person>> peopleMap) {
        ArrayList<Person> lastNames = null;
        for (Person person : people) {
            String lastName = person.getLastName();
            lastNames = peopleMap.get(lastName);
            if(lastNames == null) {
                lastNames = new ArrayList<>();
            }
            lastNames.add(person);
            peopleMap.put(lastName,lastNames);
        }
    }

    //Sort hashmap by last name

    public static void sortPeople(HashMap<String, ArrayList<Person>> peopleMap) {
        for (String lastName : peopleMap.keySet()) {
            ArrayList<Person> lastNameList = peopleMap.get(lastName);
            Collections.sort(lastNameList);
        }
    }

}
