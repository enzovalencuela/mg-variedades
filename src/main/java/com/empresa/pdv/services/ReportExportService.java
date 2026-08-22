package com.empresa.pdv.services;

import com.empresa.pdv.models.FormatoExportacao;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * RF23: exportação dos relatórios gerados pelos demais serviços, preservando os filtros
 * aplicados na consulta em tela, em formato CSV (planilha) ou PDF real (via Apache PDFBox).
 */
public class ReportExportService {

    private static final PDFont FONTE_TITULO = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final PDFont FONTE_TEXTO = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final float MARGEM = 50f;
    private static final float TAMANHO_TITULO = 14f;
    private static final float TAMANHO_TEXTO = 9f;
    private static final float ALTURA_LINHA = 14f;
    private static final int MAX_CARACTERES_CELULA = 40;

    public byte[] exportar(String nomeRelatorio, Map<String, String> filtrosAplicados, List<String> cabecalhos,
                            List<List<String>> linhas, FormatoExportacao formato) {
        return switch (formato) {
            case CSV -> exportarCsv(cabecalhos, linhas).getBytes(StandardCharsets.UTF_8);
            case PDF -> exportarPdf(nomeRelatorio, filtrosAplicados, cabecalhos, linhas);
        };
    }

    private String exportarCsv(List<String> cabecalhos, List<List<String>> linhas) {
        StringBuilder csv = new StringBuilder();
        csv.append(String.join(",", escaparLinha(cabecalhos))).append("\n");
        for (List<String> linha : linhas) {
            csv.append(String.join(",", escaparLinha(linha))).append("\n");
        }
        return csv.toString();
    }

    private List<String> escaparLinha(List<String> valores) {
        return valores.stream()
                .map(valor -> valor == null ? "" : valor)
                .map(valor -> valor.contains(",") || valor.contains("\"")
                        ? "\"" + valor.replace("\"", "\"\"") + "\""
                        : valor)
                .toList();
    }

    /** RF23: exportação real em PDF, com título, filtros aplicados e tabela de dados, paginada. */
    private byte[] exportarPdf(String nomeRelatorio, Map<String, String> filtrosAplicados, List<String> cabecalhos,
                                List<List<String>> linhas) {
        try (PDDocument document = new PDDocument()) {
            Pagina pagina = novaPagina(document);

            pagina.escrever(FONTE_TITULO, TAMANHO_TITULO, nomeRelatorio);
            pagina.pularLinha();

            if (filtrosAplicados != null && !filtrosAplicados.isEmpty()) {
                pagina.escrever(FONTE_TITULO, TAMANHO_TEXTO, "Filtros aplicados:");
                for (Map.Entry<String, String> filtro : filtrosAplicados.entrySet()) {
                    pagina = garantirEspaco(document, pagina);
                    pagina.escrever(FONTE_TEXTO, TAMANHO_TEXTO, "  - " + filtro.getKey() + ": " + filtro.getValue());
                }
                pagina.pularLinha();
            }

            float larguraUtil = PDRectangle.A4.getWidth() - 2 * MARGEM;
            float larguraColuna = larguraUtil / Math.max(1, cabecalhos.size());

            pagina = garantirEspaco(document, pagina);
            pagina.escreverLinhaTabela(FONTE_TITULO, cabecalhos, larguraColuna);

            for (List<String> linha : linhas) {
                pagina = garantirEspaco(document, pagina);
                pagina.escreverLinhaTabela(FONTE_TEXTO, linha, larguraColuna);
            }
            pagina.fechar();

            ByteArrayOutputStream saida = new ByteArrayOutputStream();
            document.save(saida);
            return saida.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao gerar o PDF do relatório: " + nomeRelatorio, e);
        }
    }

    private Pagina novaPagina(PDDocument document) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        return new Pagina(contentStream, PDRectangle.A4.getHeight() - MARGEM);
    }

    /** Fecha a página atual e abre uma nova quando não há mais espaço vertical disponível. */
    private Pagina garantirEspaco(PDDocument document, Pagina pagina) throws IOException {
        if (pagina.y > MARGEM + ALTURA_LINHA) {
            return pagina;
        }
        pagina.fechar();
        return novaPagina(document);
    }

    /** Controla a posição vertical de escrita e o content stream da página PDF atual. */
    private static final class Pagina {
        private final PDPageContentStream contentStream;
        private float y;

        private Pagina(PDPageContentStream contentStream, float y) {
            this.contentStream = contentStream;
            this.y = y;
        }

        void escrever(PDFont fonte, float tamanho, String texto) throws IOException {
            contentStream.beginText();
            contentStream.setFont(fonte, tamanho);
            contentStream.newLineAtOffset(MARGEM, y);
            contentStream.showText(texto == null ? "" : texto);
            contentStream.endText();
            pularLinha();
        }

        void escreverLinhaTabela(PDFont fonte, List<String> celulas, float larguraColuna) throws IOException {
            contentStream.beginText();
            contentStream.setFont(fonte, TAMANHO_TEXTO);
            contentStream.newLineAtOffset(MARGEM, y);
            for (int i = 0; i < celulas.size(); i++) {
                if (i > 0) {
                    contentStream.newLineAtOffset(larguraColuna, 0);
                }
                contentStream.showText(truncar(celulas.get(i)));
            }
            contentStream.endText();
            pularLinha();
        }

        private String truncar(String valor) {
            if (valor == null) return "";
            return valor.length() > MAX_CARACTERES_CELULA
                    ? valor.substring(0, MAX_CARACTERES_CELULA - 3) + "..."
                    : valor;
        }

        void pularLinha() {
            y -= ALTURA_LINHA;
        }

        void fechar() throws IOException {
            contentStream.close();
        }
    }
}
