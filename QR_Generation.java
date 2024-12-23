import com.google.zxing.BarcodeFormat;
import com.google.zxing.BitMatrix;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;


class QR_Generation {
    public static void main(String[] args) {
       String qrCodeData = "https://example.com?id=123";
BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, 500, 500);
BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

// Load your private key
PrivateKey privateKey = loadPrivateKey();

// Generate a digital signature of the QR code data
Signature signature = Signature.getInstance("SHA256withRSA");
signature.initSign(privateKey);
signature.update(qrCodeData.getBytes());
byte[] digitalSignature = signature.sign();

// Embed the signature into the image metadata
ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(image);
IIOMetadata metadata = imageOutputStream.getStreamMetadata();
IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree("javax_imageio_1.0");
IIOMetadataNode textNode = new IIOMetadataNode("Text");
IIOMetadataNode textEntry = new IIOMetadataNode("TextEntry");
textEntry.setAttribute("keyword", "Signature");
textEntry.setAttribute("value", Base64.getEncoder().encodeToString(digitalSignature));
textNode.appendChild(textEntry);
root.appendChild(textNode);
metadata.setFromTree("javax_imageio_1.0", root);

URL uploadUrl = new URL(url);
HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();
connection.setDoOutput(true);
connection.setRequestMethod("POST");

try (OutputStream os = connection.getOutputStream()) {
    ImageIO.write(image, "png", os);
}

int responseCode = connection.getResponseCode();
// Handle response code as needed

    }
}
