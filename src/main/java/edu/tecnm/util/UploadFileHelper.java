package edu.tecnm.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;

public class UploadFileHelper {

    public static String guardarArchivo(MultipartFile file, String rutaDestino) {
        if (file == null || file.isEmpty()) return null;

        try {
            Path dir = Paths.get(rutaDestino).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            // Nombre original "limpio"
            String original = file.getOriginalFilename();
            if (original == null) original = "imagen";
            original = original.replaceAll("[^a-zA-Z0-9._-]", "_");

            // separa base/extensión
            int dot = original.lastIndexOf('.');
            String base = (dot > 0) ? original.substring(0, dot) : original;
            String ext  = (dot > 0 && dot < original.length() - 1) ? original.substring(dot) : "";

            // normaliza/deduce extensión
            if (ext.equalsIgnoreCase(".jpeg")) ext = ".jpg";
            if (ext.isBlank()) {
                String ct = file.getContentType();
                if (ct != null) ct = ct.toLowerCase(Locale.ROOT);
                if (ct != null && ct.contains("png")) ext = ".png";
                else if (ct != null && (ct.contains("jpg") || ct.contains("jpeg"))) ext = ".jpg";
                else ext = ".jpg"; // DEFAULT
            }

            // nombre final ÚNICO
            String nombre = System.currentTimeMillis() + "_" + base + ext;
            Path destino = dir.resolve(nombre);
            int i = 1;
            while (Files.exists(destino)) {
                nombre = System.currentTimeMillis() + "_" + base + "_" + i + ext;
                destino = dir.resolve(nombre);
                i++;
            }

            Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            return nombre; // <-- guarda SOLO el nombre con extensión

        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
