package com.ted.auctionbay.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.springframework.beans.factory.annotation.Autowired;

import com.ted.auctionbay.dao.QueryItem;
import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;
import com.ted.auctionbay.entities.items.Item;

public class ItemServicesImpl  implements ItemServices{
	
	@Autowired
	QueryItem queryItem;
	
	@Override
	public List<Item> getAllItems() {
		return queryItem.getItems();
	}

	@Override
	public Item getDetails(int ItemID) {
		return queryItem.getDetails(ItemID);
	}

	@Override
	public int getNumberofItems() {
		return queryItem.getNumberofItems();
	}

	@Override
	public List<String> getCategories(int ItemID) {
		return queryItem.getCategories(ItemID);
	}

	@Override
	public List<Double> getCoordinates(int ItemID) {
		return queryItem.getCoordinates(ItemID);
	}

	@Override
	public String getLocation(int ItemID) {
		return queryItem.getLocation(ItemID);
	}
	
	@Override
	public void exportToXML(String ItemID) throws IOException {
		initializeRatingData();
		Document doc = XMLExporter(ItemID);
		String filename = ItemID;
		WriteToXMLFile(doc,filename);	
	}

	private static void WriteToXMLFile(Document doc, String filename) throws IOException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		    DOMSource source = new DOMSource(doc);

		    File myFile = new File("C:\\Users\\takis\\Desktop\\XML\\product"+filename+".xml");
			try {
				myFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    try {
				try {
					transformer.transform(new DOMSource(doc), 
					     new StreamResult(new FileWriter(myFile)));
				} catch (TransformerException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (TransformerConfigurationException e2) {
			e2.printStackTrace();
		}
		
	}

	@Override
	public void exportAllToXML() throws IOException {
		
		List<String> qresults = queryItem.getItemIDs();
		List<Integer> itemIDs = new ArrayList<Integer>();
		for (String result : qresults){
			itemIDs.add(Integer.valueOf(result));
		}
		
		
		int count = 0;
		Document doc;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document fileDoc = null;
		Element rootElement = null;
		int fileNumber = 0;
		
		initializeRatingData();
		
		int i = 0;
		for(i=0;i < itemIDs.size() ;i++){/**/
			
			if( i%500 == 0){
				if(i != 0){
					WriteToXMLFile(fileDoc,Integer.toString(fileNumber));
					System.out.println("writing file "+fileNumber);
					fileNumber++;
				}
					
				try {
					docBuilder = docFactory.newDocumentBuilder();
					fileDoc = docBuilder.newDocument();
					
					rootElement = fileDoc.createElement("Items");
					fileDoc.appendChild(rootElement);
					
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}		
			}
			//System.out.println("store product "+i+ "to file "+i/500);
			doc = XMLExporter(itemIDs.get(i).toString());
		    Element e = doc.getDocumentElement();
		    Node n = (Node) doc.getChildNodes().item(0);
		    Node newNode = (Node) fileDoc.importNode(n, true);
		    rootElement.appendChild(newNode);
		}
		WriteToXMLFile(fileDoc,Integer.toString(fileNumber));
		System.out.println("writing file "+fileNumber);
	}
	
	
	@Override
	public Document XMLExporter(String ItemID){
					
		try {
			Auction a = auctions.get(Integer.parseInt(ItemID));//AuctionQueries.details(ItemID);
			Item p = a.getProduct();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Item");
			doc.appendChild(rootElement);
			rootElement.setAttribute("ItemID",Integer.toString(p.getProductID()));
			
			
			Element name = doc.createElement("Name");
			name.appendChild(doc.createTextNode(p.getName()));
			rootElement.appendChild(name);
			
			List<Category> categories = p.getCategories();
			for(Category c:categories){
				Element cat = doc.createElement("Category");
				cat.appendChild(doc.createTextNode(c.getName()));
				rootElement.appendChild(cat);
			}
			
			Element currently = doc.createElement("Currently");
			
			/**************fix me*******************************/
			currently.appendChild(doc.createTextNode("$" + AuctionQueries.findHighestBid(a.getAuctionID())));
			rootElement.appendChild(currently);
			/**************fix me*******************************/
			
			
			Element fb = doc.createElement("First_Bid");
			fb.appendChild(doc.createTextNode("$" +a.getFirstBid()));
			rootElement.appendChild(fb);
			
			
			/********************** fix me **********************************/
			int numberOfBids = AuctionQueries.numberOfBids(a.getAuctionID());
			/********************** fix me **********************************/
			
			Element nb = doc.createElement("Number_of_Bids");
			nb.appendChild(doc.createTextNode(""+numberOfBids));
			rootElement.appendChild(nb);
			
			Element bids = doc.createElement("Bids");
			rootElement.appendChild(bids);
			
			if( numberOfBids != 0 ){
				List<Object[]> resultSet = AuctionQueries.findBids(a.getAuctionID());
				for(Object[] r:resultSet){
					Element bid = doc.createElement("Bid");
					
					User u = users.get(r[0].toString());//UserQueries.findByUsername(r[0].toString());
					Element bidder = doc.createElement("Bidder");
					
					int rating;
					if(bidderRanking.containsKey(u.getUsername()))
						rating = bidderRanking.get(u.getUsername());	
					else
						rating = 0;
					
					bidder.setAttribute("Rating",Integer.toString(rating) );
					bidder.setAttribute("UserID",u.getUsername());
					
					Element s = doc.createElement("Street");
					s.appendChild(doc.createTextNode(u.getAddress().getStreet()));
					
					Element c = doc.createElement("City");
					c.appendChild(doc.createTextNode(u.getAddress().getCity()));
					
					bidder.appendChild(s);
					bidder.appendChild(c);
					
					Element time = doc.createElement("Time");
					time.appendChild(doc.createTextNode(r[2].toString()));
					
					Element amount = doc.createElement("Amount");
					amount.appendChild(doc.createTextNode(r[1].toString()));
					
					bid.appendChild(bidder);
					bid.appendChild(time);
					bid.appendChild(amount);
					bids.appendChild(bid);
				}
				
			}
			
			
			Element l = doc.createElement("Location");
			l.appendChild(doc.createTextNode("Latitude=" + p.getLatitude() + " " + "Longitude=" + p.getLongitute()));
			rootElement.appendChild(l);
			
			
			Element c = doc.createElement("Country");
			c.appendChild(doc.createTextNode(p.getLocation()));
			rootElement.appendChild(c);
			
			Element st = doc.createElement("Started");
			st.appendChild(doc.createTextNode(a.getStartedTime().toString()));
			rootElement.appendChild(st);
			
			Element ends = doc.createElement("Ends");
			ends.appendChild(doc.createTextNode(a.getExpirationDate().toString()));
			rootElement.appendChild(ends);
			
			Element seller = doc.createElement("Seller");
			
			
			int rating;
			if(sellerRanking.containsKey( a.getRegistereduser().getUsername()) ){
				System.out.println(p.getProductID() + "/" +a.getRegistereduser().getUsername() );
				rating = sellerRanking.get( a.getRegistereduser().getUsername());
			}else
				rating = 0;
			
			seller.setAttribute("Rating",Integer.toString(rating) );
			seller.setAttribute("UserID",a.getRegistereduser().getUsername());
			
			
			rootElement.appendChild(seller);
			
			Element d = doc.createElement("Description");
			d.appendChild(doc.createTextNode(p.getDescription()));
			rootElement.appendChild(d);
			
			return doc;
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		return null;
		
	}

	@Override
	public void initializeRatingData() throws IOException {
		// TODO Auto-generated method stub
		
	}
}
