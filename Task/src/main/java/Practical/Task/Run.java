package Practical.Task;

import Practical.Task.enteties.Employee;
import Practical.Task.enteties.ReportDefinition;
import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Run implements CommandLineRunner {
    private static Scanner scanner = new Scanner(System.in);
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private final Gson gson;


    public Run(Gson gson) {
        this.gson = gson;
    }


    @Override
    public void run(String... args) throws Exception {
        boolean correct = false;
        System.out.println("Enter employees path(The file need to be in resources):");
        String employeesFile = scanner.nextLine();
        ArrayList<Employee> employeesList = getEmployees(employeesFile);

        System.out.println("Enter report details path:");
        String reportPath = scanner.nextLine();
        ReportDefinition report = getReport(reportPath);
        ArrayList<Employee> reportedEmployees = getReportedEmployees(employeesList, report);
        System.out.println("Enter CSV file name:");
        String cvfName = scanner.nextLine();
        createCSVFile(cvfName, reportedEmployees);
    }

    private ArrayList<Employee> getReportedEmployees(ArrayList<Employee> employees,
                                                     ReportDefinition reportDefinition) {
        ArrayList<Employee> employeesArray = new ArrayList<>();
        if (reportDefinition.isUseExprienceMultiplier()) {
            for (Employee employee : employees) {
                double score = ((double) employee.getTotalSales() / (double) employee.getSalesPeriod() * employee.getExperienceMultiplier());
                employee.setScore(getRound(score));
                if (periodLimit(employee.getSalesPeriod(), reportDefinition.getPeriodLimit())) {
                    employeesArray.add(employee);
                }

            }
        } else {
            for (Employee employee : employees) {
                double score = (double) employee.getTotalSales() / (double) employee.getSalesPeriod();
                employee.setScore(getRound(score));
                ;
                if (periodLimit(employee.getSalesPeriod(), reportDefinition.getPeriodLimit())) {
                    employeesArray.add(employee);
                }

            }
        }
        int limit = limit(employeesArray.size() * reportDefinition.getTopPerformersThreshold() / 100);


        List<Employee> collect = employeesArray.stream().sorted((a1, a2) -> {
            double a1Score = a1.getScore();
            double a2Score = a2.getScore();
            return Double.compare(a2Score, a1Score);
        }).limit(limit).collect(Collectors.toList());
        return (ArrayList<Employee>) collect;
    }

    private void createCSVFile(String filename, ArrayList<Employee> reportData) throws IOException {
        String[] HEADERS = {"Name", "Score"};
        FileWriter out = new FileWriter(filename + ".csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader(HEADERS))) {
            reportData.forEach((employee) -> {
                try {
                    printer.printRecord(employee.getName(), employee.getScore());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private ReportDefinition getReport(String path) {
        String file = "src\\main\\resources\\" + path + ".txt";
        try {
            ReportDefinition employees = gson.fromJson(new FileReader(file), ReportDefinition.class);
            return employees;
        } catch (FileNotFoundException e) {
            System.out.println("Wrong path try again:");
            String sc = scanner.nextLine();
            return getReport(sc);
        }
    }

    private ArrayList<Employee> getEmployees(String path) {
        String file = "src\\main\\resources\\" + path + ".txt";
        try {
            Employee[] employees = gson.fromJson(new FileReader(file), Employee[].class);
            return new ArrayList<>(Arrays.asList(employees));
        } catch (FileNotFoundException e) {
            System.out.println("Wrong path try again:");
            String sc = scanner.nextLine();
            return getEmployees(sc);
        }
    }

    private boolean periodLimit(int salePeriod, int periodLimit) {
        return salePeriod >= periodLimit;
    }

    private double getRound(Double n) {
        return Double.parseDouble(df.format(n));
    }

    private int limit(int limit) {
        if (limit > 1) {
            return limit;
        } else {
            return 1;
        }
    }
}
