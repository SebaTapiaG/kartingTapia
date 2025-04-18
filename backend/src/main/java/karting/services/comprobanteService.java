package karting.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import karting.entities.comprobanteEntity;
import karting.repositories.comprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import com.itextpdf.layout.Document;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import java.util.List;

@Service
public class comprobanteService {

    @Autowired
    comprobanteRepository comprobanteRepository;

    @Autowired
    JavaMailSender mailSender;

    public List<comprobanteEntity> getComprobantes() {
        return comprobanteRepository.findAll();
    }

    public comprobanteEntity getComprobanteByIdComprobante(Long idComprobante) {
        return comprobanteRepository.findByIdComprobante(idComprobante);
    }



    public comprobanteEntity saveComprobante(comprobanteEntity comprobante) {
        comprobanteEntity saved = comprobanteRepository.save(comprobante);
        try {
            enviarComprobantePorCorreo(saved);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saved;
    }


    public comprobanteEntity updateComprobante(comprobanteEntity comprobante) {
        return comprobanteRepository.save(comprobante);
    }

    public boolean deleteComprobante(Long idComprobante) {
        comprobanteRepository.deleteById(idComprobante);
        return true;
    }

    public byte[] generarComprobantePdf(comprobanteEntity comprobante) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Título
        document.add(new Paragraph("Comprobante de Reserva").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

        // Formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Información de la Reserva
        document.add(new Paragraph("\nInformación de la Reserva").setBold());
        document.add(new Paragraph("Código de Reserva: " + comprobante.getIdReserva()));
        document.add(new Paragraph("Fecha de Emisión: " + sdf.format(comprobante.getFechaEmision())));
        document.add(new Paragraph("Cantidad de Vueltas: " + comprobante.getCantVueltas()));
        document.add(new Paragraph("Tiempo Máximo: " + comprobante.getTiempoMax() + " minutos"));
        document.add(new Paragraph("Nombre Cliente: " + comprobante.getNombreCliente()));

        // Tabla de Detalles de Pago (ejemplo con solo el titular)
        document.add(new Paragraph("\nDetalle de Pago").setBold());
        Table table = new Table(new float[]{2, 2, 2, 2, 2, 2}); // 6 columnas iguales

// Encabezados
        table.addCell(new Cell().add(new Paragraph("Nombre Cliente")));
        table.addCell(new Cell().add(new Paragraph("Tarifa Base")));
        table.addCell(new Cell().add(new Paragraph("Descuento Grupo")));
        table.addCell(new Cell().add(new Paragraph("Descuento Promociones")));
        table.addCell(new Cell().add(new Paragraph("IVA")));
        table.addCell(new Cell().add(new Paragraph("Monto Final")));

        // Simulación de cálculos (reemplazar con tu lógica real)
        int tarifaBase = 10000;
        int descuentoGrupo = comprobante.getDescuento();
        int descuentoPromo = 1000;
        int montoSinIVA = tarifaBase - descuentoGrupo - descuentoPromo;
        double iva = montoSinIVA * 0.19;
        int total = (int) (montoSinIVA + iva);

        table.addCell(comprobante.getNombreCliente());
        table.addCell("$" + tarifaBase);
        table.addCell("$" + descuentoGrupo);
        table.addCell("$" + descuentoPromo);
        table.addCell("$" + (int) iva);
        table.addCell("$" + total);

        document.add(table);

        document.close();
        return outputStream.toByteArray();
    }


    public void enviarComprobantePorCorreo(comprobanteEntity comprobante) throws Exception {
        // Genera el PDF en memoria
        byte[] pdfBytes = generarComprobantePdf(comprobante);

        // Configura el correo
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(comprobante.getCorreoCliente()); // Asegúrate de que esta propiedad exista o usa la entidad de reserva si no
        helper.setSubject("Comprobante de Reserva #" + comprobante.getIdReserva());
        helper.setText("Estimado/a " + comprobante.getNombreCliente() + ",\n\nAdjunto encontrará su comprobante de reserva.\n\nSaludos,\nEquipo Karting");

        helper.addAttachment("comprobante_" + comprobante.getIdReserva() + ".pdf", new ByteArrayDataSource(pdfBytes, "application/pdf"));

        mailSender.send(message);
    }

}