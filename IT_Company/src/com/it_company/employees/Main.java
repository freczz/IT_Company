package com.it_company.employees;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.util.ArrayList;

public class Main {

	private static ArrayList<Person> persons = new ArrayList<>();

	public static void main(String[] args) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document source = builder.parse(new File("xml/company.xml"));

		String root = "root";

		NodeList elementsList = source.getElementsByTagName(root);
		if (elementsList.getLength() == 0) {
			System.out.println("No " + root + "in this file");
		} else {
			for (int i = 0; i < elementsList.getLength(); i++) {
				Node foundedElement = elementsList.item(i);
				if (foundedElement.hasChildNodes()) {
					printAllChildNodes(foundedElement.getChildNodes());
				}
			}
		}

		System.out.println();

		NodeList personList = source.getDocumentElement().getElementsByTagName("person");
		createPersonsArrayList(personList);

		Document result = builder.newDocument();
		createTeam(persons, result);
	}

	private static void printAllChildNodes(NodeList list) {

		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);

			if (node.getNodeType() == Node.TEXT_NODE) {
				String textInformation = node.getNodeValue().replace("\n", "").trim();
				if (!textInformation.isEmpty()) {
					System.out.println("\t" + node.getNodeValue());
				}
			} else {
				System.out.print(node.getNodeName() + " ");
				NamedNodeMap attributes = node.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++)
					System.out.print(
							"\t" + attributes.item(j).getNodeName() + ": " + attributes.item(j).getNodeValue() + " ");
				System.out.println();
			}

			if (node.hasChildNodes())
				printAllChildNodes(node.getChildNodes());
		}
	}

	private static void createPersonsArrayList(NodeList list) {

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			NamedNodeMap attr = node.getAttributes();
			
			persons.add(new Person(attr.getNamedItem("name").getNodeValue(),
					attr.getNamedItem("position").getNodeValue(), attr.getNamedItem("project").getNodeValue()));
		}
	}

	private static void createTeam(ArrayList<Person> persons, Document result) throws Exception {

		Element rootElement = result.createElement("team");
		result.appendChild(rootElement);

		for (int i = 0; i < persons.size()-1; i++) {
			if (!persons.get(i).getPosition().equals(persons.get(i+1).getPosition())
					&& persons.get(i).getProject().equals("no")) {
				rootElement
						.appendChild(addPerson(result, persons.get(i).getName(), persons.get(i).getPosition()));
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(result);

		StreamResult console = new StreamResult(System.out);
		StreamResult file = new StreamResult(new File("xml/result.xml"));

		transformer.transform(source, console);
		transformer.transform(source, file);

		System.out.println("XML is ready");
	}

	private static Node addPerson(Document result, String name, String position) {
		Element person = result.createElement("person");
		person.setAttribute("name", name);
		person.setAttribute("position", position);
		return person;
	}
}
