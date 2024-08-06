import java.io.File;

public class Main
{

	private static final String basePath="currentBenchmarks";

	// -1 for no time limit
	private static final long MAX_TIME_IN_SECONDS=300;

	// threads to be used in generateChildren
	private static final int NUM_THREADS=14;

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
					Initializer initializer = new Initializer(basePath+"/", file.getName(), MAX_TIME_IN_SECONDS, NUM_THREADS);
					initializer.start();
					System.gc();
				}
			}
		}
		System.exit(0);
	}
}
