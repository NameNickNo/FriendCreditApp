package com.friend.app.migration;

import com.friend.app.models.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Slf4j
public class EmployeeDataEnricher {

    public Optional<Employee> getEnricherObject(Row row) {
        try {
            Employee employee = new Employee();
            employee.setEeid(getStringCellValue(row, 0));
            employee.setFullName(getStringCellValue(row, 1));
            employee.setJobTitle(getStringCellValue(row, 2));
            employee.setDepartment(getStringCellValue(row, 3));
            employee.setBusinessUnit(getStringCellValue(row, 4));
            employee.setGender(getStringCellValue(row, 5));
            employee.setEthnicity(getStringCellValue(row, 6));
            employee.setAge((int) getNumericCellValue(row, 7));
            employee.setHireDate(getDateCellValue(row, 8));
            employee.setAnnualSalary((long) getNumericCellValue(row, 9));
            employee.setBonus(getNumericCellValue(row, 10));
            employee.setCountry(getStringCellValue(row, 11));
            employee.setCity(getStringCellValue(row, 12));
            employee.setExitDate(getDateCellValue(row, 13));

            return Optional.of(employee);
        } catch (NullPointerException e) {
            log.error("Row is null!");
            e.printStackTrace();
        } catch (RuntimeException e) {
            log.error("Employee from row " + row.getRowNum() + " not created!");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private double getNumericCellValue(Row row, int i) {
        Cell cell = getCell(row, i);
        CellType cellType = cell.getCellType();
        return cellType.compareTo(CellType.NUMERIC) == 0 ? cell.getNumericCellValue() : 0;
    }

    private LocalDate getDateCellValue(Row row, int i) {
        Cell cell = getCell(row, i);
        CellType cellType = cell.getCellType();
        return cellType.compareTo(CellType.NUMERIC) == 0 ? cell.getLocalDateTimeCellValue().toLocalDate() : null;
    }

    private String getStringCellValue(Row row, int i) {
        return getCell(row, i).getStringCellValue();
    }

    private Cell getCell(Row row, int i) {
        Cell cell = row.getCell(i);
        return cell == null ? row.createCell(i) : cell;
    }
}
