import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.io.IURLProtocolHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class VideoToByteArray {
    public static byte[] convertVideoToByteArray(String videoPath) throws IOException {
        IContainer container = IContainer.make();
        if (container.open(videoPath, IContainer.Type.READ, null) < 0) {
            throw new IllegalArgumentException("Could not open video file");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        
        try (InputStream inputStream = container.getURLProtocolHandler().openInputStream(videoPath)) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        }
        
        return baos.toByteArray();
    }

    public static void main(String[] args) {
        try {
            byte[] videoData = convertVideoToByteArray("path/to/video.mp4");
            System.out.println("Video converted to byte array: " + videoData.length + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
