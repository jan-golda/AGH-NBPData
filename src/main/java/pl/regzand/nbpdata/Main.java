package pl.regzand.nbpdata;

import pl.regzand.nbpdata.commands.NBPDataCommand;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Starting point of the program.
 */
public class Main {

    /**
     * Starting point of the program.
     *
     * @param args program arguments
     */
    public static void main(String[] args) {

//        // FIXME: remove debug
//        args = new BufferedReader(new InputStreamReader(System.in)).readLine().split(" ");

        // run program
        new NBPDataCommand().execute(args);

    }

}
