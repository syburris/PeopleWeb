package com.theironyard;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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

        Spark.get(
                "/",
                (request, response) -> {
                    int offset = 0;
                    String offsetString = request.queryParams("offset");
                    if (offsetString != null) {
                        offset = Integer.valueOf(offsetString);
                    }

                    ArrayList sublist = new ArrayList(people.subList(offset,OFFSET));

                    HashMap m = new HashMap();

                    m.put("previous", offset-OFFSET);
                    m.put("next", offset+OFFSET);
                    m.put("showNext", offset+OFFSET<people.size());
                    m.put("showPrevious", offset>0);
                    m.put("people",sublist);

                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );

        Spark.get(
                "/person",
                (request, response) -> {
                    int id = Integer.valueOf(request.queryParams("id"));
                    Person person = people.get(id-1);
                    return new ModelAndView(person,"person.html");
                }
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
