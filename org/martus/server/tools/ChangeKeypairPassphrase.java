package org.martus.server.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.martus.common.MartusCrypto;
import org.martus.server.forclients.MartusServerUtilities;

public class ChangeKeypairPassphrase
{
	public static void main(String[] args) throws Exception
	{
			System.out.println("ChangeServerPassphrase:\nThis program will replace your keypair.dat file."
				+ "\nWe strongly recommend that you make sure you have a backup copy before running this program. "
				+ "\nAlso, after successfully changing the password, we strongly recommend that you create a backup of the new keypair.dat file.\n");

			if( args.length == 0 || !args[0].startsWith("--keypair"))
			{
					System.err.println("Error: Incorrect argument.\nChangeServerPassphrase --keypair=/path/keypair.dat" );
					System.err.flush();
					System.exit(2);				
			}
			File keyPairFile = new File(args[0].substring(args[0].indexOf("=")+1));

			if(!keyPairFile.exists())
			{
				System.err.println("Error: There is no keypair file at location " + keyPairFile.getAbsolutePath() + "." );
				System.err.flush();
				System.exit(3);
			}

			System.out.print("Enter current passphrase:");
			System.out.flush();
			
			InputStreamReader rawReader = new InputStreamReader(System.in);	
			BufferedReader reader = new BufferedReader(rawReader);
			try
			{
				String oldPassphrase = reader.readLine();
				
				MartusCrypto security = MartusServerUtilities.loadCurrentMartusSecurity(keyPairFile, oldPassphrase);
				
				System.out.print("Enter new passphrase:");
				System.out.flush();
				String newPassphrase1 = reader.readLine();
				System.out.print("Re-enter the new passphrase:");
				System.out.flush();
				String newPassphrase2 = reader.readLine();
				
				if( newPassphrase1.equals(newPassphrase2) )
				{
					System.out.println("Updating passphrase...");
					System.out.flush();
					updateKeypairPassphrase(keyPairFile, newPassphrase1, security);
				}
				else
				{
					System.err.println("Passwords not the same");
					System.exit(3);
				}
			}
			catch(Exception e)
			{
				System.err.println("ChangeServerPassphrase.main: " + e);
				System.exit(3);
			}
			System.out.println("Server passphrase updated.");
			System.out.flush();
			System.exit(0);
	}
	
	private static void updateKeypairPassphrase(File keyPairFile, String newPassphrase, MartusCrypto security)
		throws FileNotFoundException, IOException
	{
		FileOutputStream out = new FileOutputStream(keyPairFile);
		security.writeKeyPair(out, newPassphrase);
	}
}