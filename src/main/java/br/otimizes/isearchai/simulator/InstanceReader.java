package br.otimizes.isearchai.simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class InstanceReader {

	private BufferedReader reader;
	/**
	 * Constructor
	 * @param instance
	 */
	public InstanceReader(File instance){
		try{
			this.reader = new BufferedReader(new FileReader(instance));
			reader.mark(1);
		}
		catch(Exception e){
			System.out.println("Instance reading error");
			e.printStackTrace();
		}
	}
	/**
	 * Get Number Of Clients
	 * @return
	 */
	private int getNumberOfCustomers(){
		String[] values = null;
		int numberOfCustomers = 0;

		try{
			reader.reset();
			values = reader.readLine().split(" ");
			numberOfCustomers = Integer.parseInt(values[0]);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return numberOfCustomers;
	}
	/**
	 * Get number of attributes from the specified instance
	 * @return
	 */
	private int getNumberOfAttributes(){
		String[] values = null;
		int numberOfAttributes = 0;

		try{
			reader.reset();
			values = reader.readLine().split(" ");
			numberOfAttributes = Integer.parseInt(values[1]);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return numberOfAttributes;
	}
	/**
	 * Get importance of the clients from the instance
	 * @return
	 */
	public double[] getCustomersImportance(){
		double[] customersImportance = new double[getNumberOfCustomers()];
		String[] values = null;

		try{
			reader.reset();
			reader.readLine();
			reader.readLine();
			values = reader.readLine().split(" ");
		}
		catch(Exception e){
			e.printStackTrace();
		}

		for(int i = 0; i <= customersImportance.length - 1; i++){
			customersImportance[i] = Double.parseDouble(values[i]);
		}

		return customersImportance;
	}
	/**
	 * Get Importances of the attributes
	 * @return
	 */
	public double[][] getAttributesImportances(){
		double[][] attributesImportances = new double[getNumberOfCustomers()][getNumberOfAttributes()];
		String[] values = null;

		try{
			reader.reset();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();

			for(int i = 0; i <= attributesImportances.length - 1; i++){
				values = reader.readLine().split(" ");

				for(int j = 0; j <= attributesImportances[0].length - 1; j++){
					attributesImportances[i][j] = Integer.parseInt(values[j]);
				}
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}

		return attributesImportances;
	}
	/**
	 * Get attribute Cost from the instance
	 * @return
	 */
	public double[] getAttributesCosts(){
		double[] attributesCosts = new double[getNumberOfAttributes()];
		int numberOfCustomers = getNumberOfCustomers();
		String[] values = null;

		try{
			reader.reset();
			for(int i = 0; i <= 5 + numberOfCustomers - 1; i++){
				reader.readLine();
			}

			values = reader.readLine().split(" ");
		}
		catch(Exception e){
			e.printStackTrace();
		}

		for(int i = 0; i <= attributesCosts.length - 1; i++){
			attributesCosts[i] = Integer.parseInt(values[i]);
		}

		return attributesCosts;
	}
}
