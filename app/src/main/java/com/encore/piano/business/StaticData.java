package com.encore.piano.business;

import com.encore.piano.model.*;

import java.util.ArrayList;

public class StaticData {
	
	public static ArrayList<ConfirmationModel> GetConfirmations()
	{
		ArrayList<ConfirmationModel> Confirmations = new ArrayList<ConfirmationModel>();
		
		ConfirmationModel m1 = new ConfirmationModel();
		ConfirmationModel m2 = new ConfirmationModel();
		ConfirmationModel m3 = new ConfirmationModel();
		
		m1.setCondition("I have not been drinking.");
		m1.setConfirmed(false);
		m1.setId(1);
		
		m2.setCondition("I didn't take drugs.");
		m2.setConfirmed(false);
		m2.setId(2);
		
		m3.setCondition("I got insurance");
		m3.setConfirmed(false);
		m3.setId(3);
		
		Confirmations.add(m1);
		Confirmations.add(m2);
		Confirmations.add(m3);
		
		return Confirmations;
	}
	
	
	public static ArrayList<ConsignmentModel> GetConsignments()
	{
		ArrayList<ConsignmentModel> models = new ArrayList<ConsignmentModel>();
		
		ConsignmentModel m1 = new ConsignmentModel();
		ConsignmentModel m2 = new ConsignmentModel();
		ConsignmentModel m3 = new ConsignmentModel();
		
//		m1.setId("1100");
//		m1.setConsignmentReference("CON NOTE");
//		m1.setColCode("Mc Neills Warehouse A");
//		m1.setDeliveryCode("AC Chemical Company");
//		m1.setNumberOfItems(3);
//		m1.setTripStatus("S");
//		m1.setSpecialInstructions("Please check the packaging");
////		m1.setAddress("Camden street 1");
//		m1.setAuthToken();
//		m1.setSigned(0);
//
//		m2.setId("1101");
//		m2.setConsignmentReference("CON NOTE3");
//		m2.setColCode("Mc Neills Warehouse C");
//		m2.setDeliveryCode("AC Chemical Company");
//		m2.setNumberOfItems(3);
//		m2.setTripStatus("S");
//		m2.setSpecialInstructions("Please check the packaging");
////		m2.setAddress("Camden street 2");
//		m2.setSigned(0);
//
//		m3.setId("1102");
//		m3.setConsignmentReference("CON NOTE2");
//		m3.setColCode("Mc Neills Warehouse B");
//		m3.setDeliveryCode("AC Chemical Company");
//		m3.setNumberOfItems(3);
//		m3.setTripStatus("S");
//		m3.setSpecialInstructions("Please check the packaging");
////		m3.setAddress("Camden street 2");
//		m3.setAuthToken();
//		m3.setSigned(0);
		
		models.add(m1);
		models.add(m2);
		models.add(m3);
		
		return models;
	}
	
	public static ArrayList<PianoModel> GetItems()
	{
		
		ArrayList<PianoModel> models = new ArrayList<PianoModel>();
		
//		PianoModel im1 = new PianoModel();
//		PianoModel im2 = new PianoModel();
//		PianoModel im3 = new PianoModel();
//		PianoModel im4 = new PianoModel();
//		PianoModel im5 = new PianoModel();
//		PianoModel im6 = new PianoModel();
//		
//		im1.setCode("X00001");
//		im1.setBarcode("123456789984");
//		im1.setConsignmentId("1100");
//		im1.setId(1);		
//		im1.setName("ITEM NAMED 1");
//		im1.setPallet(3.5);
//		im1.setQuantity(14);
//		
//		im2.setCode("X00002");
//		im2.setBarcode("123456789984");
//		im2.setConsignmentId("1100");
//		im2.setId(2);
//		im2.setName("ITEM NAMED 2");
//		im2.setPallet(3.5);
//		im2.setQuantity(14);
//		
//		im3.setCode("X00003");
//		im3.setBarcode("123456789984");
//		im3.setConsignmentId("1101");
//		im3.setId(3);
//		im3.setName("ITEM NAMED 3");
//		im3.setPallet(3.5);
//		im3.setQuantity(14);
//		
//		im4.setCode("X00004");
//		im4.setBarcode("123456789984");
//		im4.setConsignmentId("1101");
//		im4.setId(4);
//		im4.setName("ITEM NAMED 4");
//		im4.setPallet(3.5);
//		im4.setQuantity(14);
//		
//		im5.setCode("X00005");
//		im5.setBarcode("123456789984");
//		im5.setConsignmentId("1102");
//		im5.setId(5);
//		im5.setName("ITEM NAMED 5");
//		im5.setPallet(3.5);
//		im5.setQuantity(14);
//	
//		im6.setCode("X00006");
//		im6.setBarcode("123456789984");
//		im6.setConsignmentId("1102");
//		im6.setId(6);
//		im6.setName("ITEM NAMED 6");
//		im6.setPallet(3.5);
//		im6.setQuantity(14);	
//		
//		models.add(im1);
//		models.add(im2);
//		models.add(im3);
//		models.add(im4);
//		models.add(im5);
//		models.add(im6);
		
		return models;
	}
	
	
	public static ArrayList<TripodModel> GetTripStatuses()
	{
		ArrayList<TripodModel> statuses = new ArrayList<TripodModel>();
		
		TripodModel m1 = new TripodModel();		
		m1.setCode("D");
		m1.setDescription("Delivered");
		
		TripodModel m2 = new TripodModel();		
		m2.setCode("S");
		m2.setDescription("Started");
		
		statuses.add(m1);
		statuses.add(m2);
		
		return statuses;
	}
	
	public static ArrayList<TripodModel> GetPodStatuses()
	{
		ArrayList<TripodModel> statuses = new ArrayList<TripodModel>();
		
		TripodModel m1 = new TripodModel();		
		m1.setCode("N");
		m1.setDescription("No");
		
		TripodModel m2 = new TripodModel();		
		m2.setCode("Y");
		m2.setDescription("Yes");
		
		TripodModel m3 = new TripodModel();		
		m3.setCode("D");
		m3.setDescription("Damaged");
		
		TripodModel m4 = new TripodModel();		
		m4.setCode("C");
		m4.setDescription("Closed");
		
		TripodModel m5 = new TripodModel();		
		m5.setCode("R");
		m5.setDescription("Received");
		
		statuses.add(m1);
		statuses.add(m2);
		statuses.add(m3);
		statuses.add(m4);
		statuses.add(m5);
		
		return statuses;
	}
	
	
	public static ArrayList<MessageModel> GetMessages()
	{
		
		ArrayList<MessageModel> messages = new ArrayList<MessageModel>();
//		
//		MessageModel m1 = new MessageModel();
//		m1.setId(0);
//		m1.setMessageReference("ref1");
//		m1.setRead(0);
//		m1.setSent(0);
//		m1.setReplyId(111);
//		m1.setSender("Marko Markovic");
//		m1.setSenderId(222);
//		m1.setText("This is first test message");
//		m1.setTime(new Date().toString());
//		m1.setTitle("First message");
//		
//		MessageModel m2 = new MessageModel();
//		m2.setId(1);
//		m2.setMessageReference("ref2");
//		m2.setRead(0);
//		m2.setSent(0);
//		m2.setReplyId(111);
//		m2.setSender("Jovan Jovanovic");
//		m2.setSenderId(223);
//		m2.setText("This is second test message");
//		m2.setTime(new Date().toString());
//		m2.setTitle("Second message");
//		
//		MessageModel m3 = new MessageModel();
//		m3.setId(2);
//		m3.setMessageReference("ref3");
//		m3.setRead(0);
//		m3.setSent(0);
//		m3.setReplyId(111);
//		m3.setSender("Marko Markovic");
//		m3.setSenderId(224);
//		m3.setText("This is third test message");
//		m3.setTime(new Date().toString());
//		m3.setTitle("Third message");
		
//		messages.add(m1);
//		messages.add(m2);
//		messages.add(m3);
//		
		return messages;
	}
	
	public static ArrayList<RecipientModel> GetRecipients()
	{
		ArrayList<RecipientModel> recipients = new ArrayList<RecipientModel>();
		
		RecipientModel model = new RecipientModel();		
		model.setName("Marko Markovic");
		
		RecipientModel model1 = new RecipientModel();		
		model1.setName("Jovan Jovanovic");
		
		recipients.add(model);
		recipients.add(model1);
		
		return recipients;
	}

}
