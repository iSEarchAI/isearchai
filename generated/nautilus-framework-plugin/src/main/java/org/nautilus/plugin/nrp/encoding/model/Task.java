package org.nautilus.plugin.nrp.encoding.model;

import org.nautilus.core.util.Converter;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Task {

	protected static JMetalRandom random = JMetalRandom.getInstance();

	public doublecost;
	public doubleprofit;
	public doubleimportante

	public Task(doublecost,doubleprofit,doubleimportante) {
        this.cost = cost,this.profit = profit,this.importante = importante
	}

	public static Task getRandom() {

		return new Task(
			Converter.round(random.nextDouble(1, 10), 1),Converter.round(random.nextDouble(1, 10), 1),Converter.round(random.nextDouble(1, 10), 1)
		);
	}

	public String toString() {
		return Converter.toJson(this);
	}
}
