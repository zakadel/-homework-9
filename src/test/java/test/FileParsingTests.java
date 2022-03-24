package test;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class FileParsingTests {
    private static final String CSV_FILE_NAME = "csv_example.csv";
    private static final String XLSX_FILE_NAME = "matrix_api_test.xlsx";
    private static final String PDF_FILE_NAME = "test_plan_primer.pdf";
    private static final String ZIP_FILE_PATH = "src/test/resources/zipFile.zip";

    public ZipFile unZip(String zipFilePath) throws Exception {
        return new ZipFile(zipFilePath);
    }

    @Test
    public void testPdf() throws Exception {
        ZipEntry zipEntry = unZip(ZIP_FILE_PATH).getEntry(PDF_FILE_NAME);
        InputStream inputStream = unZip(ZIP_FILE_PATH).getInputStream(zipEntry);
        PDF pdf = new PDF(inputStream);
        assertThat(pdf.author).isEqualTo("baban666");
        assertThat(pdf.numberOfPages).isEqualTo(12);
        assertThat(pdf.text).contains("Test Plan");
    }

    @Test
    public void testCsv() throws Exception {
        ZipEntry zipEntry = unZip(ZIP_FILE_PATH).getEntry(CSV_FILE_NAME);
        try (InputStream inputStream = unZip(ZIP_FILE_PATH).getInputStream(zipEntry);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> allStrings = reader.readAll();
            assertThat(allStrings.get(1)).contains("1;abc;cba;20");
            assertThat(allStrings.size()).isEqualTo(21);
        }
    }

    @Test
    public void testXlsx() throws Exception {
        ZipEntry zipEntry = unZip(ZIP_FILE_PATH).getEntry(XLSX_FILE_NAME);
        InputStream inputStream = unZip(ZIP_FILE_PATH).getInputStream(zipEntry);
        XLS xlsx = new XLS(inputStream);
        assertThat(xlsx.excel.getSheetName(0)).isEqualTo("Sheet1");
        String expectedResult = "Проверить код состояния:";
        String actualResult = xlsx.excel.getSheet("Sheet1").getRow(2).getCell(2).getStringCellValue();
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(actualResult);
        String utf8ActualResult = StandardCharsets.UTF_8.decode(buffer).toString();
        assertThat(expectedResult).isEqualTo(utf8ActualResult);
    }
}
