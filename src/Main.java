import java.io.File;

public class Main
{

	private static final String basePath="currentBenchmarks";

	// -1 for no time limit
	private static final long MAX_TIME_IN_SECONDS=300;

	public static void main(String[] args)
	{
		System.out.println("\nPress 'q' then 'Enter' to stop the program.");
		// Create a File object for the directory
		File directory=new File(basePath);

		// Check if the directory exists and is indeed a directory
		if (directory.exists() && directory.isDirectory())
		{
			// Get all the files in the directory
			File[] filesList=directory.listFiles();
			if (filesList!=null)
			{
				// For each file in the directory start  a new initializer
				for (File file : filesList)
				{
					Initializer initializer=new Initializer(basePath+"/", file.getName(), MAX_TIME_IN_SECONDS);
					initializer.start();
					System.gc();
				}
			}
		}
		System.exit(0);
	}
}

// todo dump per crash
// todo fare programmino per le performance spaziali e temporali, ruba le righe della matrice e delle velocità con un parserino e fa un grafico