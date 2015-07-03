package algorithms.pso_ga.draw;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
	public static void createRenderedImage(float[][] data, String fileName) {

		int fWidth = data.length;
		int fHeight = data[0].length;
		//int[] fPixels=rgbRender(data);
		//int[] fPixels=grayRender(data);
		int[] fPixels=colorRender(data);
		
		/*Save image*/
		BufferedImage bi = new BufferedImage(fWidth, fHeight,
				BufferedImage.TYPE_INT_RGB);
		// Get the writable raster so that data can be changed.
		WritableRaster wr = bi.getRaster();
		// Write the byte data to the raster
		wr.setDataElements(0, 0, fWidth, fHeight, fPixels);
		// Export to file
		File f = new File(fileName);
		try {
			ImageIO.write(bi, "bmp", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	static int[] grayRender(float[][] data){
		int fWidth = data.length;
		int fHeight = data[0].length;
		int[] fPixels = new int[fWidth * fHeight];
		int j = 0;	
		// Using a different more mainstream method , 5 colors {red,yellow,green,aqua,blue}
		
		/* Setup */
		// use red color for valleys , or for hills
		boolean redValley = true;
		// intervals to use for each color between 0 and 256; {red,yellow,green,aqua,blue}
		double intervals[] = { 0, 0.001, 10, 50, 256 };

		/*Create image*/
		double limits[] = { 0, 64, 128, 192, 255 };
		double[] val = limits.clone();
		if (redValley) {
			for (int k = 0; k < val.length; k++)
				val[k] = 255 - intervals[intervals.length - k - 1];
		}

		for (int row = fHeight - 1; row >= 0; row--) {
			for (int col = 0; col < fWidth; col++) {
				int red , green, blue;
				red=green=blue = (int)Math.floor(data[col][row]);

				int alpha = 255;
				fPixels[j++] = (alpha << 24) | (red << 16) | (green << 8)
						| blue;
			}// end col loop
		}// end row loop
		return fPixels;
	}
	
	
	static int[] colorRender(float[][] data){
		int fWidth = data.length;
		int fHeight = data[0].length;
		int[] fPixels = new int[fWidth * fHeight];
		int j = 0;	
		// Using a different more mainstream method , 5 colors {red,yellow,green,aqua,blue}
		
		/* Setup */
		// use red color for valleys , or for hills
		boolean redValley = true;
		// intervals to use for each color between 0 and 256; {red,yellow,green,aqua,blue}
		//double intervals[] = { 0, 1, 20, 50, 256 };
		double intervals[] = { 0, 64, 128, 192, 255};

		/*Create image*/
		double limits[] = { 0, 64, 128, 192, 255 };
		double[] val = limits.clone();
		if (redValley) {
			for (int k = 0; k < val.length; k++)
				val[k] = 255 - intervals[intervals.length - k - 1];
		}

		for (int row = fHeight - 1; row >= 0; row--) {
			for (int col = 0; col < fWidth; col++) {
				int red = 0;
				int green = 0;
				int blue = 0;
				double d = data[col][row];
				if (redValley)
					d = 255 - d;
				for (int i = 1; i < limits.length; i++) {
					if (d >= val[i - 1] && d <= val[i]) {
						int temp = (int) Math
								.floor(4 * ((d - val[i - 1])
										* (limits[i] - limits[i - 1]) / (val[i] - val[i - 1])));
						
					    if (i == 1) {
							red = 0;
							blue = 255;
							green = temp;
						} else if (i == 2) {
							red = 0;
							blue = 255 - temp;
							green = 255;
						} else if (i == 3) {
							blue = 0;
							green = 255;
							red = temp;
						} else if (i == 4) {
							blue = 0;
							green = 255 - temp;
							red = 255;
						}
						break;

					}

				}

				int alpha = 255;
				fPixels[j++] = (alpha << 24) | (red << 16) | (green << 8)
						| blue;
				// Color cl = new Color(red,green,blue);
			}// end col loop
		}// end row loop
		return fPixels;
	}
	
	
	
	static int[] rgbRender(float[][] data){
		int fWidth = data.length;
		int fHeight = data[0].length;
		int[] fPixels = new int[fWidth * fHeight];

		int j = 0;
		
		// Build the array of pixels with the color components.
		float color[][] = { { 0.0f, 255, 0, 0 }, { 1f, 128, 128, 0 },
				{ 10f, 0, 255, 0 }, { 40f, 0, 128, 128 },
				{ 100, 0, 0, 255 }, { 260, 0, 0, 0 } };
		for (int y = fHeight - 1; y >= 0; y--) {
			for (int x = 0; x < fWidth; x++) {

				for (int i = 1; i < color.length; i++) {
					if (data[x][y] < color[i][0]) {
						float[] rgb = new float[3];
						for (int c = 1; c <= 3; c++) {
							float X = data[x][y];
							float x1 = color[i - 1][0];
							float x2 = color[i][0];
							float y1 = color[i - 1][c];
							float y2 = color[i][c];
							rgb[c - 1] = Math.abs(y1 - (X - x1) * (y1 - y2)
									/ (x2 - x1));
						}

						int red = Math.round(rgb[0]);
						int green = Math.round(rgb[1]);
						int blue = Math.round(rgb[2]);
						int alpha = 255; // non-transparent

						if (data[x][y] < 0)
							red = green = blue = 0;
						fPixels[j++] = (alpha << 24) | (red << 16)
								| (green << 8) | blue;

						break;
					}

				}

			}
		}
		return fPixels;
	}
	
	
}