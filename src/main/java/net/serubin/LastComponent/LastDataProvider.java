package net.serubin.LastComponent;

import net.escapecraft.component.Log;
import net.escapecraft.escapeutils.EscapeUtils;
import org.bukkit.Location;

import java.io.*;
import java.util.*;

/**
 * Created by seru on 8/9/15.
 */
public class LastDataProvider {

	private final LastComponent last;
	private String dataFileName = "last.csv";
	private File dataFile = null;

	protected Map<UUID, Location> deaths;
	protected Map<UUID, Double> logins;

	// Parse locations
	private final int USER = 0;
	private final int LOGIN = 1;

	public LastDataProvider(EscapeUtils plugin, LastComponent last) {
		this.last = last;

		// Creates new file
		dataFile = new File(plugin.getDataFolder(), dataFileName);
		last.logInfo("Trying file: " + dataFile.getPath() + " exists? " + dataFile.exists());

		if (!dataFile.exists()) {
			last.logInfo("Creating '" + dataFileName + "'...");
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				last.logWarning("'" + dataFileName + "' could not be created!");
				e.printStackTrace();
			}
		}

		// make the variables come ALIVE
		deaths = new HashMap<UUID, Location>();
		logins = new HashMap<UUID, Double>();
	}

	/**
	 * Fetches data from flat file and saves it to memory
	 * @return true/false
	 */
	public boolean fetchData() {
		try {
			FileReader fStreamIn = new FileReader(dataFile);
			BufferedReader in = new BufferedReader(fStreamIn);

			String line = in.readLine();
			int lineNumber = 0;
			while (line != null) {
				last.logInfo("Line: " + line);
				String[] parts = line.split(",");
				UUID uuid;
				// UUID
				String user = parts[USER]; // Parses out uuid
				try {
					String[] bitStr = user.split(":");
					Long[] bits = new Long[2];

					bits[0] = Long.parseLong(bitStr[0]);
					bits[1] = Long.parseLong(bitStr[1]);

					uuid = new UUID(bits[0], bits[1]);

				} catch (NumberFormatException e) {
					last.logWarning("There was a problem loading user on line number "
							+ Integer.toString(lineNumber) + "!");
					continue;
				}
				// LOGIN TIME
				String login = parts[LOGIN];

				Double loginTime = null;

				// Parse timestamp
				try {
					loginTime = Double.parseDouble(login);
				} catch (NumberFormatException ex) {
					last.logWarning("There was a problem loading last login for user '"
							+ user + "' on line number "
							+ Integer.toString(lineNumber) + "!");
					continue;
				}

				logins.put(uuid, loginTime);

				line = in.readLine();
			}

			in.close();
			fStreamIn.close();
		} catch (IOException e) {
			last.logWarning("There was an error loading warp data...");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Pushes login data to flatfile
	 * @return true/false
	 */
	public boolean pushData() {

		try {
			// Creates out writers
			FileWriter fStreamOut = new FileWriter(dataFile);
			BufferedWriter out = new BufferedWriter(fStreamOut);

			// Iteration through login maps for saving
			for (Map.Entry<UUID, Double> entry : logins.entrySet()) {
				UUID key = entry.getKey();
				Double value = entry.getValue();

				// Writes
				out.write(key.getMostSignificantBits() + ":" + key.getLeastSignificantBits() + "," + value);
				out.newLine();
			}

			out.close();
			fStreamOut.close();

		} catch (IOException e) {
			last.logWarning("[LastReport] There was an error saving stored data.");
			return false;
		}
		return true;
	}
}
