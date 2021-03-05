package com.developerdoge.chall2.unit;

import com.developerdoge.chall2.calculator.CALCULATOR_OPERATIONS;
import com.developerdoge.chall2.calculator.CALCULATOR_OUTPUTS;
import com.developerdoge.chall2.calculator.Calculator;
import com.developerdoge.chall2.calculator.CalculatorInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CalculatorTest {

    @Test
    public void canParseValidInput(){

        String input = "{\n" +
                "  \"values\": [5, 1, 20],\n" +
                "  \"operation\": \"add\",\n" +
                "  \"type\": \"integer\"\n" +
                "}";

        Calculator calculator = new Calculator(input);

        Assertions.assertTrue(calculator.isValid(),"error creating calculator with valid input");

        //Test fields of the input
        CalculatorInput calculatorInput = calculator.getInput();


        Assertions.assertTrue(calculatorInput.type == CALCULATOR_OUTPUTS.integer,"mismatched output type");
        Assertions.assertTrue(calculatorInput.operation == CALCULATOR_OPERATIONS.add,"mismatched operation type");

        //Compare contents of list
        List<Number> testList = List.of(5,1,20);

        Assertions.assertTrue(testList.containsAll(calculatorInput.values),"mismatched values");

    }

    @Test
    public void failsOnInvalidInput(){

        String input = "{\n" +
                "  \"values\": [5, 1, 20],\n" +
                "  \"operation\": \"add\",\n" +
                "  \"type\": \"banana\"\n" +
                "}";

        Calculator calculator = new Calculator(input);

        Assertions.assertTrue(!calculator.isValid(),"giving invalid input as valid.");

    }


}
