package Utilidades;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Clase utilitaria para mantener un tema visual consistente
 * en toda la aplicacion. Centraliza colores, fuentes y estilos.
 */
public class Tema {

    // --- Paleta de colores ---
    public static final Color PRIMARIO = new Color(0, 0, 102);
    public static final Color PRIMARIO_CLARO = new Color(41, 128, 185);
    public static final Color EXITO = new Color(46, 204, 113);
    public static final Color PELIGRO = new Color(255, 51, 51);
    public static final Color FONDO = new Color(240, 240, 245);
    public static final Color FONDO_PANEL = new Color(250, 250, 252);
    public static final Color FILA_PAR = new Color(245, 245, 250);
    public static final Color FILA_IMPAR = Color.WHITE;
    public static final Color SELECCION = new Color(41, 128, 185);
    public static final Color TEXTO = new Color(33, 33, 33);
    public static final Color TEXTO_CLARO = Color.WHITE;
    public static final Color BORDE_ERROR = new Color(231, 76, 60);
    public static final Color BORDE_OK = new Color(46, 204, 113);

    // --- Colores por severidad ---
    public static final Color SEVERIDAD_BAJA = new Color(220, 237, 200);
    public static final Color SEVERIDAD_MEDIA = new Color(255, 245, 187);
    public static final Color SEVERIDAD_ALTA = new Color(255, 224, 178);
    public static final Color SEVERIDAD_CRITICA = new Color(255, 205, 210);

    // --- Colores por fase ---
    public static final Color FASE_REGISTRADO = new Color(227, 242, 253);
    public static final Color FASE_PROCESO = new Color(255, 243, 224);
    public static final Color FASE_SOLUCIONADO = new Color(220, 237, 200);
    public static final Color FASE_CERRADO = new Color(224, 224, 224);

    // --- Fuentes ---
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FUENTE_TABLA = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_CAMPO = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_STATUS = new Font("Segoe UI", Font.PLAIN, 12);

    // --- Formato de fecha ---
    public static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat FORMATO_FECHA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Retorna el color de fondo correspondiente a una severidad.
     */
    public static Color getColorSeveridad(String severidad) {
        if (severidad == null) return FILA_IMPAR;
        switch (severidad) {
            case "BAJA": return SEVERIDAD_BAJA;
            case "MEDIA": return SEVERIDAD_MEDIA;
            case "ALTA": return SEVERIDAD_ALTA;
            case "CRITICA": return SEVERIDAD_CRITICA;
            default: return FILA_IMPAR;
        }
    }

    /**
     * Retorna el color de fondo correspondiente a una fase.
     */
    public static Color getColorFase(String fase) {
        if (fase == null) return FILA_IMPAR;
        switch (fase) {
            case "REGISTRADO": return FASE_REGISTRADO;
            case "PROCESO": return FASE_PROCESO;
            case "SOLUCIONADO": return FASE_SOLUCIONADO;
            case "CERRADO": return FASE_CERRADO;
            default: return FILA_IMPAR;
        }
    }

    /**
     * Aplica el estilo de tabla con colores por severidad, fuentes y formato de fechas.
     * La columna de indice colSeveridad se usa para colorear las filas.
     */
    public static void aplicarEstiloTabla(JTable tabla, int colSeveridad) {
        tabla.setRowHeight(30);
        tabla.setFont(FUENTE_TABLA);
        tabla.getTableHeader().setFont(FUENTE_TABLA_HEADER);
        tabla.getTableHeader().setBackground(PRIMARIO);
        tabla.getTableHeader().setForeground(TEXTO_CLARO);
        tabla.setSelectionBackground(SELECCION);
        tabla.setSelectionForeground(TEXTO_CLARO);
        tabla.setGridColor(new Color(220, 220, 225));
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new java.awt.Dimension(1, 1));

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Object valorMostrar = value;
                if (value instanceof java.sql.Timestamp) {
                    valorMostrar = FORMATO_FECHA_HORA.format(value);
                } else if (value instanceof java.util.Date) {
                    valorMostrar = FORMATO_FECHA.format(value);
                }

                Component c = super.getTableCellRendererComponent(table, valorMostrar,
                        isSelected, hasFocus, row, column);

                if (!isSelected) {
                    // Colorear por severidad
                    Object sevVal = table.getValueAt(row, colSeveridad);
                    String sev = sevVal != null ? sevVal.toString() : "";
                    c.setBackground(getColorSeveridad(sev));
                    c.setForeground(TEXTO);
                }
                return c;
            }
        });
    }

    /**
     * Version simple sin coloreo por severidad (filas alternadas).
     */
    public static void aplicarEstiloTabla(JTable tabla) {
        tabla.setRowHeight(30);
        tabla.setFont(FUENTE_TABLA);
        tabla.getTableHeader().setFont(FUENTE_TABLA_HEADER);
        tabla.getTableHeader().setBackground(PRIMARIO);
        tabla.getTableHeader().setForeground(TEXTO_CLARO);
        tabla.setSelectionBackground(SELECCION);
        tabla.setSelectionForeground(TEXTO_CLARO);
        tabla.setGridColor(new Color(220, 220, 225));
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new java.awt.Dimension(1, 1));

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Object valorMostrar = value;
                if (value instanceof java.sql.Timestamp) {
                    valorMostrar = FORMATO_FECHA_HORA.format(value);
                } else if (value instanceof java.util.Date) {
                    valorMostrar = FORMATO_FECHA.format(value);
                }

                Component c = super.getTableCellRendererComponent(table, valorMostrar,
                        isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? FILA_PAR : FILA_IMPAR);
                    c.setForeground(TEXTO);
                }
                return c;
            }
        });
    }
}
