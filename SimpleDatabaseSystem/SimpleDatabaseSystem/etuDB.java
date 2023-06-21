import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class etuDB{
    //CLASS VARIABLES
    public SinglyLinkedList<Table> listOfTables;
    //CONSTRUCTORS
    public etuDB(){
        listOfTables = new SinglyLinkedList<>();
    }
    //METHODS
        //MAIN METHODS
    /**This function creates a new table and keep reference in the singly linked list of the class.
     * To investigate tokensOfCommand, follow parameterParser() method.
     * @param command
     */
    public void createTable(String command){
        boolean isExist = false;
        String[] tokensOfCommand;
        //Investigating and handling command
        try {
            tokensOfCommand = parameterParser(command, 0);
        }
        catch (WrongFileTypeException error){
            System.out.println(error.getMessage());
            return;
        }
        catch(UnrecognizableCommand error){
            System.out.println(error.getMessage());
            return;
        }
        //If everything is okay this block of code process necessarily things.
        if(tokensOfCommand.length == 6 && tokensOfCommand[5].equals("true")){
            try {
                Table newTable = new Table(tokensOfCommand[3], tokensOfCommand[4]);
                //Iterating linked list in purpose to find already existing headers to overwrite.
                SinglyLinkedList<Table>.Node iteratorTable = listOfTables.headNode;
                while(iteratorTable != null){
                    if(iteratorTable.element.header.equals(tokensOfCommand[4])){
                        iteratorTable.element = newTable;
                        isExist = true;
                        break;
                    }
                    iteratorTable = iteratorTable.nextNode;
                }
                //It is if table is not exist in the linked list.
                if(!isExist){
                    listOfTables.add(newTable);
                }

            }
            catch (FileNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Unexpected error from unknown source! Table is not created.");
        }
    }
    /**Listing columns from tables.
     * Expected input: SELECT columNamesCSV FROM tableName
     * Expected input: SELECT * FROM tableName
     * Expected input: SELECT columNamesCSV FROM tableName WHERE columnName=someValue
     * Expected input: SELECT * FROM tableName WHERE columnName=someValue
     * @param query
     * @return String array of columns listed or null if error occurs
     */
    public String[] query(String query){
        boolean tableFound = false;
        String[] tokensOfCommand;
        try {
            tokensOfCommand = parameterParser(query, 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        if(tokensOfCommand[tokensOfCommand.length - 1].equals("true")){
            SinglyLinkedList<Table>.Node currentTable = listOfTables.headNode;
            //Iterating table list to find given table
            while(currentTable != null){
                //After finding table other elements will be checked and string will be formed.
                if(currentTable.element.header.equals(tokensOfCommand[3])){
                    tableFound = true;
                    //Preparing for return value.
                    String[] finalStringArray;
                    int finalStringArrayIndex = 0;
                    //Creating a temp row to iterato through table.
                    SinglyLinkedList<Row>.Node currentRow = currentTable.element.rowsInTable.headNode;
                    //Taking column names from the table.
                    String columnString = getSchema(tokensOfCommand[3]);
                    String[] columnsOfTables = columnString.split(",");
                    //Modes of SELECT command.
                        //Command *: Listing all columns.
                    if(tokensOfCommand[1].equals("*")){
                        finalStringArray = new String[columnsOfTables.length * (currentTable.element.numberOfRows + 1)];
                        //WHERE token did not used
                        if(tokensOfCommand[tokensOfCommand.length - 2].equals("conditionOff")){
                            for(int indexOfColumnsInTable = 0, iterateNumber = currentRow.element.elementsOfRow.length; indexOfColumnsInTable < iterateNumber; indexOfColumnsInTable++){
                                currentRow = currentTable.element.rowsInTable.headNode;
                                while(currentRow != null){
                                    finalStringArray[finalStringArrayIndex] = currentRow.element.elementsOfRow[indexOfColumnsInTable];
                                    finalStringArrayIndex++;
                                    currentRow = currentRow.nextNode;
                                }
                                finalStringArray[finalStringArrayIndex] = "-----------";
                                finalStringArrayIndex++;
                            }
                        }
                        //WHERE token has used.
                        else{
                            String[] conditionTokens = tokensOfCommand[5].split("=");
                            //If WHERE commnad demand search in first column.
                            if(conditionTokens[0].equals(currentRow.element.elementsOfRow[0])){
                                BinarySearchTree.Node matchedNode = currentTable.element.indexedFirstColumn.findingRows(conditionTokens[1]);
                                BinarySearchTree.Node duplicateMatchedNode;
                                for(int indexOfColumnsInTable = 0, iterateNumber = currentRow.element.elementsOfRow.length; indexOfColumnsInTable < iterateNumber; indexOfColumnsInTable++){
                                    finalStringArray[finalStringArrayIndex] = currentRow.element.elementsOfRow[indexOfColumnsInTable];
                                    duplicateMatchedNode = matchedNode;
                                    //Creating iterator BST node.
                                    finalStringArray[finalStringArrayIndex] = currentRow.element.elementsOfRow[indexOfColumnsInTable];
                                    finalStringArrayIndex++;
                                    while(duplicateMatchedNode != null){
                                        finalStringArray[finalStringArrayIndex] = matchedNode.element.elementsOfRow[indexOfColumnsInTable];
                                        finalStringArrayIndex++;
                                        duplicateMatchedNode = duplicateMatchedNode.nextNodeThroughDepth;
                                    }
                                    finalStringArray[finalStringArrayIndex] = "-----------";
                                    finalStringArrayIndex++;
                                }
                            }
                            //If WHERE command demands other columns as indicator.
                            else{
                                String[] tempColumnNames = getSchema(currentTable.element.header).split(",");
                                int conditionColumnIndex;
                                for(conditionColumnIndex = 0; conditionColumnIndex < tempColumnNames.length; conditionColumnIndex++){
                                    if(conditionTokens[0].equals(tempColumnNames[conditionColumnIndex])){
                                        break;
                                    }
                                }
                                for(int indexOfColumnsInTable = 0, iterateNumber = currentRow.element.elementsOfRow.length; indexOfColumnsInTable < iterateNumber; indexOfColumnsInTable++){
                                    currentRow = currentTable.element.rowsInTable.headNode;
                                    finalStringArray[finalStringArrayIndex] = currentRow.element.elementsOfRow[indexOfColumnsInTable];
                                    currentRow = currentRow.nextNode;
                                    while(currentRow != null){
                                        if(conditionTokens[1].equals(currentRow.element.elementsOfRow[conditionColumnIndex])){
                                            finalStringArray[finalStringArrayIndex] = currentRow.element.elementsOfRow[indexOfColumnsInTable];
                                            finalStringArrayIndex++;
                                        }
                                        currentRow = currentRow.nextNode;
                                    }
                                    finalStringArray[finalStringArrayIndex] = "-----------";
                                    finalStringArrayIndex++;
                                }
                            }
                        }//End of else block for WHERE command in mode *.
                    }
                        //Command someColumnsCSV: Listing some columns
                    else{
                        int numberOfColumnsMatched = 0;
                        //Forming an array for column names that wanted from user
                        String[] wantedColumns = tokensOfCommand[1].split(",");
                        //Forming final array
                        finalStringArray = new String[wantedColumns.length * (currentTable.element.numberOfRows + 2)];
                        //WHERE command did not used.
                        if(tokensOfCommand[tokensOfCommand.length - 2].equals("conditionOff")){
                            //Iterating through wantedColumns.
                            for(int outerIndex = 0; outerIndex < wantedColumns.length; outerIndex++){
                                //Iterating through columns of table.
                                for(int innerIndex = 0; innerIndex < columnsOfTables.length; innerIndex++){
                                    if(wantedColumns[outerIndex].equals(columnsOfTables[innerIndex])){
                                        currentRow = currentTable.element.rowsInTable.headNode;
                                        //Adding column to array.
                                        while(currentRow != null){
                                            finalStringArray[finalStringArrayIndex] = currentRow.element.elementsOfRow[innerIndex];
                                            finalStringArrayIndex++;
                                            currentRow = currentRow.nextNode;
                                        }
                                        finalStringArray[finalStringArrayIndex] = "--------------";
                                        finalStringArrayIndex++;
                                        numberOfColumnsMatched++;
                                        break;
                                    }
                                }
                            }
                        }
                        //WHERE command is used.
                        else{
                            String[] conditionTokens = tokensOfCommand[5].split("=");
                            String[] tempColumnNames = getSchema(currentTable.element.header).split(",");
                            int conditionColumnIndex;
                            for(conditionColumnIndex = 0; conditionColumnIndex < tempColumnNames.length; conditionColumnIndex++){
                                if(conditionTokens[0].equals(tempColumnNames[conditionColumnIndex])){
                                    break;
                                }
                            }
                            //Iterating through wantedColumns.
                            for(int outerIndex = 0; outerIndex < wantedColumns.length; outerIndex++){
                                //Iterating through columns of table.
                                for(int innerIndex = 0; innerIndex < columnsOfTables.length; innerIndex++){
                                    if(wantedColumns[outerIndex].equals(columnsOfTables[innerIndex])){
                                        currentRow = currentTable.element.rowsInTable.headNode;
                                        finalStringArray[finalStringArrayIndex] = currentRow.element.elementsOfRow[innerIndex];
                                        finalStringArrayIndex++;
                                        //Searching BST.
                                        if(conditionTokens[0].equals(columnsOfTables[0])){
                                            //Creating iterator BST node.
                                            BinarySearchTree.Node matchedNode = currentTable.element.indexedFirstColumn.findingRows(conditionTokens[1]);
                                            while(matchedNode != null){
                                                finalStringArray[finalStringArrayIndex] = matchedNode.element.elementsOfRow[innerIndex];
                                                finalStringArrayIndex++;
                                                matchedNode = matchedNode.nextNodeThroughDepth;
                                            }
                                        }
                                        //Searching SLL.
                                        else{
                                            currentRow = currentRow.nextNode;
                                            //Adding column to array.
                                            while(currentRow != null){
                                                if(conditionTokens[1].equals(currentRow.element.elementsOfRow[conditionColumnIndex])){
                                                    finalStringArray[finalStringArrayIndex] = currentRow.element.elementsOfRow[innerIndex];
                                                    finalStringArrayIndex++;
                                                }
                                                currentRow = currentRow.nextNode;
                                            }
                                        }
                                        finalStringArray[finalStringArrayIndex] = "--------------";
                                        finalStringArrayIndex++;
                                        numberOfColumnsMatched++;
                                        break;
                                    }
                                }
                            }
                        }
                        //If all columns is not presented this if will write a error message and return null.
                        if(numberOfColumnsMatched != wantedColumns.length){
                            System.out.println("Some columns did not match with given table's columns.");
                            return null;
                        }
                    }
                    if(tokensOfCommand[tokensOfCommand.length - 2].equals("conditionOn")){
                        String[] temp = new String[finalStringArrayIndex];
                        for(int index = 0; index < finalStringArrayIndex; index++){
                            temp[index] = finalStringArray[index];
                        }
                        finalStringArray = temp;
                    }
                    return finalStringArray;
                }//End of table found in table list if.
                currentTable = currentTable.nextNode;
            }//End of table list iterator while.
            //Table not found error message.
            if(!tableFound){
                System.out.println("Table could not found.");
                return null;
            }
        }//End of if true part.
        System.out.println(tokensOfCommand[tokensOfCommand.length - 1]);
        return null;
    }//End of query.
    /**Parsing parameters of the createTable() and query() methods. Mode 0 is for createTable, mode 1 is for query().
     * @param command
     * @param mode
     * @return
     * @throws WrongFileTypeException
     * @throws UnrecognizableCommand
     */
    public String[] parameterParser(String command, int mode) throws WrongFileTypeException, UnrecognizableCommand{
        //To make the tokensOfCommand longer, I tokenize the String command and then applies tokens to the array. Array tokensOfCommand's last indexes holds the feedback of parser.
        StringTokenizer createdTokens = new StringTokenizer(command, " ");
        String[] tokensOfCommand = new String[createdTokens.countTokens() + 2];
        for(int index = 0; 0 != createdTokens.countTokens(); index++){
            tokensOfCommand[index] = createdTokens.nextToken();
        }
        //Creating proper feedback.
        switch(mode){
            //createTable() method.
            case 0:
                //3rd index is path, 4th is header,5th is check feedback
                if(tokensOfCommand.length > 5 && tokensOfCommand[0].equals("CREATE") && tokensOfCommand[1].equals("TABLE") && tokensOfCommand[2].equals("FROM")){
                    String[] pathParsing = tokensOfCommand[3].split(File.separator);                        //Parsing path
                    tokensOfCommand[4] = pathParsing[pathParsing.length - 1];                               //Taking name of table from path's last index
                    tokensOfCommand[4] = tokensOfCommand[4].substring(0, tokensOfCommand[4].length() - 4);  //Get rid of .csv at the end
                    tokensOfCommand[5] = "true";
                    if(!(pathParsing[pathParsing.length - 1].endsWith(".csv"))){
                        tokensOfCommand[5] = "Wrong file format!";
                        throw new WrongFileTypeException(tokensOfCommand[5]);
                    }
                }
                else{
                    if(tokensOfCommand.length == 5){
                        tokensOfCommand[5] = "UnrecognizableCommand";
                        throw new UnrecognizableCommand(tokensOfCommand[5]);
                    }
                    else{
                        throw new UnrecognizableCommand("Create, unknown length.");
                    }
                }
                break;
            //query() method.
            //Last index is feedback of the function. It is 7 or 5. true or false.
            //Second last index is information about condition check. It is 6 or 4. conditionOn or conditionOff.
            case 1:
                if(tokensOfCommand.length > 4 && tokensOfCommand[0].equals("SELECT") && tokensOfCommand[2].equals("FROM")){
                    tokensOfCommand[tokensOfCommand.length - 1] = "true";
                    if(tokensOfCommand.length == 8){
                        if(tokensOfCommand[4].equals("WHERE") && (2 == tokensOfCommand[5].split("=").length)){
                            tokensOfCommand[6] = "conditionOn";
                        }
                        else{
                            tokensOfCommand[7] = "false";
                            throw new UnrecognizableCommand("Condition syntax problem.");
                        }
                    }
                    else{
                        tokensOfCommand[tokensOfCommand.length - 2] = "conditionOff";
                    }
                }
                else{
                    if(tokensOfCommand.length == 5){
                        tokensOfCommand[5] = "UnrecognizableCommand";
                        throw new UnrecognizableCommand(tokensOfCommand[5]);
                    }
                    else{
                        throw new UnrecognizableCommand("Create, unknown length.");
                    }
                }
                break;
            default:
                tokensOfCommand[tokensOfCommand.length - 1] = "Wrong mode!";
                throw new UnrecognizableCommand(tokensOfCommand[tokensOfCommand.length - 1]);
            }
        return tokensOfCommand;
    }
    /**Get first row of table and return it.
     * @param tableName
     * @return columnNames
     */
    public String printSchema(String tableName){
        SinglyLinkedList<Table>.Node tempTable = listOfTables.headNode;
        StringBuilder columnNamesString = new StringBuilder();
        while(tempTable != null){
            if(tempTable.element.header.equals(tableName)){
                String[] columnNames = tempTable.element.rowsInTable.headNode.element.elementsOfRow;
                for(int index = 0; index < columnNames.length; index++){
                    columnNamesString.append(columnNames[index] + ",");
                }
                break;
            }
            tempTable = tempTable.nextNode;
        }
        System.out.println(columnNamesString);
        return columnNamesString.toString();
    }
    /**This method waiting for input of user. Thanks to that program will work until exit will be written.
     */
    public void prompt(){
        System.out.println("To exit program write \"exit\" and press enter.");
        System.out.println("etuDB>>");
        String inputLine;
        String[] inputTokens;
        Scanner inputTaker = new Scanner(System.in);
        while(!((inputLine = inputTaker.nextLine()).equals("exit"))){
            inputTokens = inputLine.split(" ");
            if(inputTokens[0].equals("CREATE")){
                //Creating table
                createTable(inputLine);
                //Getting table name from inputLine
                String[] pathParsing = inputTokens[3].split(File.separator);                         //Parsing path
                String tableName = pathParsing[pathParsing.length - 1];                              //Taking name of table from path's last index
                tableName = tableName.substring(0, tableName.length() - 4);                          //Get rid of .csv at the end
                //Iterating tableList to find the table which is added.
                SinglyLinkedList<Table>.Node iteratorTable = listOfTables.headNode;
                while(iteratorTable != null){
                    if(iteratorTable.element.header.equals(tableName)){
                        iteratorTable.element.printTable();
                    }
                    iteratorTable = iteratorTable.nextNode;
                }
            }
            else if(inputTokens[0].equals("SELECT")){
                String[] queryOutput = query(inputLine);
                for(int index = 0; index < queryOutput.length; index++){
                    System.out.println(queryOutput[index]);
                }
            }
            else{
                System.out.println("Unexpected input.");
            }
        }
        inputTaker.close();
    }
        //SIDE METHODS
    /**Print all database.
     */
    public void printDB(){
        SinglyLinkedList<Table>.Node tempNode = listOfTables.headNode;
        while(tempNode != null){
            tempNode.element.printTable();
            tempNode = tempNode.nextNode;
        }
    }
    /**Same method with printSchema() but does not print column names.
     * @param tableName
     * @return
     */
    public String getSchema(String tableName){
        SinglyLinkedList<Table>.Node tempTable = listOfTables.headNode;
        StringBuilder columnNamesString = new StringBuilder();
        while(tempTable != null){
            if(tempTable.element.header.equals(tableName)){
                String[] columnNames = tempTable.element.rowsInTable.headNode.element.elementsOfRow;
                for(int index = 0; index < columnNames.length; index++){
                    columnNamesString.append(columnNames[index] + ",");
                }
                break;
            }
            tempTable = tempTable.nextNode;
        }
        return columnNamesString.toString();
    }
}