package org.aqua.parser.xml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.aqua.Caster;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler {
    public static Map<String, Object> parseMap(String stringSource) {
        Map<String, Object> xmlMap = new HashMap<String, Object>();
        try {
            Document doc = new SAXReader().read(new InputSource(new StringReader(stringSource)));
            xmlMap = Caster.cast(parseElement(doc.getRootElement()));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return xmlMap;
    }

    private static Object parseElement(Element element) {
        if (0 == element.attributeCount() && element.isTextOnly()) {
            return element.getTextTrim();
        }
        List<?> elements = element.elements();
        if (!elements.isEmpty()) {
            if (element.getName().equals(((Element) elements.get(0)).getName() + "s")) {
                List<Object> list = new ArrayList<Object>(elements.size());
                for (Object o : elements) {
                    list.add(parseElement((Element) o));
                }
                return list;
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (Object o : element.attributes()) {
            Attribute a = (Attribute) o;
            map.put(a.getQualifiedName(), a.getValue());
        }
        for (Object o : elements) {
            Element e = (Element) o;
            map.put(e.getName(), parseElement(e));
        }
        String text = element.getTextTrim();
        if (!text.isEmpty()) {
            map.put(element.getName(), text);
        }
        return map;
    }

    public void backup() {
        String stringSource = "";
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new InputSource(new StringReader(stringSource)), new XMLParser());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private XMLParser() {
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        System.out.println("uri:" + uri);
        System.out.println("prefix mapp:" + prefix);
        super.startPrefixMapping(prefix, uri);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        System.out.println("uri:" + uri);
        System.out.println("startElement:" + localName + "/" + qName);
        System.out.println("attri:" + attributes);
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("uri:" + uri);
        System.out.println("end:" + localName + "/" + qName);
        super.endElement(uri, localName, qName);
    }
}
