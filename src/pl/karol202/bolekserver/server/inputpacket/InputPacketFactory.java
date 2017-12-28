package pl.karol202.bolekserver.server.inputpacket;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.ServerException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputPacketFactory
{
	public static InputPacket createPacket(byte[] bytes)
	{
		try
		{
			return getPacket(bytes);
		}
		catch(Exception e)
		{
			new ServerException("Cannot read packet.", e).printStackTrace();
			return null;
		}
	}
	
	private static InputPacket getPacket(byte[] bytes) throws IOException, SAXException, ParserConfigurationException
	{
		InputStream inputStream = new ByteArrayInputStream(bytes);
		Document document = getDocument(inputStream);
		if(document == null) return null;
		return createPacketFromXML(document);
	}
	
	private static Document getDocument(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(inputStream);
	}
	
	private static InputPacket createPacketFromXML(Document document)
	{
		Element root = document.getDocumentElement();
		InputPacketType packetType = InputPacketType.getPacketTypeByName(root.getTagName());
		if(packetType == null) return null;
		InputPacket packet = packetType.createPacket();
		DataBundle bundle = createDataBundle(root);
		if(bundle == null) return null;
		packet.readData(bundle);
		return packet;
	}
	
	private static DataBundle createDataBundle(Element packetElement)
	{
		DataBundle bundle = new DataBundle();
		NamedNodeMap map = packetElement.getAttributes();
		for(int i = 0; i < map.getLength(); i++)
		{
			Attr attr = (Attr) map.item(i);
			String fullName = attr.getName();
			String[] parts = fullName.split("\\.");
			if(parts.length != 2) return null;
			char typeChar = parts[0].charAt(0);
			String name = parts[1];
			switch(typeChar)
			{
			case 's': bundle.putString(name, attr.getValue()); break;
			case 'i': bundle.putInt(name, Integer.parseInt(attr.getValue())); break;
			case 'b': bundle.putBoolean(name, Boolean.parseBoolean(attr.getValue())); break;
			default: return null;
			}
		}
		return bundle;
	}
}