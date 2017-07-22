package ae.nlp.biocreative;


import com.pengyifan.bioc.io.BioCCollectionReader;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.net.URL;

/**
 * Created by  on 22/07/2017.
 */
public class Parser {

    public  BioCCollectionReader process(URL url) throws IOException, SAXException, XMLStreamException, ParserConfigurationException {

        File file = new File(url.getFile());
        removeDtd(file.getAbsolutePath());
        BioCCollectionReader bioc = new BioCCollectionReader(new FileInputStream(file.getAbsolutePath()));
        return  bioc;
    }



    private void removeDtd(String filePath) throws IOException, XMLStreamException {
        XMLEventWriter output = null;
        XMLEventReader input = null;
        StringWriter stringWriter= new StringWriter();

        try {
            XMLInputFactory inFactory = XMLInputFactory.newFactory();
            XMLOutputFactory outFactory = XMLOutputFactory.newFactory();
            inFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            input = inFactory.createXMLEventReader(
                    new FileInputStream(filePath));
            XMLEventReader filtered = inFactory.createFilteredReader(
                    input, new DTDFilter());
             output = outFactory.createXMLEventWriter(stringWriter
                    );

            output.add(filtered);



        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {


            bw.write(stringWriter.toString());

            // no need to close it.
            //bw.close();

            System.out.println("Done");

        }
        } finally {
            if (output != null)
                output.close();

            if (input != null){
                input.close();
            }
        }

    }

    static class DTDFilter implements EventFilter {
        @Override
        public boolean accept(XMLEvent event) {
            return event.getEventType() != XMLStreamConstants.DTD;
        }

    }
}
