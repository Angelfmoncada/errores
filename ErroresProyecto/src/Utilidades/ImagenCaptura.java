package Utilidades;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Utilidad para manejar capturas de pantalla adjuntas a errores.
 * Copia imágenes al directorio de capturas y genera miniaturas.
 */
public class ImagenCaptura {

    private static final String DIR_CAPTURAS = "capturas";
    private static final long MAX_TAMANO_BYTES = 5 * 1024 * 1024; // 5 MB

    /**
     * Obtiene (y crea si no existe) el directorio de capturas.
     */
    public static File getDirectorioCapturas() {
        File dir = new File(DIR_CAPTURAS);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * Copia una imagen al directorio de capturas con nombre único.
     * @return la ruta relativa del archivo copiado
     * @throws IOException si el archivo excede 5MB o no se puede copiar
     */
    public static String copiarImagen(File archivoOrigen) throws IOException {
        if (archivoOrigen.length() > MAX_TAMANO_BYTES) {
            throw new IOException("La imagen excede el tamaño máximo de 5MB");
        }

        String nombreUnico = System.currentTimeMillis() + "_" + archivoOrigen.getName();
        Path destino = Paths.get(getDirectorioCapturas().getPath(), nombreUnico);

        Files.copy(archivoOrigen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return destino.toString();
    }

    /**
     * Carga una imagen y la escala para mostrar como miniatura.
     */
    public static ImageIcon cargarMiniatura(String rutaArchivo, int ancho, int alto) {
        try {
            File archivo = new File(rutaArchivo);
            if (!archivo.exists()) {
                return null;
            }
            BufferedImage original = ImageIO.read(archivo);
            if (original == null) {
                return null;
            }
            BufferedImage escalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = escalada.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(original, 0, 0, ancho, alto, null);
            g2d.dispose();
            return new ImageIcon(escalada);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Carga la imagen completa para visualización.
     */
    public static ImageIcon cargarImagenCompleta(String rutaArchivo, int maxAncho, int maxAlto) {
        try {
            File archivo = new File(rutaArchivo);
            if (!archivo.exists()) {
                return null;
            }
            BufferedImage original = ImageIO.read(archivo);
            if (original == null) {
                return null;
            }

            int anchoOrig = original.getWidth();
            int altoOrig = original.getHeight();

            // Escalar proporcionalmente si excede el máximo
            double escala = Math.min((double) maxAncho / anchoOrig, (double) maxAlto / altoOrig);
            if (escala >= 1.0) {
                return new ImageIcon(original);
            }

            int nuevoAncho = (int) (anchoOrig * escala);
            int nuevoAlto = (int) (altoOrig * escala);

            BufferedImage escalada = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = escalada.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(original, 0, 0, nuevoAncho, nuevoAlto, null);
            g2d.dispose();
            return new ImageIcon(escalada);
        } catch (IOException e) {
            return null;
        }
    }

    private static String obtenerExtension(String nombre) {
        int punto = nombre.lastIndexOf('.');
        if (punto > 0) {
            return nombre.substring(punto);
        }
        return "";
    }
}
