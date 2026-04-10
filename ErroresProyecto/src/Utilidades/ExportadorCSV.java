package Utilidades;

import Modelo.ErrorTicket;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Utilidad para exportar listas de errores a formato CSV.
 * Permite al usuario generar reportes descargables.
 */
public class ExportadorCSV {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Exporta una lista de errores a un archivo CSV.
     * @param errores lista de errores a exportar
     * @param archivo archivo destino (.csv)
     * @throws IOException si no se puede escribir el archivo
     */
    public static void exportar(List<ErrorTicket> errores, File archivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            // Encabezados
            writer.write("ID,Titulo,Descripcion,Severidad,Fase,Fecha,Solucion,Resuelto Por,Fecha Solucion,Pasos Reproducir");
            writer.newLine();

            for (ErrorTicket e : errores) {
                writer.write(String.join(",",
                    String.valueOf(e.getId()),
                    escaparCSV(e.getTitulo()),
                    escaparCSV(e.getDescripcion()),
                    e.getSeveridad().name(),
                    e.getFase().name(),
                    e.getFecha() != null ? SDF.format(e.getFecha()) : "",
                    escaparCSV(e.getSolucion()),
                    escaparCSV(e.getResueltoPor()),
                    e.getFechaSolucion() != null ? SDF.format(e.getFechaSolucion()) : "",
                    escaparCSV(e.getPasosReproducir())
                ));
                writer.newLine();
            }
        }
    }

    /**
     * Escapa un valor para que sea seguro en CSV.
     * Envuelve en comillas dobles si contiene comas, saltos de linea o comillas.
     */
    private static String escaparCSV(String valor) {
        if (valor == null) {
            return "";
        }
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
