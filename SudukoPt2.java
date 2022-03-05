/* 
Mason Hill
Assignment 2
CMPT375
March 1st, 2022




*/


import java.lang.*;

public class SudukoPt2 {

    public static void main(String[] args) {

        /*
         * Making a 3d array. Argument 1 is the x axis, argument 2 is the y axis
         * (Both of these are the sudoku board axis btw)
         * Argument 3 represents the valid numbers that can be put on the board
         * So the way this works is that if we go to 3,7,5, then we will be going to the
         * square
         * at 3,7, then look if 5 is a valid number
         */
        boolean array[][][] = new boolean[9][9][9];

        // Setting a char to zero
        char zero = '0';

        // A counter that is used to scan each input spot.
        // It is initialized here so that it doesn't get reset each time in the for loop
        int counter = 0;

        try {
            // Setting all the squares and possible numbers in those squares to true
            for (int i = 0; i < 9; i++) {
                for (int v = 0; v < 9; v++) {
                    for (int w = 0; w < 9; w++) {
                        // Where they get set to true
                        array[i][v][w] = true;
                    }
                }
            }

            // System.out.println("Before the for loop");
            // This for loop will move the rows down

            // Row incrementor
            for (int i = 0; i < 9; i++) {

                // System.out.println("In the for loop");

                // This loop will move the column to the right
                for (int j = 0; j < 9; j++) {

                    // System.out.println("Even deeper in the for loop");
                    // System.out.println(j);

                    // Putting each input digit into a temporary variable called holder
                    // The counter is used here so that we can get through all 81 values
                    int holder = Integer.parseInt("" + args[0].charAt(counter));
                    /// Increment the counter variable
                    counter++;

                    // System.out.println(holder);

                    // Checking that the holder actually has a value in it besides zero
                    // Since that would mean that the value needs to go in the board.
                    if (holder != 0) {

                        // Making the value into one that is based
                        // on a 0-8 array since input can only be 1-9
                        holder = holder - 1;

                        // System.out.println("Made it into the if statement");

                        // System.out.println("After setting to true");

                        // This loop sets all the other values to false
                        // Meaning that the only true value is the input value
                        for (int w = 0; w < 9; w++) {
                            array[i][j][w] = false;
                        }
                        // Looping through the columns
                        for (int w = 0; w < 9; w++) {
                            array[i][w][holder] = false;
                        }
                        // Looping through the rows
                        for (int v = 0; v < 9; v++) {
                            array[v][j][holder] = false;
                        }

                        int blockRow = (i / 3) * 3;
                        int blockColumn = (j / 3) * 3;

                        for (int v = 0; v < 3; v++) {
                            for (int u = 0; u < 3; u++) {
                                array[blockRow + v][blockColumn + u][holder] = false;
                            }
                        }

                        array[i][j][holder] = true;

                    }
                    singleNumberSelectorEarly(array);

                }
            }

           
            boolean temporaryArray[][][] = array;
            
            //Checking the array constraints until they return true aka changes have been made to the array
            while (modifiedConstraintSetter(temporaryArray) != true);

            // WHERE THE ALGORITHM GETS CALLED
            //We search the temporary array and set it to our current array
            temporaryArray = search(temporaryArray);
            //If the temp array has something in it, then we print it
            if (temporaryArray != null) {
                printer(temporaryArray);
                sudukoPrinter(temporaryArray);
                System.out.println("Made it");
            } else {
                System.out.println("Made it outside but false");

                // printer(temporaryArray);
            }
            // printer(temporaryArray);
            // sudukoPrinter(temporaryArray);

        } catch (java.lang.ArrayIndexOutOfBoundsException exception) {
            System.out.println("The try catch broke");
        }

        /**
         * THIS IS WHERE THE INPUT GETTING AND FORMATTING ENDS.
         * 
         * 
         */
    }

    static boolean[][][] search(boolean array[][][]) {

        // Stopping condition
        // Initalizing the row and column values to -1
        // If there is no unsolved column or row, then we have solved the suduko
        int unsolvedRow = -1;
        int unsolvedCol = -1;
        // This counts the number of solved squares.
        int solvedCount = 0;

        // Looping thorugh all rows
        for (int i = 0; i < 9; i++) {
            // Looping through all columns
            for (int j = 0; j < 9; j++) {
                // Setting the count to 0
                int count = 0;

                // Seeing how many possible values there are by looping through our values on
                // each square
                for (int k = 0; k < 9; k++) {
                    // If this is true then that means that k is a valid number
                    if (array[i][j][k] == true) {
                        // Incrementing the counter. If the counter is 1 then that means there is one
                        // possible value
                        // If it is 7 then there is seven possible values.
                        count++;
                    }
                }
                // If there is no valid numbers then we need to backtrack
                if (count == 0) {
                    return null;

                }
                // This means that we have solved this square.
                else if (count == 1) {
                    solvedCount++;
                }
                // If the count is more than one, then that means that this is an unsolved
                // square so we capture it
                else {
                    unsolvedRow = i;
                    unsolvedCol = j;
                }
            }
        }
        // This is the win condition. If this triggers then we have solved suduko
        if (solvedCount == 81) {
            // Returning our solved suduko array for printing purposes
            return array;
        }

        // Recursion
        for (int i = 0; i < 9; i++) {
           
            //Seeing if the array at the unsolved position has a possible value
            if (array[unsolvedRow][unsolvedCol][i]) {
                
                //Making a new 3d array
                boolean[][][] deepCopy = new boolean[9][9][9];
                
                //Cloning all the positions from our original array into a temporary one
                for (int j = 0; j < 9; j++) {
                    for (int k = 0; k < 9; k++) {
                        deepCopy[j][k] = array[j][k].clone();
                    }
                }
                //Setting all the values in the temporary array at the unsolved position to false except for the one we are choosing.
                for (int j = 0; j < 9; j++) {
                    if (i != j) {
                        deepCopy[unsolvedRow][unsolvedCol][j] = false;
                    }
                }

                //Setting all the constraints for our temporary array based on the value we selected in the unsolved spot.
                modifiedConstraintSetter(deepCopy);
                //Storing the result of our search of our temporary array into another temporary array. This is where the recursion happens
                boolean[][][] result = search(deepCopy);
                //If our temporary array search doesnt return null then that means that we can return our resulting array
                if (result != null) {
                    return result;
                }
            }
        }
        return null;

    }

    // The whole purpose of this method is to do the constraint consistency once a
    // value has been selected.
    static boolean modifiedConstraintSetter(boolean array[][][]) {
        //A variable flag to indicate if changes have been made to the array
        boolean noChanges = true;
        
        //Looping through all spots on the board and all the possible values
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                for (int z = 0; z < 9; z++) {

                    //Here we are checking for if at any given spot their is 2 or more possible choices.
                    int counter = 0;
                    for (int i = 0; i < 9; i++) {
                        if ((array[x][y][i]) == true) {
                            counter++;
                        }
                    }


                    // Checking if any of the values in the current spot are eligible and if there is only one possible number
                    if (array[x][y][z] == true && counter == 1) {
                        //If we are in here that means the we are looking at an eligible value and it is the only eligible value for that spot.
                        
                        //Setting all the row and column squares that have the same eligible value to false
                        for (int w = 0; w < 9; w++) {
                            if (w != x && array[w][y][z] == true) {
                                noChanges = false;
                                array[w][y][z] = false;
                            }
                            if (w != y && array[x][w][z] == true) {
                                noChanges = false;
                                array[x][w][z] = false;
                            }

                        }

                        
                        
                        //This is done to make it possible to check all 9 boxes
                        int blockRow = (x / 3) * 3;
                        int blockColumn = (y / 3) * 3;
                        
                        //Looping through all the squares in a 3x3 box
                        for (int v = 0; v < 3; v++) {
                            for (int u = 0; u < 3; u++) {

                                //Setting the value in all possible squares that have the same value in the 3x3 grid that this square resides in to false
                                if (blockRow + v != x && blockColumn + u != y && array[blockRow + v][blockColumn + u][z] == true) {
                                    noChanges = false;
                                    array[blockRow + v][blockColumn + u][z] = false;
                                }

                            }
                        }
                    

                    }

                }
            }
        }
        

       //Return a boolean that indicates whether changes have been made or not
        return noChanges;
    }

    static void printer(boolean array[][][]) {
        for (int t = 0; t < 9; t++) {
            for (int y = 0; y < 9; y++) {
                String printer = "";
                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }

                System.out.println("S[" + t + "][" + y + "] {" + printer.trim() + "}");
            }
        }

    }

    /*
     * THIS STATEMENT WILL PRINT THE SUDUKO BOARD. I KNOW IT COULD BE MORE OPTIMIZED
     * BUT IM MORE CONCERNED ABOUT THE AI WORKING
     */
    static void sudukoPrinter(boolean array[][][]) {
        for (int t = 0; t < 3; t++) {
            String printer = "";
            printer += "|";
            for (int y = 0; y < 3; y++) {

                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }

            }
            printer += "|";
            for (int y = 3; y < 6; y++) {

                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }

            }
            printer += "|";
            for (int y = 6; y < 9; y++) {

                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }

            }
            printer += "|";
            System.out.println(printer);

        }
        System.out.println("-------------");
        for (int t = 3; t < 6; t++) {
            String printer = "";
            printer += "|";
            for (int y = 0; y < 3; y++) {

                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }

            }
            printer += "|";
            for (int y = 3; y < 6; y++) {

                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }

            }
            printer += "|";
            for (int y = 6; y < 9; y++) {

                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }

            }
            printer += "|";
            System.out.println(printer);
        }
        System.out.println("-------------");
        for (int t = 6; t < 9; t++) {
            String printer = "";
            printer += "|";
            for (int y = 0; y < 3; y++) {

                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }

            }
            printer += "|";
            for (int y = 3; y < 6; y++) {

                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }
            }
            printer += "|";
            for (int y = 6; y < 9; y++) {

                for (int u = 0; u < 9; u++) {

                    if (array[t][y][u] != false) {

                        printer += (u + 1);
                    }

                }
            }
            printer += "|";
            System.out.println(printer);

        }

    }

    static void singleNumberSelectorEarly(boolean array[][][]) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // To return to: I need to count if there is only one number on a spot. If there
                // is, then I need to call the constraint propagation on it

                int counter = 0;
                for (int w = 0; w < 9; w++) {
                    if (array[i][j][w] == true) {
                        counter++;
                    }
                }
                // If we have a spot that only has 1 possible input, call the constraint setter
                // as that number has to be the input
                if (counter == 1) {
                    for (int w = 0; w < 9; w++) {

                        if (array[i][j][w] == true) {

                            singleNumberSelectorPropagationEarly(array, i, j, w);

                        }
                    }
                }
            }
        }

        // System.out.println("Made it to the very end of the single number selector");

    }

    static void singleNumberSelectorPropagationEarly(boolean array[][][], int i, int j, int k) {

        // If that value is in the row, this input cannot be used
        for (int w = 0; w < 9; w++) {
            array[w][j][k] = false;

        }

        // If that value isn't in the column: set all the values of that number in the
        // row to false
        for (int w = 0; w < 9; w++) {
            array[i][w][k] = false;

        }

        int blockRow = (i / 3) * 3;
        int blockColumn = (j / 3) * 3;
        // Set all the values in the 3x3 grid to false
        for (int v = 0; v < 3; v++) {
            for (int u = 0; u < 3; u++) {
                array[blockRow + v][blockColumn + u][k] = false;

            }
        }

        // System.out.println("Returning from singleNumberSelectorPropagation but its
        // the early one");
        // It will return true if their is no single number in its domain that has to be
        // chosen.
        array[i][j][k] = true;

    }

}

