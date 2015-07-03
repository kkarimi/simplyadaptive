package com.sa.main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.sa.util.CSVReader;
import com.sa.util.CSVWriter;

public class AttributeSet {
	public ArrayList<ArrayList<Double>> dataMat;
	public String[] attributeNames;
	double[][][] values;
	int[] choices;


	public AttributeSet(String fileName){
		dataMat= new ArrayList<ArrayList<Double>>();
		CSVReader reader=null;
		try {
			reader = new CSVReader(new FileReader(fileName));
			String [] nextLine;
			nextLine = reader.readNext();
			this.attributeNames=nextLine;

			while ((nextLine = reader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				ArrayList<Double> dataVec=new ArrayList<Double>();
				for(int i=0; i<nextLine.length; i++){
					dataVec.add(Double.parseDouble(nextLine[i]));
				}
				dataMat.add(dataVec);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//skip first column

		normalize();

		int recCnt=dataMat.size();
		int attrCnt=dataMat.get(0).size();
		//if training set
		if(attrCnt>22){
			this.values=new double[recCnt][2][11];
			for(int i=0; i<11;i++){
				for (int j=0; j<recCnt;j++){
					values[j][0][i]=dataMat.get(j).get(i+1);
					values[j][1][i]=dataMat.get(j).get(i+12);
				}
			}
			this.choices= new int[recCnt];
			for (int j=0; j<recCnt;j++){
			//if(dataMat.get(j).get(0)==1)
					choices[j]=dataMat.get(j).get(0).intValue();
			//	else
			//		choices[j]=0;
			}
		}
		System.out.println("Parsing complete: "+dataMat.size()+" records");
	}

	public void normalize(){
		int recCnt=dataMat.size();
		int attrCnt=dataMat.get(0).size();
		double maxVec[] =new double[recCnt];
		double minVec[] =new double[recCnt];
		for(int i=0; i<attrCnt;i++){
			for (int j=0; j<recCnt;j++){
				if(maxVec[i]<dataMat.get(j).get(i))
				{
					maxVec[i]=dataMat.get(j).get(i);
				}
				if(minVec[i]>dataMat.get(j).get(i))
				{
					minVec[i]=dataMat.get(j).get(i);
				}
			}
		}	

		for(int i=0; i<attrCnt;i++){
			for (int j=0; j<recCnt;j++){
				dataMat.get(j).set(i, (dataMat.get(j).get(i)-minVec[i])/maxVec[i]);
			}
		}
	}	

	public void saveDiffMatrix(String fileName) throws  Exception{
		CSVWriter writer= new CSVWriter(new FileWriter(fileName),',',CSVWriter.NO_QUOTE_CHARACTER);		
		String[] names=Arrays.copyOfRange(this.attributeNames,0,12);
		writer.writeNext(names);

		String [] nextLine=new String[12];
		for(ArrayList<Double> compVec:dataMat){
			nextLine[0]=String.valueOf(Math.round(compVec.get(0)));
			for (int i=0; i<11;i++){
				//nextLine[i+1]= String.valueOf(compVec.get(i+1)-compVec.get(i+1+11));
				nextLine[i+1]= String.valueOf(compVec.get(i+1)-compVec.get(i+1+11));
			}
			writer.writeNext(nextLine);
		}
		writer.close();
	}





}






