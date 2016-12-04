package ir.bmi.api.WrapperFile.excel.excel;


import ir.bmi.api.excelParser.base.templateComponent.wrapperFile.WrapperBody;
import ir.bmi.api.excelParser.base.templateComponent.wrapperFile.WrapperHeader;
import ir.bmi.api.excelParser.exception.IOExcelException;
import ir.bmi.api.excelParser.parser.MetaDataObject;
import ir.bmi.api.excelParser.parserWrapper.ParserSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alotfi on 6/6/2016.
 */
public class ExcelParserSheet implements ParserSheet {
    private XSSFSheet sheet;
    private XSSFWorkbook xssfWorkbook;
    private MetaDataObject metaDataObject;
    private String sheetName;
    private WrapperBody wrapperBody;

    public ExcelParserSheet(XSSFWorkbook xssfWorkbook, MetaDataObject metaDataObject) {
        this.xssfWorkbook = xssfWorkbook;
        this.metaDataObject = metaDataObject;
        this.sheetName = metaDataObject.getSheetName();
    }

    public ExcelParserSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public WrapperBody getBody() throws IOExcelException {
        wrapperBody = new WrapperBody(new ExcelParserBody(sheet));
        return wrapperBody;
    }

    public String getName() {
        return sheet.getSheetName();
    }

    public void create() throws IOExcelException {
        sheet = xssfWorkbook.createSheet(this.sheetName);
        createContentFile();
    }

    private void createContentFile() throws IOExcelException {
        ExcelParserBody parserBody = null;
        if (metaDataObject.getTitleHolder()) {
            createHeader();
            parserBody = createBody(metaDataObject.getMetaDataObjects());
        } else {
            ArrayList<MetaDataObject> metaDataObjects = new ArrayList<MetaDataObject>();
            metaDataObjects.add(metaDataObject);
            parserBody = createBody(metaDataObjects);
        }
        wrapperBody = new WrapperBody(parserBody);
    }

    private ExcelParserBody createBody(List<MetaDataObject> metaDataObjects) {
        ExcelParserBody parserBody = new ExcelParserBody(xssfWorkbook, sheet, metaDataObjects);
        parserBody.create();
        return parserBody;
    }

    private void createHeader() {
        ExcelParserHeader parserRow = new ExcelParserHeader(xssfWorkbook, sheet, metaDataObject.getMetaDataObjects());
        parserRow.create();
//        wrapperHeader = new WrapperRow(parserRow);
    }

    public WrapperHeader getHeader() {
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator == null) {
            throw new IllegalArgumentException();
        }
        return new WrapperHeader(new ExcelParserRow(rowIterator.next()));
    }
}
