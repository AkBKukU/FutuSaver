

import java.io.IOException;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class LightBeam {

    
    Random randNumGen = new Random();
    
    private Image lightImage = null;
    
    private float posX;
    private float posY;
    private float beamHeight = 0  ;
    private float maxBeamHeight = (float) Math.sqrt( Math.pow(ScreenSaver.SCREENY,2) + Math.pow(ScreenSaver.SCREENX/2,2) );
    int maxLightSpeed = 1;
    int maxLightWidth = 1;
    int minLightWidth = 0;
    ConfigHandler loadConfig ;
    
    //--Hold Light Info--|Angle|Current Rotate Speed|New Rotate Speed|Rotate Acceleration|Is new Width Higher?(1=yes)|Current Width|New Width|Width Acceleration|Is new speed Higher?(1=yes)
    float beamInfo[][];//{  0  ,         1          ,        2       ,         3         ,             4            ,      5      ,    6    ,         7        ,            8              }
    float tempAngleStorage[];
    boolean beamOutDone = false;
    boolean beamUpDone = false;
    
    /**
     * Primary Constructor
     * @param numBeams
     * @param type
     */
    public LightBeam(int numBeams, String type){
        
        try {
            lightImage = new Image(type);
            
            lightImage.setCenterOfRotation((lightImage.getWidth() / 2),
                                            lightImage.getHeight());
        } catch (SlickException e) {
             //TODO Auto-generated catch block
            e.printStackTrace();
        }
        //--Image is drawn from top left corner, so must subtract image values
        posX = (ScreenSaver.SCREENX / 2);
        posY = ScreenSaver.SCREENY - (float) Math.sqrt(Math.pow(ScreenSaver.SCREENY,2) + Math.pow(ScreenSaver.SCREENX/2,2) );

        try {
            String stDir = System.getProperty("user.dir");
            loadConfig = new ConfigHandler(stDir + "/settings.cfg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        maxLightSpeed = Integer.parseInt(loadConfig.getValueFor("light-beam-max-speed"));
        maxLightWidth = Integer.parseInt(loadConfig.getValueFor("light-beam-max-width"));
        minLightWidth = Integer.parseInt(loadConfig.getValueFor("light-beam-min-width"));
        
        beamInfo = new float[numBeams][9];
        tempAngleStorage = new float[numBeams];
        //--Assign Random angles and velocity
        for(int c = 0;c < beamInfo.length;c++){
            //--Set Angles
            beamInfo[c][0] = 90f;
            tempAngleStorage[c] = randNumGen.nextInt(180);
            
            //--Set Rotation Speed
            beamInfo[c][1] = 0;
            //--Set Acceleration
            beamInfo[c][3] =(randNumGen.nextInt(maxLightSpeed) * 0.00001f);

            //--Set Width
            beamInfo[c][5] = 20;
            
        }
        
    }
    
    /**
     * Advances the rotation of the beams
     */
    public void calcRotation() {
        
        for(int c = 0;c < beamInfo.length;c++){
            beamInfo[c][0] += beamInfo[c][1];

            //--Checks if a beam has made it all the way down yet
            if(!(beamUpDone)){
                if(beamHeight >= maxBeamHeight ){
                    beamUpDone = true;
                    beamHeight = maxBeamHeight ;

                    int posNeg = 1;
                    for(int d = 0;d < beamInfo.length;d++){
                        //--Set Rotation Speed
                        if((d%2)==0){posNeg = 1;}else{posNeg = -1;}
                        beamInfo[d][1] = posNeg * (randNumGen.nextInt(maxLightSpeed) * 0.001f);
                        //--Set New Rotation Speed
                        if((d%2)==0){posNeg = 1;}else{posNeg = -1;}
                        beamInfo[d][2] = posNeg * (randNumGen.nextInt(maxLightSpeed) * 0.001f);
                        //--Set if new speed is higher
                        if(beamInfo[d][2] > beamInfo[d][1]){
                            beamInfo[d][8] = 1;
                        }else{
                            beamInfo[d][8] = 0;
                        }
                        //--Set New Width
                        beamInfo[d][6] = randNumGen.nextInt(maxLightWidth - minLightWidth) + minLightWidth;
                        //--Set if new width is higher
                        if(beamInfo[d][6] > beamInfo[d][5]){
                            beamInfo[d][4] = 1;
                        }else{
                            beamInfo[d][4] = 0;
                        }
                        //--Set New Width Acceleration
                        beamInfo[d][7] = (randNumGen.nextInt(10000) * 0.001f);
                    }
                }else{
                    beamHeight += 1f; 
                }
            }
            //--Checks if a beam has made it all the way down yet
            if(!(beamOutDone) && beamUpDone){
                if(Math.abs(beamInfo[c][0]) > 190 ){
                    beamOutDone = true;
                    
                }
            }

            
            

            //--Checks if a beam has made it to it's new rotation speed
            if(1 == beamInfo[c][8] && beamInfo[c][1] >= beamInfo[c][2]){

                //--Set New Rotation Speed
                beamInfo[c][2] =  (randNumGen.nextInt(maxLightSpeed) * 0.001f) - (maxLightSpeed/2) * 0.001f  ;

                //--Set if new speed is higher
                if(beamInfo[c][2] > beamInfo[c][1]){
                    beamInfo[c][8] = 1;
                }else{
                    beamInfo[c][8] = 0;
                }
                //--Set Acceleration
                beamInfo[c][3] = (randNumGen.nextInt(maxLightSpeed) * 0.01f);
                
            }else if(1 == beamInfo[c][8] && !(beamInfo[c][1] >= beamInfo[c][2])){
                
                beamInfo[c][1] += beamInfo[c][3]/1000; 
                
                
            }else if(0 == beamInfo[c][8] && beamInfo[c][1] <= beamInfo[c][2]){

                //--Set New Rotation Speed
                beamInfo[c][2] =  (randNumGen.nextInt(maxLightSpeed) * 0.001f) - (maxLightSpeed/2) * 0.001f ;

                //--Set if new speed is higher
                if(beamInfo[c][2] > beamInfo[c][1]){
                    beamInfo[c][8] = 1;
                }else{
                    beamInfo[c][8] = 0;
                }
                //--Set Acceleration
                beamInfo[c][3] = (randNumGen.nextInt(maxLightSpeed) * 0.01f);
                
            }else if(0 == beamInfo[c][8] && !(beamInfo[c][1] <= beamInfo[c][2])){
                
                beamInfo[c][1] -= beamInfo[c][3]/1000; 
            }

            
            
            if (beamInfo[c][0] > 190f) {
                beamInfo[c][0] =   -10f;
                
            } else if (beamInfo[c][0] < -10f) {
                beamInfo[c][0] = 190f;
                
            }
            
        }
        
        //System.out.println("Angle: " + beamHeight);
        
    }
    
    
    
    
    /**
     * Adjusts the width of the beams
     */
    public void calcWidth() {
        
        for(int c = 0;c < beamInfo.length;c++){
            
            

            //--Checks if a beam has made it to it's new width
            if(1 == beamInfo[c][4] && beamInfo[c][5] > beamInfo[c][6]){
                
                //--Set New Width
                beamInfo[c][6] = randNumGen.nextInt(maxLightWidth - minLightWidth) + minLightWidth;
                //--Set if new width is higher
                if(beamInfo[c][6] > beamInfo[c][5]){
                    beamInfo[c][4] = 1;
                }else{
                    beamInfo[c][4] = 0;
                }
                //--Set New Width Acceleration
                beamInfo[c][7] = (randNumGen.nextInt(1000) * 0.001f);
                
            }else if(1 == beamInfo[c][4] && !(beamInfo[c][5] > beamInfo[c][6])){
                beamInfo[c][5] += beamInfo[c][7]; 
            }else if(0 == beamInfo[c][4] && beamInfo[c][5] < beamInfo[c][6]){
                
                //--Set New Width
                beamInfo[c][6] = randNumGen.nextInt(maxLightWidth - minLightWidth) + minLightWidth;
                //--Set if new width is higher
                if(beamInfo[c][6] > beamInfo[c][5]){
                    beamInfo[c][4] = 1;
                }else{
                    beamInfo[c][4] = 0;
                }
                //--Set New Width Acceleration
                beamInfo[c][7] = (randNumGen.nextInt(1000) * 0.001f);
                
            }else if(0 == beamInfo[c][4] && !(beamInfo[c][5] < beamInfo[c][6])){
                beamInfo[c][5] -= beamInfo[c][7]; 
            }
            
        }
        
        //System.out.println("Angle: " + beamHeight);
        
    }
    
    
    
    
    /**
     * Loops through the array values and draws the image using them
     */
    public void drawAll() {

        for(int c = 0; c < beamInfo.length; c++) {
            
            lightImage.rotate( -(lightImage.getRotation()) 
                                + beamInfo[c][0] - 90);
            
            float trans = (float) (1f - Math.pow(beamInfo[c][5] / maxLightWidth,2));
            
            lightImage.draw(posX - beamInfo[c][5] /2 ,
                            posY + maxBeamHeight - beamHeight,
                            beamInfo[c][5], beamHeight,
                            new Color(1f,1f,1f, trans )
            );
            
        }
    }
   
    
    
    /**
     * Debug Methods
     */
    
    //--Dumps the angles of all existing light beams to console.
    public void dumpAngles(){
        
        for(int c = 0;c < beamInfo.length;c++){
            System.out.println( beamInfo[c][0]-90);
        }
    }
    
    
    
    
    
}
