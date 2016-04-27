package org.aqua;

import org.aqua.file.FileUtil;
import org.aqua.framework.core.AquaCore;
import org.aqua.parser.xml.XMLParser;

public class DemoApp {

    public static void main(String[] args) {
        AquaCore core = new AquaCore(XMLParser.parseMap(FileUtil.readFile("sample.xml")));
    }

}
