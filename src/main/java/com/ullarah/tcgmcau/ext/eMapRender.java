package com.ullarah.tcgmcau.ext;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;

public class eMapRender extends MapRenderer {

    private SoftReference<BufferedImage> cacheImage;
    private boolean hasRendered = false;

    public eMapRender(String location, Boolean cache) throws IOException {

        this.cacheImage = new SoftReference<>( this.getImage( location, cache ) );

    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {

        if( this.hasRendered ) return;

        if( this.cacheImage.get() != null ) {

            canvas.drawImage( 0, 0, this.cacheImage.get() );
            this.hasRendered = true;

        } else this.hasRendered = true;

    }

    private BufferedImage getImage(String location, Boolean cache) throws IOException {

        ImageIO.setUseCache( false );
        BufferedImage cardImage;

        if( cache ) cardImage = ImageIO.read( new File( location ) );
        else cardImage = resizeImage( new URL( location ), new Dimension( 128, 128 ) );

        ImageIO.setUseCache( ImageIO.getUseCache() );

        return cardImage;

    }

    private BufferedImage resizeImage(final URL location, final Dimension size) throws IOException {

        String[] dirURL = location.toString().replace("http://cards.mcau.org/img/card/","").replace(".JPG","").split("/");
        File cardMapFolder = new File( mInit.getPlugin().getDataFolder() + File.separator + "maps" + File.separator + dirURL[0] );

        if( !cardMapFolder.exists() ) cardMapFolder.mkdirs();

        final BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = resized.createGraphics();

        try {

            g.drawImage( ImageIO.read( location ), 0, 0, size.width, size.height, null );
            g.dispose();

        } catch (IIOException e){

            Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Failed to Load: " + location.toString() + " - Has it been deleted?" );

        }


        try{

            File outputfile = new File( cardMapFolder + File.separator + dirURL[1] + ".JPG" );
            ImageIO.write( resized, "jpg", outputfile );

        } catch (ArrayIndexOutOfBoundsException e){

            Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Failed to Load: " + location.toString() + " - Has it been deleted?" );

        }

        return resized;

    }

}