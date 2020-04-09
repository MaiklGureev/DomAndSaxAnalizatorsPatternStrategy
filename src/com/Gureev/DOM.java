package com.Gureev;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.util.Scanner;

public class DOM implements Analizator {

    @Override
    public void checkAverageMark() {
        try {
            System.out.println("");
            Scanner scanner =  new Scanner(System.in);
            String filename = scanner.nextLine();

            //load
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = null;
            builder = f.newDocumentBuilder();
            Document doc = builder.parse(new File("src/com/Gureev/"+filename+".dtd"));
            Node root = doc.getDocumentElement();

            NamedNodeMap rootAttributes = root.getAttributes();
            Node student = rootAttributes.item(0).getFirstChild();
            String name = student.getNodeValue();

            NodeList subjects = doc.getDocumentElement().getElementsByTagName("subject");

            System.out.println("Student last name: "+name);
            System.out.print("Marks: ");
            int[] marks = new int[subjects.getLength()];
            for (int i = 0; i < subjects.getLength(); i++) {
                Node node = subjects.item(i);
                NamedNodeMap subjectAttributes = node.getAttributes();
                marks[i] = Integer.parseInt(node.getAttributes().getNamedItem("mark").getNodeValue());
                System.out.print(marks[i]);
            }

            Node averageMark = doc.getDocumentElement().getElementsByTagName("average").item(0);
            String average = averageMark.getFirstChild().getNodeValue();
            System.out.println("\nAverage mark in doc: "+average);
            double calcMark = calculate(marks);
            System.out.println("\nCalculate average mark: "+calcMark);
            averageMark.getFirstChild().setNodeValue(String.valueOf(calcMark));

            //save
            doc.normalizeDocument();
            DOMSource source = new DOMSource(doc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            File myFile = new File("src/com/Gureev/NEW_DOM_"+filename+".dtd");
            FileWriter writer = new FileWriter(myFile);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            scanner.close();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
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
}
