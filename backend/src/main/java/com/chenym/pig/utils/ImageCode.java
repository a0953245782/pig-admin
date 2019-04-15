package com.chenym.pig.utils;

import java.awt.image.BufferedImage;

public class ImageCode {
    private BufferedImage image;

    public ImageCode(BufferedImage image, String sRand, int expireId) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
