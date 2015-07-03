package algorithms.pso_ga.draw;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveMatrix {

	public static void saveImage(float[][] matrix,String fileName) 
	{
		System.out.print("\nsaving matrix...");
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter( fileName );

			BufferedWriter bufferedWriter = new BufferedWriter( fileWriter );

			for ( int i = matrix[0].length-1 ; i >=0 ; i-- ) // y
			{
				String imageRow= "";

				for ( int j = 0 ; j < matrix.length ; j++ )// x
				{
					imageRow += matrix[ j ][ i ] + ",";
				}

				bufferedWriter.write( imageRow );
				bufferedWriter.newLine();
			}

			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("OK.");
	}		

	public static void saveMatrix(float[][] matrix,String fileName) 
	{
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter( fileName );

			BufferedWriter bufferedWriter = new BufferedWriter( fileWriter );

			for ( int i = 0 ; i < matrix.length ; i++ )
			{
				String matrixRow= "";

				for ( int j = 0 ; j < matrix[ i ].length ; j++ )
				{
					matrixRow += matrix[ i ][ j ] + ",";
				}

				bufferedWriter.write( matrixRow );
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
