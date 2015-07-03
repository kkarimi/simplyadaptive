package algorithms.pso_ga.draw;


public class TestImage {
//	public static void createRenderedImage(float[][] data,String fileName) {
//		
//		float color[][]={ 	{0f  	,255	,0 		,0  },
//							{1f 	,128  	,128	,0  },
//							{30f 	,0		,255	,0},
//							{60f 	,0		,128	,128},
//							{100	,0		,0  	,255}
//					  };
//		
//		int fWidth  = data.length;
//	    int fHeight = data[0].length;
//		int[] fPixels = new int [fWidth * fHeight];
//
//		int j=0;
//
//		// Build the array of pixels with the color components.
//		for  (int y = 0; y < fHeight; y++) {
//			for ( int x = 0; x < fWidth; x++) {
//				for(int i=1;i<color.length;i++){
//					if(data[x][y]<color[i][0]){
//						float[] rgb=new float[3];
//						for(int c=1; c<=3;c++){
//							float X=data[x][y];
//							float x1=color[i-1][0];
//							float x2=color[i][0];
//							float y1=color[i-1][c];
//							float y2=color[i][c];
//							rgb[c-1]=Math.abs(y1-(X-x1)*(y1-y2)/(x2-x1));
//						}
//							
//							
//						int red = Math.round( rgb[0]);
//						int green=Math.round( rgb[1]);
//						int blue=Math.round( rgb[2]);
//						int alpha = 255; // non-transparent
//						fPixels[j++] =  (alpha << 24) |  (red << 16)
//						|  (green << 8 ) | blue;
//						
//						if( red==0){
//							int aaa=0;
//						}
//						break;
//					}
//					
//				}
//				
//			}
//		}
//
//		BufferedImage bi =
//			new BufferedImage (fWidth, fHeight, BufferedImage.TYPE_INT_RGB);
//		// Get the writable raster so that data can be changed.
//		WritableRaster wr = bi.getRaster();
//
//		// Now write the byte data to the raster
//		wr.setDataElements (0, 0, fWidth, fHeight, fPixels);
//
//		File f = new File (fileName);
//		try {
//			ImageIO.write (bi, "bmp", f);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	} 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		float min=Float.MAX_VALUE;
		float max=Float.MIN_VALUE;
		float resolution=0.01f;
		int dim=Math.round(1/resolution);
		float[][] a=new float[dim][dim];
		for (int x = 0; x < dim; x+=1) {
			for (int y = 0; y < dim; y+=1) {
				float[] d={(x)/(float)dim,(y)/(float)dim,.5f};
				
				a[x][y]=(y-dim/2)/(float)dim;
				//System.out.println((n+550)/1000.0+"\t"+(j+850)/1000.0+"\t"+a[n][j]);
				if(a[x][y]<min) min=a[x][y];
				if(a[x][y]>max) max=a[x][y];
			}
			if(x%100==0)System.out.println(x);
		}
		System.out.println(min+"\t"+max);
		//normalize(0-100)
		for (int x = 0; x < dim; x+=1) {
			for (int y = 0; y < dim; y+=1) {
				//System.out.println(a[x][y]);
				a[x][y]=(float)255*(a[x][y]-min)/(max-min);
				//System.out.println(a[x][y]);
			}
		}
		System.out.println(min+"\t"+max);
		ImageUtils.createRenderedImage(a, "test.bmp");
	}

}
