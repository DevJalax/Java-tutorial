import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageToByteArray {
    public static byte[] convertImageToByteArray(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    public static void main(String[] args) {
        try {
            byte[] imageData = convertImageToByteArray("path/to/image.jpg");
            System.out.println("Image converted to byte array: " + imageData.length + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
