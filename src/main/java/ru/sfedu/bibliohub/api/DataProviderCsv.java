package ru.sfedu.bibliohub.api;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import ru.sfedu.bibliohub.utils.ConfigurationUtil;
import ru.sfedu.bibliohub.utils.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProviderCsv extends FileDataProvider {

    public DataProviderCsv() {
        try {
            fileNamePattern = ConfigurationUtil.getConfigurationEntry(Constants.CSV_PATH) + Constants.CSV_PATTERN;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected <T> List<T> read(Class<T> type) {
        List<T> list = new ArrayList<>();
        try {
            File file = initFile(getFileName(type));
            if (file.length() > 0) {
                CSVReader csvReader = new CSVReader(new FileReader(file));
                CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader).withType(type).build();
                list.addAll(csvToBean.parse());
                csvReader.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    protected <T> boolean write(List<T> list, Class<T> type, String methodName) {
        try {
            File file = initFile(getFileName(type));
            CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter).build();
            beanToCsv.write(list);
            csvWriter.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            sendLogs(methodName, list.size() > 0 ? list.get(list.size() - 1) : null, false);
            return false;
        }
        sendLogs(methodName, list.size() > 0 ? list.get(list.size() - 1) : null, true);
        return true;
    }
}
