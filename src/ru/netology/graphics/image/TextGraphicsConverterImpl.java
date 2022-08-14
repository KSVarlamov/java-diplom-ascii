package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private int maxWidth = -1;
    private int maxHeight = -1;
    private double maxRatio = -1.0;
    private TextColorSchema textColorSchema = new TextColorSchemaDefaultImpl();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        StringBuilder sb = new StringBuilder();
        BufferedImage img = ImageIO.read(new URL(url));
        if (maxRatio > 0) {
            double imgRatio = 1.0 * img.getWidth() / img.getHeight();
            if (maxRatio - imgRatio < 0) {
                throw new BadImageSizeException(imgRatio, maxRatio);
            }
        }
        double horizontalRatio = 1;
        double verticalRatio;
        double scale = 1;
        if ((img.getWidth() - maxWidth > 0) && (maxWidth > 0)) {
            horizontalRatio = 1.0 * maxWidth / img.getWidth();
            scale = horizontalRatio;
        }
        if ((img.getHeight() - maxHeight > 0) && (maxHeight > 0)) {
            verticalRatio = 1.0 * maxHeight / img.getHeight();
            if (horizontalRatio != 1) {
                scale = Math.min(verticalRatio, horizontalRatio);
            } else {
                scale = verticalRatio;
            }
        }
        int newWidth = (int) (img.getWidth() * scale);
        int newHeight = (int) (img.getHeight() * scale);
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        System.out.printf("Изначальное изображение %sx%s\n", img.getWidth(), img.getHeight());
        System.out.printf("Новое изображение %sx%s\n", newWidth, newHeight);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        int[] tmpArr = new int[3];
        WritableRaster bwRaster = bwImg.getRaster();
        for (int h = 0; h < bwImg.getHeight(); h++) {
            for (int w = 0; w < bwImg.getWidth(); w++) {
                int color = bwRaster.getPixel(w, h, tmpArr)[0];
                char c = textColorSchema.convert(color);
                sb.append(c);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.textColorSchema = schema;
    }
}
