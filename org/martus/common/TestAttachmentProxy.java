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

package org.martus.common;

import java.io.File;
import java.util.Arrays;

public class TestAttachmentProxy extends TestCaseEnhanced
{
	public TestAttachmentProxy(String name)
	{
		super(name);
	}

	public void testFileProxy() throws Exception
	{
		File file = File.createTempFile("$$$TestAttachmentProxy", null);
		file.deleteOnExit();
		UnicodeWriter writer = new UnicodeWriter(file);
		writer.writeln("This is some text");
		writer.close();

		MartusCrypto security = new MockMartusSecurity();
		byte[] sessionKeyBytes = security.createSessionKey();

		AttachmentProxy a = new AttachmentProxy(file);
		assertEquals(file.getName(), a.getLabel());
		assertEquals("file", file, a.getFile());
		assertNull("not null key?", a.getSessionKeyBytes());

		UniversalId uid = UniversalId.createDummyUniversalId();
		assertNull("already has a uid?", a.getUniversalId());
		a.setUniversalIdAndSessionKey(uid, sessionKeyBytes);
		assertEquals("wrong uid?", uid, a.getUniversalId());
		assertEquals("wrong key?", true, Arrays.equals(sessionKeyBytes, a.getSessionKeyBytes()));
		assertNull("still has file?", a.getFile());
	}

	public void testUidProxy() throws Exception
	{
		UniversalId uid = UniversalId.createDummyUniversalId();
		String label = "label";
		AttachmentProxy a = new AttachmentProxy(uid, label, null);
		assertEquals("wrong uid?", uid, a.getUniversalId());
		assertEquals("wrong label?", label, a.getLabel());
		assertNull("has file?", a.getFile());

	}

	public void testStringProxy() throws Exception
	{
		String label = "label";
		AttachmentProxy a = new AttachmentProxy(label);
		assertEquals(label, a.getLabel());
		assertNull("file", a.getFile());
	}
}
