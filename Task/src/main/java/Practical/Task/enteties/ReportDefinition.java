package Practical.Task.enteties;


import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDefinition {
    @Expose(serialize = false, deserialize = true)
    private int topPerformersThreshold;
    @Expose(serialize = false, deserialize = true)
    private boolean useExprienceMultiplier;
    @Expose(serialize = false, deserialize = true)
    private int periodLimit;
}
