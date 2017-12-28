package pl.karol202.bolekserver.server.outputpacket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.ServerException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;

public class OutputPacketEncoder
{
	public static byte[] encodePacket(OutputPacket packet)
	{
		try
		{
			return encode(packet);
		}
		catch(ParserConfigurationException | TransformerException e)
		{
			new ServerException("Cannot save packet.", e).printStackTrace();
			return null;
		}
	}
	
	private static byte[] encode(OutputPacket packet) throws ParserConfigurationException, TransformerException
	{
		Document document = createDocumentFromPacket(packet);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(outputStream);
		transformer.transform(domSource, streamResult);
		
		return outputStream.toByteArray();
	}
	
	private static Document createDocumentFromPacket(OutputPacket packet) throws ParserConfigurationException
	{
		Document document = getNewDocument();
		DataBundle bundle = new DataBundle();
		packet.saveData(bundle);
		Element packetElement = createPacketElement(document, packet, bundle);
		document.appendChild(packetElement);
		return document;
	}
	
	private static Document getNewDocument() throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.newDocument();
	}
	
	private static Element createPacketElement(Document document, OutputPacket packet, DataBundle bundle)
	{
		Element element = document.createElement(packet.getName());
		bundle.getStringEntriesStream().forEach(e -> element.setAttribute("s." + e.getKey(), e.getValue()));
		bundle.getIntEntriesStream().forEach(e -> element.setAttribute("i." + e.getKey(), String.valueOf(e.getValue())));
		bundle.getBooleanEntriesStream().forEach(e -> element.setAttribute("b." + e.getKey(), String.valueOf(e.getValue())));
		return element;
	}
}