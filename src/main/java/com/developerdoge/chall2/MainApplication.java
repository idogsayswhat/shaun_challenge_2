package com.developerdoge.chall2;

import com.developerdoge.chall2.calculator.Calculator;
import io.javalin.Javalin;

/***
 * Main entry point to begin hosting the calculator on the default port(8080)
 */
public class MainApplication {

    public static void main(String[] args){

        Javalin app = Javalin.create().start(8080);

        //Create the required endpoint - POST /calculate
        app.post("/calculate",  (context)->{

            Calculator calculator = new Calculator(context.body());
            if(calculator.isValid()){
                context.status(200).json(calculator.getResult());
            }
            else{
                context.status(400).json(calculator.getError());
            }

        });

    }

}
