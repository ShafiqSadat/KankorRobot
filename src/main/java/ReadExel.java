import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExel {

    private static final String NAME = "C:\\Users\\Shafi\\Desktop\\@kankors-1401-103138.xlsx";

    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream(NAME);
            Workbook workbook = new XSSFWorkbook(file);
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Sheet> sheets = workbook.sheetIterator();
            while(sheets.hasNext()) {
                Sheet sh = sheets.next();
//                System.out.println("Sheet name is "+sh.getSheetName());
//                System.out.println("---------");
                for (Row row : sh) {
                    System.out.println(row.getCell(0));

                    ResultsModel resultsModel = new ResultsModel();
                    resultsModel.setKankorID(dataFormatter.formatCellValue(row.getCell(0)));
                    resultsModel.setName(dataFormatter.formatCellValue(row.getCell(1)));
                    resultsModel.setFatherName(dataFormatter.formatCellValue(row.getCell(2)));
                    resultsModel.setGrandFatherName(dataFormatter.formatCellValue(row.getCell(3)));
                    resultsModel.setSchool(dataFormatter.formatCellValue(row.getCell(7)));
                    resultsModel.setPoints(dataFormatter.formatCellValue(row.getCell(8)));
                    resultsModel.setResult(dataFormatter.formatCellValue(row.getCell(9)));
                    resultsModel.setProvince(dataFormatter.formatCellValue(row.getCell(6)));
                    resultsModel.setSex(dataFormatter.formatCellValue(row.getCell(4)));
                    KankorDB.addResults(resultsModel);


//                    for (Cell cell : row) {
//                        String cellValue = dataFormatter.formatCellValue(cell);
//
//                        //if(cell.getCellType() == CellType.STRING) {
//                        //
//                        //}
////                        if (cell.getRow().getCell(1).toString().equals("روزینا") && cell.getRow().getCell(2).toString().equals("محمدیاسین")) {
////                            System.out.println(cellValue);
////                        }
////                        System.out.print();
//                    }
//                    System.out.println();
                }
            }
            workbook.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

}