package edu.tecnm.util; 

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems; // Necesario para obtener el baseUrl

@Service
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;

    public PdfGeneratorService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(String templateName, Context context) {
        
        String html = templateEngine.process(templateName, context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            
            // Define la ruta base para que Flying Saucer resuelva CSS y recursos internos.
            String baseUrl = FileSystems.getDefault().getPath("").toUri().toURL().toString();
            
            ITextRenderer renderer = new ITextRenderer();
            
            // Usa la versión que acepta el baseUrl
            renderer.setDocumentFromString(html, baseUrl); 
            
            renderer.layout();
            renderer.createPDF(os);
            return os.toByteArray();
        } catch (Exception e) {
            System.err.println("Error fatal al generar PDF: " + e.getMessage());
            e.printStackTrace();
            return new byte[0]; 
        }
    }
}