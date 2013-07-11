

    
import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class ScreenSaver extends BasicGame{


        //--Values to be replaced by settings.cfg
        LightBeam lightA = null;
        int lightACount = 50;
        public static int SCREENX = 1280;
        public static int SCREENY = 720;

        //--Static Images
        Image glow = null;
        float glowScale;
        float glowX;
        float glowY;
        Image title = null;
        float titleTrans = 0.000001f;
        float titleScale;
        float titleX;
        float titleY;
        
        boolean displayTitle = true;
        Color back = new Color(0f,.08f,.12f);
        ConfigHandler loadConfig ;
     
        
        public ScreenSaver()
        {
            //--Set Title of Window
            super("Futurama Screen Saver");
            
            try {
                String stDir = System.getProperty("user.dir");
                loadConfig = new ConfigHandler(stDir + "/settings.cfg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            loadValues(); //--the values from settings.cfg
        }
     
        @Override
        public void init(GameContainer gc) throws SlickException {
            
            //--Setup Glow Image Settings
            glow = new Image("skylight2.png");
            glowScale = (SCREENX / 1920f);
            glowX = (SCREENX / 2) - ((glow.getWidth() * glowScale) / 2);
            glowY = SCREENY-(glow.getWidth() * glowScale);

            //--Setup Title Image Settings
            title = new Image("title.png");
            titleScale = (SCREENX / 1920f) ;
            titleX = (SCREENX / 2) - ((title.getWidth()*titleScale) / 2);
            titleY = (SCREENY/2)-((title.getHeight()*titleScale)/2)  ;
            
            //--Create light beams from class
            lightA = new LightBeam(lightACount, "lightA.png");
            
            
        }
     
        @Override
        public void update(GameContainer gc, int delta)
                throws SlickException
        {
                
            //--Update the rotations of the light beams
            lightA.calcRotation();
            lightA.calcWidth();
                
        }
     
        public void render(GameContainer gc, Graphics g)
                throws SlickException
        {
            
            g.setBackground(back); //--Set Background Color
            
            lightA.drawAll(); //--Draw lights
            
            glow.draw(glowX, glowY, glowScale); //--Draw glow light to smooth out pivot point of lights
            
            if(titleTrans < 1){titleTrans = titleTrans * 1.08f ;} //--Fade in Title
            
            if(displayTitle){title.draw( titleX , titleY, titleScale, new Color(1f,1f,1f, titleTrans ));} //--Print title image
        }
     
        public static void main(String[] args)
                throws SlickException
        {
             AppGameContainer app =
                new AppGameContainer( new ScreenSaver() );
             app.setVSync(true);
     
             app.setDisplayMode(SCREENX, SCREENY, false);
             app.start();
        }
        
        
        
        /**
         * Load Variable Values from settings file
         */
        public void loadValues(){
            String falseCheck = "FALSE";

            ScreenSaver.SCREENX = Integer.parseInt(loadConfig.getValueFor("screen-width"));
            ScreenSaver.SCREENY = Integer.parseInt(loadConfig.getValueFor("screen-height"));
            this.lightACount = Integer.parseInt(loadConfig.getValueFor("light-count"));
            if(falseCheck.equals(loadConfig.getValueFor("draw-title"))){ displayTitle = false;}
            
        }
    }

