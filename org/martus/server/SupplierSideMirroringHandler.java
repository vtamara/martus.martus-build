package org.martus.server;

import java.util.Vector;

import org.martus.common.Database;
import org.martus.common.MartusCrypto;
import org.martus.common.MartusUtilities;

public class SupplierSideMirroringHandler implements MirroringInterface
{
	SupplierSideMirroringHandler(Database supplierDatabase, MartusCrypto verifierToUse)
	{
		db = supplierDatabase;
		verifier = verifierToUse;
		authorizedCallers = new Vector();
	}
	
	public void clearAllAuthorizedCallers()
	{
		authorizedCallers.clear();
	}
	
	public void addAuthorizedCaller(String authorizedAccountId)
	{
		authorizedCallers.add(authorizedAccountId);
	}
	
	public Vector request(String callerAccountId, Vector parameters, String signature)
	{
		if(!MartusUtilities.verifySignature(parameters, verifier, callerAccountId, signature))
		{
			Vector result = new Vector();
			result.add(SIG_ERROR);		
			return result;
		}

		if(!isAuthorizedToBackup(callerAccountId))
		{
			Vector result = new Vector();
			result.add(NOT_AUTHORIZED);
			return result;
		}

		Vector result = new Vector();
		try
		{
			return executeCommand(callerAccountId, parameters);
		}
		catch (RuntimeException e)
		{
			result = new Vector();
			result.add(BAD_PARAMETER);
			return result;
		}

	}

	Vector executeCommand(String callerAccountId, Vector parameters)
	{
		Vector result = new Vector();

		int cmd = extractCommand(parameters.get(0));
		switch(cmd)
		{
			case cmdPing:
			{
				result.add(OK);
				return result;
			}
			case cmdListAccountsForBackup:
			{
				Vector accounts = listAccounts(callerAccountId);
	
				result.add(OK);
				result.add(accounts);
				return result;
			}
			case cmdListSealedBulletinsForBackup:
			{
				String authorAccountId = (String)parameters.get(1);
				Vector infos = listSealedBulletins(callerAccountId, authorAccountId);
			}
			default:
			{
				result = new Vector();
				result.add(UNKNOWN_COMMAND);
			}
		}
		
		return result;
	}

	Vector listAccounts(String callerAccountId)
	{
		class Collector implements Database.AccountVisitor
		{
			public void visit(String accountId)
			{
				accounts.add(accountId);
			}
			
			Vector accounts = new Vector();
		}

		Collector collector = new Collector();		
		db.visitAllAccounts(collector);
		return collector.accounts;
	}
	
	Vector listSealedBulletins(String callerAccountId, String authorAccountId)
	{
		return null;
	}
	
	int extractCommand(Object possibleCommand)
	{
		String cmdString = (String)possibleCommand;
		if(cmdString.equals(CMD_PING))
			return cmdPing;

		if(cmdString.equals(CMD_LIST_ACCOUNTS_FOR_BACKUP))
			return cmdListAccountsForBackup;
		
		if(cmdString.equals(CMD_LIST_SEALED_BULLETINS_FOR_BACKUP))
			return cmdListSealedBulletinsForBackup;
		
		return cmdUnknown;
	}
	
	boolean isAuthorizedToBackup(String callerAccountId)
	{
		return authorizedCallers.contains(callerAccountId);
	}

	public static class UnknownCommandException extends Exception {}

	final static int cmdUnknown = 0;
	final static int cmdPing = 1;
	final static int cmdListAccountsForBackup = 2;
	final static int cmdListSealedBulletinsForBackup = 3;
	
	Database db;
	MartusCrypto verifier;
	
	Vector authorizedCallers;
}
