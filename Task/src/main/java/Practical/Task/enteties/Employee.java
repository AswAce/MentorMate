package Practical.Task.enteties;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Expose(serialize =false ,deserialize = true)
    private String name;

    @Expose(serialize =false ,deserialize = true)
    private long totalSales;
    @Expose(serialize =false ,deserialize = true)
    private int salesPeriod;
    @Expose(serialize =false ,deserialize = true)
    private double experienceMultiplier;

    @Expose(serialize =false ,deserialize = false)
    private double score;


}
