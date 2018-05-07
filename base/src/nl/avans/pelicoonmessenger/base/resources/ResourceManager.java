package nl.avans.pelicoonmessenger.base.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ResourceManager {

    public BufferedImage getImage(String name){
        try {
            return ImageIO.read(getClass().getResource(name));
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
