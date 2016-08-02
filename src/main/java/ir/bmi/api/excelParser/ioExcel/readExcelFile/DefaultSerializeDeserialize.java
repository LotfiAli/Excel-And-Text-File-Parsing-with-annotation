package ir.bmi.api.excelParser.ioExcel.readExcelFile;

import ir.bmi.api.excelParser.exception.BaseExcelParserException;
import ir.bmi.api.excelParser.model.ResultModel;
import ir.bmi.api.excelParser.parser.MetaDataObject;
import ir.bmi.api.excelParser.parserWrapper.ParserFile;
import ir.bmi.api.excelParser.reflection.Utility;
import ir.bmi.api.excelParser.validation.ValidationResult;


/**
 * Created by alotfi on 5/25/2016.
 */

//May Be Must Use Decorator Pattern
public final class DefaultSerializeDeserialize implements SerializeFile, DeSerializeFile {

    private ValidationResult validationResult;

    public DefaultSerializeDeserialize() throws BaseExcelParserException {
        validationResult = new ValidationResult();
    }

    public ResultModel deserializeFile(MetaDataObject metaDataObject, Class typeResult, ParserFile parserFile) throws BaseExcelParserException {
        Object targetObject = null;
        parserFile.parse(metaDataObject);
        targetObject = createObjectFromMetaData(metaDataObject, typeResult, parserFile);
        ResultModel resultModel = new ResultModel();
        resultModel.setValidationResult(validationResult);
        resultModel.setResultModel(targetObject);
        return resultModel;
    }

    public void serializeFile(MetaDataObject metaDataObject, ParserFile parserFile) throws BaseExcelParserException {
        parserFile.create(metaDataObject);
    }

    private Object createObjectFromMetaData(MetaDataObject metaDataObject, Class typeResult, ParserFile parserFile) throws BaseExcelParserException {
        Object targetObject;
        targetObject = Utility.newInstanceFromType(typeResult);
        for (MetaDataObject metaData : metaDataObject.getMetaDataObjects()) {
            CreateObjectFromMetaData readerField = CreateObjectFromMetaData.createReaderField(targetObject, metaData, parserFile, validationResult);
            readerField.read(metaData);
        }
        return targetObject;
    }
}
