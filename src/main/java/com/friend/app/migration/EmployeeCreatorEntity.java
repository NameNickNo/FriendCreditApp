package com.friend.app.migration;

import com.friend.app.models.Employee;
import com.friend.app.service.EmployeeService;
import com.friend.app.setting.HibernateQualifier;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EmployeeCreatorEntity {

    @Value("${employee.entity.path}")
    private String employeeEntityPath;

    @Value("${employee.entity.creator.included}")
    private boolean isEmployeeEntityCreatorIncluded;

    private final EmployeeDataEnricher employeeDataEnricher;
    private final EmployeeService hibernateEmployeeService;

    public EmployeeCreatorEntity(EmployeeDataEnricher employeeDataEnricher,
                                 @HibernateQualifier EmployeeService hibernateEmployeeService) {
        this.employeeDataEnricher = employeeDataEnricher;
        this.hibernateEmployeeService = hibernateEmployeeService;
    }


    @SneakyThrows
    public void processDataFromFile() {
        if (StringUtils.isBlank(employeeEntityPath)) {
            log.error("Path parameter '{}' is empty or was not defined", employeeEntityPath);
            return;
        }

        if (!isEmployeeEntityCreatorIncluded)
            return;

        Sheet sheet = getFirstSheetFromPath(employeeEntityPath);
        List<Employee> employeeList = new CopyOnWriteArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            int rowNumber = i;

            executorService.submit(() -> {
                Optional<Employee> employee = employeeDataEnricher.getEnricherObject(sheet.getRow(rowNumber));
                employee.ifPresent(employeeList::add);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        long start = System.currentTimeMillis();
        hibernateEmployeeService.save(employeeList);
        long end = System.currentTimeMillis();
        log.info("Insert ended on {} ", end - start);
    }

    public Sheet getFirstSheetFromPath(String path) {
        Workbook workbook = new XSSFWorkbook();
        int sheetIndex = 0;
        try (InputStream inputStream = new FileInputStream(path)) {
            workbook = WorkbookFactory.create(inputStream);
            return workbook.getSheetAt(sheetIndex);
        } catch (IOException e) {
            log.error("Workbook is not found. Path: {}", path, e);
        }
        return workbook.createSheet();
    }

}
