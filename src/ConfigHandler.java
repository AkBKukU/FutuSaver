/**=============================================================*\
 *                                                              *
 *                     ConfigHandler Class                      *
 *                                                              *
 *                      by: Shelby Jueden                       *
 *                                                              *
 *    ______________________________________________________    *
 *   |                    ConfigHandler                     |   *
 *   |------------------------------------------------------|   *
 *   | - configFile:                        File            |   *
 *   | - loadFile:                          Scanner         |   *
 *   | - filePath:                          String          |   *
 *   | - rawDump:                           String[]        |   *
 *   |------------------------------------------------------|   *
 *   | + ConfigHandler(file:String)                         |   *
 *   |                                                      |   * 
 *   | + getValueFor(needle:String):        String          |   *
 *   | + getArray()                                         |   *
 *   | + countLines(file:String):           int             |   *
 *   |                                                      |   * 
 *   |______________________________________________________|   *
 *                                                              *
 *                                                              *
\*==============================================================*/


/**
 * Imports
 */
import java.util.Scanner;
import java.io.*;



public class ConfigHandler {
    
    File configFile;
    Scanner loadFile;
    
    String filePath;
    
    String[] rawDump;
    
    
    /**
     * Primary Constructor
     * @param file
     * @throws IOException
     */
    public ConfigHandler(String file) throws IOException{
        
        this.filePath = file;
        
        getArray(filePath);
        
    }
    
    /**
     * Returns a string value for the give entry in the file as a string
     * @param needle
     * @return
     */
    public String getValueFor(String needle){
        
        String value = null;
        
        for(int c = 0;c < rawDump.length;c++){
            
            if(needle.regionMatches(0, rawDump[c].trim() ,0, needle.length())){
                
                String splitter[] = rawDump[c].split(":");
                value = splitter[1].trim();
            }
            
        }
        
        return value.toUpperCase();
    }

    
    /**
     * Load file into an array
     * @param file
     * @throws IOException
     */
    private void getArray(String file) throws IOException{

        this.configFile = new File(file);
                
        loadFile = new Scanner(configFile);

        int fileLength = countLines(file);
        
        this.rawDump = new String[fileLength];
        
        for(int c=0;loadFile.hasNext();c++){

            this.rawDump[c] = loadFile.nextLine();
            
        }
        loadFile.close();
        
    }

    /**File Line Counter**/
    public int countLines(String path) throws IOException{
        
        int output = 0;
        
        //--Open file.
        File file = new File(path);
        Scanner inputFile = new Scanner(file);
        
        while (inputFile.hasNext()){
        
            String noWhere = inputFile.nextLine();
            output++;
        }

        inputFile.close();
        
        return output;
    }

    public boolean setValueFor(String type){
        
        return true;
    }
}
