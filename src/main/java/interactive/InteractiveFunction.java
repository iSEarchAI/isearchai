package interactive;

import br.otimizes.isearchai.learning.MLElement;
import br.otimizes.isearchai.learning.MLSolution;
import br.otimizes.isearchai.learning.MLSolutionSet;

/**
 * Interface that allows to adapt the method of interaction with the user
 */
public interface InteractiveFunction<T extends MLSolutionSet<S, MLElement>, S extends MLSolution<MLElement>> {

    T run(T solutionSet) throws Exception;
}
