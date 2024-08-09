package br.otimizes.isearchai.simulator;

import weka.core.*;

/**
 *
 * This class is a platform that stores the individuals and their respective human evaluations.
 * Thus, a dataSet (Type Instances) is available for training the classifiers in WEKA API.
 *
 * @since 03-31-2015
 * @author --
 *
 */
public class DataSet {

	public Instances dataSet;
	FastVector featureVector;
	int numberOfAttributes;

	public DataSet(int maxOfEvaluations, int numberOfAttributes) {

		this.numberOfAttributes = numberOfAttributes;
		Attribute[] labelAttributes = new Attribute[numberOfAttributes];

		for (int i = 0; i < numberOfAttributes; i++) {
			labelAttributes[i] = new Attribute("r"+(i+1));
		}

		Attribute classifier = new Attribute("evaluation");

		featureVector = new FastVector(numberOfAttributes +1);
		for (int i = 0; i < numberOfAttributes; i++) {
			featureVector.addElement(labelAttributes[i]);
		}

		featureVector.addElement(classifier);

		dataSet = new Instances ("trainingSet", featureVector, maxOfEvaluations);
		dataSet.setClassIndex(numberOfAttributes);
	}
	/**
	 * Method used to store a solution with its respective subjective evaluation
	 */
	public void insert(int[] individual, int she) {
		Instance aux = new DenseInstance(numberOfAttributes +1);
		for (int i = 0; i < individual.length; i++) {
			aux.setValue((Attribute)featureVector.elementAt(i), individual[i]);
		}
		aux.setValue((Attribute)featureVector.elementAt(numberOfAttributes), she);
		dataSet.add(aux);
	}
	/**
	 *
	 * @return A dataset used by Classifiers in WEKA
	 */
	public Instances getDataSet() {
		return dataSet;
	}
	/**
	 *
	 * @return Convert a given solution in instance
	 */
	public Instance getInstance(int [] individual) {
		Instance instance = new DenseInstance(numberOfAttributes + 1);
		for (int i = 0; i < individual.length; i++) {
			instance.setValue((Attribute)featureVector.elementAt(i), individual[i]);
		}
		return instance;
	}
}
