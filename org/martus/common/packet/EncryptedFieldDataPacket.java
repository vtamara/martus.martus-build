/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2001-2003, Beneficent
Technology, Inc. (Benetech).

Martus is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later
version with the additions and exceptions described in the
accompanying Martus license file entitled "license.txt".

It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, including warranties of fitness of purpose or
merchantability.  See the accompanying Martus License and
GPL license for more details on the required license terms
for this software.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.

*/

package org.martus.common.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.martus.common.MartusXml;
import org.martus.common.XmlWriterFilter;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.MartusCrypto.EncryptionException;
import org.martus.util.Base64;

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
