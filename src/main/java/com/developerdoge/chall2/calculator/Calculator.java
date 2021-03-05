package com.developerdoge.chall2.calculator;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.json.JavalinJson;

import java.text.DecimalFormat;
import java.util.List;

import static io.javalin.plugin.json.JavalinJson.fromJson;

/***
 * An immutable, one use class for parsing the challenge data.
 */
public class Calculator {

    private CalculatorInput input;
    private CalculatorResult result;
    private CalculatorError error;

    private final boolean hasError;


    public Calculator(String data){

        try {
            input = JavalinJackson.defaultObjectMapper().readValue(data,CalculatorInput.class);

        }
        catch (JsonProcessingException e) {
            error = new CalculatorError(e.getOriginalMessage());
            hasError = true;
            return;
        }

        if(input.values == null){
            hasError = true;
            error = new CalculatorError("invalid values "+input.operation.toString());
            return;
        }
        if(input.values.isEmpty()){
            hasError = true;
            error = new CalculatorError("empty input list");
            return;
        }


        try{
            result = new CalculatorResult(calculateResult());
        }
        catch(ArithmeticException exception){
            hasError = true;
            error = new CalculatorError(exception.toString());
            return;
        }
        hasError = false;

    }

    /***
     * calculate the result and assign it.
     *
     * assume all values contain decimals, output will be formatted appropriatly.
     */
    private String calculateResult() {
        double accumulator = 0;
        switch (input.operation) {

            case add -> {
                accumulator = 0;
                for (Number number : input.values) {
                    accumulator += number.doubleValue();
                }
            }
            case sub -> {
                accumulator = 0;
                for (Number number : input.values) {
                    accumulator -= number.doubleValue();
                }
            }
            case mul -> {
                accumulator = 1;
                for (Number number : input.values) {
                    accumulator *= number.doubleValue();
                }
            }
            case div -> {
                //this sort of doesn't make sene to do the usual way...
                //let's assign the first value to be the numberator, and the reat will divide it repeatly.
                accumulator = input.values.get(0).doubleValue();
                for (int index = 1; index < input.values.size(); index++) {
                    accumulator /= input.values.get(index).doubleValue();
                }
            }
        }

        switch (input.type) {

            case integer -> {
                return Integer.toString((int) accumulator);

            }
            case decimal -> {
               return Float.toString((float) accumulator);
            }
            case safe -> {

                //To be 'safe', let's follow significant figure rules.
                //The output will only have as many decimal places as the largest amount of places of the numbers.

                int decimalCount = 0;
                for (Number number : input.values) {
                    String[] data = Double.toString(number.doubleValue()).split("\\.");

                    if (data.length > 1) {

                        int count = data[1].length();
                        if (count > decimalCount) {

                            decimalCount = count;

                        }
                    }
                }
                StringBuilder decformat = new StringBuilder();
                decformat.append("#");
                if(decimalCount > 0){
                    decformat.append(".");
                }
                for (int i = 0; i < decimalCount; i++) {
                    decformat.append("#");
                }
                DecimalFormat format = new DecimalFormat(decformat.toString());
                return format.format(accumulator);
            }
        }
        return null;
    }



    public boolean isValid(){
        return !hasError;
    }


    /***
     * get the error resulting from the calculator's input.
     * @return the error. may be null if there is no error.
     */
    public CalculatorError getError() {
        return error;
    }


    /***
     * get the result of the calculators input.
     * @return the result. may be null if there is an error.
     */
    public CalculatorResult getResult() {
        return result;
    }

    /***
     * get the PARSED input that was created from the provided string.
     * @return the input object
     */
    public CalculatorInput getInput(){
        return input;
    }
}
