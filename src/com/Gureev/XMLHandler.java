package com.Gureev;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class XMLHandler extends DefaultHandler {

    Student student = new Student();
    String  lastElementName = "";
    double calcMark = 0;
    char[] dtdFile;
    int averageMarkStart,averageMarkLength;
    int [] marks;


    @Override
    public void startDocument() throws SAXException {
        // Тут будет логика реакции на начало документа
        System.out.println("Start parse XML...");

    }

    @Override
    public void endDocument() throws SAXException {
        // Тут будет логика реакции на конец документа
        System.out.println("Student last name: "+student.getLastName());
        fillMarksArray();
        System.out.print("Marks: ");
        for (int a = 0; a< marks.length;a++) {
            System.out.print(marks[a]);
        }
        System.out.println("\nAverage mark in doc: "+student.getAverageMark());
        System.out.println("Calculate average  mark: "+calcMark);

        if (lastElementName.equals("average")) {
            char[] newCh;
            char[] averageCh = String.valueOf(calcMark).toCharArray(); //оценка
            char[] averageWord = new String("</average>").toCharArray(); //
            newCh = Arrays.copyOf(dtdFile, averageMarkStart+averageCh.length+averageWord.length); //скопировали начало с увеличенной длинной
            System.arraycopy(averageCh,0,newCh,averageMarkStart,averageCh.length); //скопировали оценку
            System.arraycopy(averageWord,0,newCh,averageMarkStart+averageCh.length,averageWord.length); //скопировали финальное слово
            System.out.println(newCh);
            writeToFile(newCh);
        }
        System.out.println("Stop parse XML...");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Тут будет логика реакции на начало элемента
        if (qName.equals("student")) {
            String lastName = attributes.getValue("lastname");
            student.setLastName(lastName);
        }
        if(qName.equals("subject")){
            String title = attributes.getValue("title");
            int mark = Integer.parseInt(attributes.getValue("mark"));
            student.addSubject(title,mark);
        }
        if(qName.equals("average")){
            lastElementName = "average";
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Тут будет логика реакции на конец элемента

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // Тут будет логика реакции на текст между элементами
        String information = new String(ch, start, length);
        information = information.replace("\n", "").trim();

        if (!information.isEmpty()) {
            if (lastElementName.equals("average")) {
                int mark = Integer.parseInt(information);
                student.setAverageMark(mark);
                dtdFile = ch;
                averageMarkLength = length;
                averageMarkStart =start;
            }
        }
    }


    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        // Тут будет логика реакции на пустое пространство внутри элементов (пробелы, переносы строчек и так далее).
        if (lastElementName.equals("average")) {
            dtdFile = ch;
            averageMarkLength = length;
            averageMarkStart =start;
        }
    }

    double calculate(int[] marks){
        double average = 0;
        int count = marks.length;
        for(int a = 0; a<count;a++){
            average +=marks[a];
        }
        return Math.round(average/count*100)/100.0;
    }

    public void fillMarksArray(){
        marks = new int[student.getSubjectsCount()];
        for (int a = 0; a< student.getSubjectsCount();a++) {
            marks[a] = student.getSubject(a).mark;
        }
        calcMark = calculate(marks);
    }
    public void writeToFile(char[] text){

        File myFile = new File("src/com/Gureev/NEW_SAX_student.dtd");
        try(FileWriter writer = new FileWriter(myFile, false))
        {
//           for(int a= 0; a<text2.length;a++){
//               writer.append(text2[a]);
//           }
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private class Student{
        String lastName;
        double average;
        ArrayList<Subject> subjects = new ArrayList<>();

        public void addSubject(String title,  int mark){
            Subject subject =  new Subject(title,mark);
            subjects.add(subject);
        }

        public Subject getSubject(int i) {
            return subjects.get(i);
        }

        public int getSubjectsCount(){
            return subjects.size();
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public double getAverageMark() {
            return average;
        }

        public void setAverageMark(double average) {
            this.average = average;
        }

        private class Subject{
            String title;
            int mark;

            public Subject(String title, int mark) {
                this.title = title;
                this.mark = mark;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getMark() {
                return mark;
            }

            public void setMark(int mark) {
                this.mark = mark;
            }
        }
    }
}