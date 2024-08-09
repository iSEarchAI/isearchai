package br.otimizes.isearchai.simulator;
import java.util.Random;
/**
 * This Class is used as a simulator of different types of profile for evaluating solutions.
 * This way, depending on the profile chosen, the evaluation of a  given  can  be different.
 * The evaluation works on the following way: First of all one of the available profiles  is
 * chosen. Then according to the chosen profile  the  attributes  will  be  added  in  the
 * target-individual. The number of attributes to be added is set with a given  percentage.
 * Thus, a solution is evaluated according to the  similarity between it and the target-individual.
 *
 * @since 03-31-2015
 * @author --
 *
 */
public class HumanSimulator {
	/**
	 * Maximum score the Human can give to a solution
	 */
	private double maxHumanEvaluation;
	/**
	 * Stores the number of attributes
	 */
	private int numberOfAttributes;
	/**
	 * Stores the attributes Scores of the attributes
	 */
	private double[] attributesScore;
	/**
	 * Stores attributes Costs
	 */
	private double[] attributesCost;
	/**
	 * Stores the number of attributes the target solutions has.
	 */
	private int targetSolutionPercentage;
	/**
	 * Stores Target Solution
	 */
	private int[] targetSolution;

	private Random random;
	/**
	 * Constructor
	 */
	public HumanSimulator(){
		random = new Random();
		this.maxHumanEvaluation = 100;
		targetSolutionPercentage = 50;
	}
	/**
	 *
	 */
	public void setScoreValues (double[] attributesScore) {
		this.attributesScore = attributesScore;
		this.numberOfAttributes = attributesScore.length;
	}
	/**
	 *
	 */
	public void setCostValues (double[] attributesCost) {
		this.attributesCost = attributesCost;
		this.numberOfAttributes = attributesCost.length;
	}
	/**
	 * Set the Profile used to generate a target individual
	 * @param human
	 */
	public void setHumanSimulatorProfile (String human) {

		int nAttributesInSolution = getNumberOfAttributesInIndividual(targetSolutionPercentage);
		/*
		 * Generate individual RandomLy
		 */
		if(human == "RANDOM"){
			this.targetSolution = getRandomlyTargetSolution(nAttributesInSolution);
		}
		/*
		 * Order the attributes by score and return a solution with the higher score attributes
		 */
		else if (human == "HIGHER_SCORE") {
			this.targetSolution = getHigherScoreTargetSolution(nAttributesInSolution);
		}
		/*
		 * Order the attributes by score and return a solution with the lowest score attributes
		 */
		else if(human == "LOWER_SCORE") {
			this.targetSolution = getLowerScoreTargetSolution(nAttributesInSolution);
		}
		/*
		 * Order the attributes by cost and return a solution with the higher cost attributes
		 */
		else if (human == "HIGHER_COST") {
			this.targetSolution = getHigherCostTargetSolution(nAttributesInSolution);
		}
		/*
		 * Order the attributes by cost and return a solution with the lowest cost attributes
		 */
		else if (human == "LOWER_COST") {
			this.targetSolution = getLowerCostTargetSolution(nAttributesInSolution);
		}
		/*
		 * This method has a set of solutions generated manually
		 */
		else if (human == "MANUALLY") {
			this.targetSolution = getManuallyTargetSolution();
		}
		/*
		 *
		 */
		else {
			System.out.println("Profile NOT FOUND!");
			System.exit(0);
		}
	}
	/**
	 *
	 * @return
	 */
	public double getMaxHumanEvaluation(){
		return this.maxHumanEvaluation;
	}
	/**
	 * Evaluate a given individual according to the target solution
	 * @return "subjective" evaluation
	 */
	public int getHumanEvaluation(int[] individual){
		double humanEvaluation = 0;
		double numberOfSimilaritiesInIndividual = getNumberOfSimilaritiesInIndividual(individual);

		humanEvaluation = (maxHumanEvaluation * numberOfSimilaritiesInIndividual) / targetSolution.length;

		return (int) Math.round(humanEvaluation);
	}
	/**
	 *
	 * @return the target solution
	 */
	public int[] getTargetSolution(){
		return this.targetSolution;
	}
	/**
	 *
	 */
	private int[] getManuallyTargetSolution(){
		int[] targetSolution = null;

		if(50 == attributesCost.length) {
			targetSolution = new int[] {1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0};
		}

		else if(100 == attributesCost.length){
		targetSolution = new int[] {0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1};
		}

		else if(150 == attributesCost.length){
			targetSolution = new int[] {0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0};
		}

		else if(200 == attributesCost.length){
			targetSolution = new int[] {0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1};
		}
		else {
			System.out.println("There is no TARGET for such size!");
			System.exit(0);
		}

		return targetSolution;
	}
	/**
	 *
	 */
	private int getNumberOfAttributesInIndividual(int attributesPercentage){
		return (int) Math.round((attributesPercentage / 100.0) * numberOfAttributes);
	}
	/**
	 *
	 */
	private int[] getRandomlyTargetSolution(int numberOfAttributesInSolution){
		int[] targetSolution = new int[numberOfAttributes];

		for(int i = 0; i <= targetSolution.length - 1; i++){
			targetSolution[i] = -1;
		}

		for(int i = 0; i <= numberOfAttributesInSolution - 1; i++){
			int randomAttribute = random.nextInt(numberOfAttributes);

			while(isAttributeInTargetSolution(randomAttribute, targetSolution) == true){
				randomAttribute = random.nextInt(numberOfAttributes);
			}

			targetSolution[randomAttribute] = 1;
		}

		for(int i = 0; i <= targetSolution.length - 1; i++){
			if(targetSolution[i] != 1){
				targetSolution[i] = 0;
			}
		}

		return targetSolution;
	}
	/**
	 *
	 */
	private boolean isAttributeInTargetSolution(int attribute, int[] targetSolution){
		boolean result = false;

		for(int i = 0; i <= targetSolution.length - 1; i++){
			if(targetSolution[attribute] == 1){
				result = true;
				break;
			}
		}

		return result;
	}
	/**
	 *
	 */
	private int[] getHigherScoreTargetSolution(int numberOfAttributesInSolution){
		int[] targetSolution = new int[numberOfAttributes];
		int[] attributesSelectedFlags = new int[numberOfAttributes];
		int higherScoreAttribute = 0;

		for(int i = 0; i <= numberOfAttributesInSolution - 1; i++){
			higherScoreAttribute = selectHigherScoreAttribute(attributesSelectedFlags);
			attributesSelectedFlags[higherScoreAttribute] = 1;
			targetSolution[higherScoreAttribute] = 1;
		}

		return targetSolution;
	}
	/**
	 *
	 */
	private int selectHigherScoreAttribute(int[] attributesSelectedFlags){
		int higherScoreAttribute = getFirstAttributeNotSelected(attributesSelectedFlags);

		for(int i = 0; i <= attributesScore.length - 1; i++){
			if(attributesScore[i] > attributesScore[higherScoreAttribute] && attributesSelectedFlags[i] == 0){
				higherScoreAttribute = i;
			}
		}

		return higherScoreAttribute;
	}
	/**
	 *
	 */
	private int getFirstAttributeNotSelected(int[] attributesSelectedFlags){
		int firstAttributeNotSelected = 0;

		for(int i = 0; i <= attributesSelectedFlags.length - 1; i++){
			if(attributesSelectedFlags[i] == 0){
				firstAttributeNotSelected = i;
				break;
			}
		}

		return firstAttributeNotSelected;
	}
/**
 *
 */
	private int[] getLowerScoreTargetSolution(int numberOfAttributesInSolution){
		int[] targetSolution = new int[numberOfAttributes];
		int[] attributesSelectedFlags = new int[numberOfAttributes];
		int lowerScoreAttribute = 0;

		for(int i = 0; i <= numberOfAttributesInSolution - 1; i++){
			lowerScoreAttribute = selectLowerScoreAttribute(attributesSelectedFlags);
			attributesSelectedFlags[lowerScoreAttribute] = 1;
			targetSolution[lowerScoreAttribute] = 1;
		}

		return targetSolution;
	}
	/**
	 *
	 */
	private int selectLowerScoreAttribute(int[] attributesSelectedFlags){
		int lowerScoreAttribute = getFirstAttributeNotSelected(attributesSelectedFlags);

		for(int i = 0; i <= attributesScore.length - 1; i++){
			if(attributesScore[i] < attributesScore[lowerScoreAttribute] && attributesSelectedFlags[i] == 0){
				lowerScoreAttribute = i;
			}
		}

		return lowerScoreAttribute;
	}
	/**
	 *
	 */
	private int[] getHigherCostTargetSolution(int numberOfAttributesInSolution){
		int[] targetSolution = new int[numberOfAttributes];

		int[] attributesSelectedFlags = new int[numberOfAttributes];
		int higherCostAttribute = 0;

		for(int i = 0; i <= numberOfAttributesInSolution - 1; i++){
			higherCostAttribute = selectHigherCostAttribute(attributesSelectedFlags);
			attributesSelectedFlags[higherCostAttribute] = 1;
			targetSolution[higherCostAttribute] = 1;
		}

		return targetSolution;
	}
	/**
	 *
	 */
	private int selectHigherCostAttribute(int[] attributesSelectedFlags){
		int higherCostAttribute = getFirstAttributeNotSelected(attributesSelectedFlags);

		for(int i = 0; i <= attributesCost.length - 1; i++){
			if(attributesCost[i] > attributesCost[higherCostAttribute] && attributesSelectedFlags[i] == 0){
				higherCostAttribute = i;
			}
		}

		return higherCostAttribute;
	}
	/**
	 *
	 */
	private int[] getLowerCostTargetSolution(int numberOfAttributesInSolution){
		int[] targetSolution = new int[numberOfAttributes];

		int[] attributesSelectedFlags = new int[numberOfAttributes];
		int lowerCostAttribute = 0;

		for(int i = 0; i <= numberOfAttributesInSolution - 1; i++){
			lowerCostAttribute = selectLowerCostAttribute(attributesSelectedFlags);
			attributesSelectedFlags[lowerCostAttribute] = 1;
			targetSolution[lowerCostAttribute] = 1;
		}

		return targetSolution;
	}
	/**
	 *
	 */
	private int selectLowerCostAttribute(int[] attributesSelectedFlags){
		int lowerCostAttribute = getFirstAttributeNotSelected(attributesSelectedFlags);

		for(int i = 0; i <= attributesCost.length - 1; i++){
			if(attributesSelectedFlags[i] == 0)
			if(attributesCost[i] < attributesCost[lowerCostAttribute] && attributesSelectedFlags[i] == 0){
				lowerCostAttribute = i;
			}
		}

		return lowerCostAttribute;
	}
	/**
	 *
	 */
	public int getNumberOfSimilaritiesInIndividual(int[] individual){
		int numberOfSimilaritiesInIndividual = 0;

		for(int i = 0; i <= targetSolution.length - 1; i++){
			if(targetSolution[i] == individual[i]){
				numberOfSimilaritiesInIndividual++;
			}
		}

		return numberOfSimilaritiesInIndividual;
	}
	/**
	 *
	 */
	public int getNumberOfAttributes(){
		return numberOfAttributes;
	}
}
