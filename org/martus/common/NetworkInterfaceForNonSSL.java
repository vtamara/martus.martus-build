/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2002, Beneficent
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

import java.util.Vector;

public interface NetworkInterfaceForNonSSL
{
	public String ping();
	public Vector getServerInformation();
	public String requestUploadRights(String authorAccountId, String tryMagicWord);
	public String uploadBulletin(String authorAccountId, String bulletinLocalId, String data);
	public String uploadBulletinChunk(String authorAccountId, String bulletinLocalId, int totalSize, int chunkOffset, int chunkSize, String data, String signature);
	public Vector downloadBulletin(String authorAccountId, String bulletinLocalId);
	public Vector downloadMyBulletinChunk(String authorAccountId, String bulletinLocalId, int chunkOffset, int maxChunkSize, String signature);
	public Vector listMyBulletinSummaries(String authorAccountId);
	public Vector listFieldOfficeBulletinSummaries(String hqAccountId, String authorAccountId);
	public Vector listFieldOfficeAccounts(String hqAccountId);
	public Vector downloadPacket(String authorAccountId, String packetLocalId);
	public String authenticateServer(String tokenToSign);
}
