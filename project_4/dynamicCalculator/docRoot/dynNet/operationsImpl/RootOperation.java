package dynNet.operationsImpl;

import dynNet.dynCalculator.Operation;
import java.lang.Math.*;



/**
 * Class [RootOperation]
 * <p>
 * This is a concrete operation class, that implements the
 * interface <code>Operation</code>.
 *
 * @author Daniel Rydberg, Joe Domabyl, Nick Nannen
 * @version May 20002
 */
public class RootOperation implements Operation{
	
	public double calculate(float firstNumber, float secondNumber){
		return Math.pow((double)firstNumber, (double)secondNumber);
	}
}