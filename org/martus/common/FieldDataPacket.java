package org.martus.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.martus.common.MartusCrypto.EncryptionException;


public class FieldDataPacket extends Packet
{
	public FieldDataPacket(UniversalId universalIdToUse, String[] fieldTagsToUse)
	{
		super(universalIdToUse);
		fieldTags = fieldTagsToUse;
		for(int f = 0; f < fieldTags.length; ++f)
			fieldTags[f] = fieldTags[f].toLowerCase();
		clearAll();
	}
	
	public static UniversalId createUniversalId(String accountId)
	{
		return UniversalId.createFromAccountAndPrefix(accountId, prefix);
	}
	
	public static boolean isValidLocalId(String localId)
	{
		return localId.startsWith(prefix);
	}
	
	public boolean isEncrypted()
	{
		return encryptedFlag;
	}
	
	public void setEncrypted(boolean newValue)
	{
		encryptedFlag = newValue;
	}
	
	public void setHQPublicKey(String hqKey)
	{
		hqPublicKey = hqKey;	
	}

	public String getHQPublicKey()
	{
		return hqPublicKey;	
	}

	public boolean isPublicData()
	{
		return !isEncrypted();
	}	
	
	public boolean isEmpty()
	{
		if(fieldData.size() > 0)
			return false;
		
		if(attachments.size() > 0)
			return false;
			
		return true;
	}
	
	public int getFieldCount()
	{
		return fieldTags.length;
	}
	
	public String[] getFieldTags()
	{
		return fieldTags;
	}
	
	public boolean fieldExists(String fieldTag)
	{
		String lookFor = fieldTag.toLowerCase();
		for(int f = 0; f < fieldTags.length; ++f)
		{
			if(fieldTags[f].equals(lookFor))
				return true;
		}
		return false;
	}
	
	public String get(String fieldTag)
	{
		Object value = fieldData.get(fieldTag.toLowerCase());
		if(value == null)
			return "";

		return (String)value;
	}
	
	public void set(String fieldTag, String data)
	{
		if(!fieldExists(fieldTag))
			return;

		fieldData.put(fieldTag.toLowerCase(), data);
	}
	
	public void clearAll()
	{
		fieldData = new TreeMap();
		clearAttachments();
		hqPublicKey="";
	}
	
	public void clearAttachments()
	{
		attachments = new Vector();
	}
	
	public AttachmentProxy[] getAttachments()
	{
		AttachmentProxy[] list = new AttachmentProxy[attachments.size()];
		for(int i = 0; i < list.length; ++i)
			list[i] = (AttachmentProxy)attachments.get(i);
			
		return list;
	}
	
	public void addAttachment(AttachmentProxy a)
	{
		attachments.add(a);
	}
	
	
	public byte[] writeXml(Writer writer, MartusCrypto signer) throws IOException
	{
		byte[] result = null;
		if(isEncrypted() && !isEmpty())
			result = writeXmlEncrypted(writer, signer);
		else
			result = writeXmlPlainText(writer, signer);
		return result;
	}
	
	public void loadFromXml(InputStreamWithSeek inputStream, byte[] expectedSig, MartusCrypto verifier) throws 
		IOException,
		InvalidPacketException,
		WrongPacketTypeException,
		SignatureVerificationException,
		MartusCrypto.DecryptionException,
		MartusCrypto.NoKeyPairException
	{
		super.loadFromXmlInternal(inputStream, expectedSig, verifier);
		if(encryptedDataDuringLoad != null)
		{
			if(verifier == null)
				throw new MartusCrypto.DecryptionException();
				
			String encryptedData = encryptedDataDuringLoad;
			encryptedDataDuringLoad = null;
			try
			{
				byte[] encryptedBytes = Base64.decode(encryptedData);
				ByteArrayInputStreamWithSeek inEncrypted = new ByteArrayInputStreamWithSeek(encryptedBytes);
				ByteArrayOutputStream outPlain = new ByteArrayOutputStream();
				if(getAccountId().equals(verifier.getPublicKeyString()))
					verifier.decrypt(inEncrypted, outPlain);
				else if(encryptedHQSessionKeyDuringLoad != null)
				{
					byte[] encryptedHQSessionKey = Base64.decode(encryptedHQSessionKeyDuringLoad);
					byte[] hqSessionKey = verifier.decryptSessionKey(encryptedHQSessionKey);
					verifier.decrypt(inEncrypted, outPlain, hqSessionKey);
				}	
				else
				{
					throw new MartusCrypto.DecryptionException();
				}
				
				byte[] plainXmlBytes = outPlain.toByteArray();
				ByteArrayInputStreamWithSeek inPlainXml = new ByteArrayInputStreamWithSeek(plainXmlBytes);
				UniversalId outerId = getUniversalId();
				loadFromXml(inPlainXml, verifier);
				if(outerId != getUniversalId())
				{
					// TODO: make sure this has a test!
					throw new InvalidPacketException("Inner and outer ids are different");
				}
			}
			catch(Base64.InvalidBase64Exception e)
			{
				throw new InvalidPacketException("Base64Exception");
			}
		}	
	}
	
	public void loadFromXml(InputStreamWithSeek inputStream, MartusCrypto verifier) throws 
		IOException,
		InvalidPacketException,
		WrongPacketTypeException,
		SignatureVerificationException,
		MartusCrypto.DecryptionException,
		MartusCrypto.NoKeyPairException
	{
		loadFromXml(inputStream, null, verifier);
	}
	
	protected byte[] writeXmlPlainText(Writer writer, MartusCrypto signer) throws IOException
	{
		return super.writeXml(writer, signer);
	}
	
	protected byte[] writeXmlEncrypted(Writer writer, MartusCrypto signer) throws IOException
	{
		StringWriter plainTextWriter = new StringWriter();
		writeXmlPlainText(plainTextWriter, signer);
		String payload = plainTextWriter.toString();
		
		EncryptedFieldDataPacket efdp = new EncryptedFieldDataPacket(getUniversalId(), payload, signer);
		efdp.setHQPublicKey(getHQPublicKey());
		return efdp.writeXml(writer, signer);
	}
	
	protected String getPacketRootElementName()
	{
		return MartusXml.FieldDataPacketElementName;
	}
	
	protected void internalWriteXml(XmlWriterFilter dest) throws IOException
	{
		super.internalWriteXml(dest);
		if(isEncrypted() && !isEmpty())
			writeElement(dest, MartusXml.EncryptedFlagElementName, "");
			
		Iterator iterator = fieldData.keySet().iterator();
		while(iterator.hasNext())
		{
			String key = (String)(iterator.next());
			writeElement(dest, MartusXml.FieldElementPrefix + key, (String)(fieldData.get(key)));
		}
		
		for(int i = 0 ; i <attachments.size(); ++i)
		{
			AttachmentProxy a = (AttachmentProxy)attachments.get(i);
			dest.writeStartTag(MartusXml.AttachmentElementName);
			writeElement(dest, MartusXml.AttachmentLocalIdElementName, a.getUniversalId().getLocalId());
			writeElement(dest, MartusXml.AttachmentKeyElementName, Base64.encode(a.getSessionKeyBytes()));
			writeElement(dest, MartusXml.AttachmentLabelElementName, a.getLabel());
			dest.writeEndTag(MartusXml.AttachmentElementName);		
		}
	}
	
	protected void setFromXml(String elementName, String data) throws
			Base64.InvalidBase64Exception
	{
		if(elementName.equals(MartusXml.EncryptedFlagElementName))
		{
			this.setEncrypted(true);
		}
		else if(elementName.startsWith(MartusXml.FieldElementPrefix))
		{
			int prefixLength = MartusXml.FieldElementPrefix.length();
			String tag = elementName.substring(prefixLength);
			set(tag, data);
		}
		else if(elementName.equals(MartusXml.AttachmentLocalIdElementName))
		{
			pendingAttachmentLocalId = data;
		}
		else if(elementName.equals(MartusXml.AttachmentKeyElementName))
		{
			pendingAttachmentKeyBytes = Base64.decode(data);
		}
		else if(elementName.equals(MartusXml.AttachmentLabelElementName))
		{
			UniversalId uid = UniversalId.createFromAccountAndLocalId(getAccountId(), pendingAttachmentLocalId);
			addAttachment(new AttachmentProxy(uid, data, pendingAttachmentKeyBytes));	
		}
		else if(elementName.equals(MartusXml.HQSessionKeyElementName))
		{
			encryptedHQSessionKeyDuringLoad = data;
		}
		else if(elementName.equals(MartusXml.EncryptedDataElementName))
		{
			encryptedDataDuringLoad = data;
		}
		else
		{
			super.setFromXml(elementName, data);
		}
	}
	
	final String packetHeaderTag = "packet";

	private boolean encryptedFlag;
	private String[] fieldTags;
	private Map fieldData;
	private Vector attachments;
	
	private String encryptedDataDuringLoad;	
	private String encryptedHQSessionKeyDuringLoad;
	private String pendingAttachmentLocalId;
	private byte[] pendingAttachmentKeyBytes;
	private static final String prefix = "F-";
	private String hqPublicKey;
}

class EncryptedFieldDataPacket extends Packet
{
	EncryptedFieldDataPacket(UniversalId uid, String plainTextData, MartusCrypto crypto) throws IOException
	{
		super(uid);
		security = crypto;
		try
		{
			sessionKeyBytes = security.createSessionKey();
			byte[] plainTextBytes = plainTextData.getBytes("UTF-8");
			ByteArrayInputStream inPlain = new ByteArrayInputStream(plainTextBytes);
			ByteArrayOutputStream outEncrypted = new ByteArrayOutputStream();
			security.encrypt(inPlain, outEncrypted, sessionKeyBytes);
			byte[] encryptedBytes = outEncrypted.toByteArray();
			encryptedData = Base64.encode(encryptedBytes);
		}
		catch(UnsupportedEncodingException e)
		{
			throw new IOException("UnsupportedEncodingException");
		}
		catch(MartusCrypto.NoKeyPairException e)
		{
			throw new IOException("NoKeyPairException");
		}
		catch(MartusCrypto.EncryptionException e)
		{
			throw new IOException("EncryptionException");
		}
	}

	protected String getPacketRootElementName()
	{
		return MartusXml.FieldDataPacketElementName;
	}
	
	void setHQPublicKey(String hqKey)
	{
		hqPublicKey = hqKey;
	}
	
	protected void internalWriteXml(XmlWriterFilter dest) throws IOException
	{
		super.internalWriteXml(dest);
		writeElement(dest, MartusXml.EncryptedFlagElementName, "");
		if(hqPublicKey.length() > 0)
		{
			try 
			{
				byte[] encryptedSessionKey = security.encryptSessionKey(sessionKeyBytes, hqPublicKey);
				writeElement(dest, MartusXml.HQSessionKeyElementName, Base64.encode(encryptedSessionKey));
			} 
			catch(EncryptionException e) 
			{
				throw new IOException("FieldDataPacket.internalWriteXml Encryption Exception");
			}
		}
		writeElement(dest, MartusXml.EncryptedDataElementName, encryptedData);
	}
	
	MartusCrypto security;
	String encryptedData;
	private String hqPublicKey = "";
	private byte[] sessionKeyBytes;
}
