package org.tishan;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiabetesReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double sum = 0.0;
        int count = 0;

        // Sum HbA1c values and count occurrences
        for (DoubleWritable value : values) {
            sum += value.get();
            count++;
        }

        // Calculate average
        double average = count > 0 ? sum / count : 0.0;

        // Round to 4 decimal
        BigDecimal roundedAvg = new BigDecimal(average).setScale(4, RoundingMode.HALF_UP);

        // Emit key and average HbA1c
        context.write(key, new DoubleWritable(roundedAvg.doubleValue()));
    }
}
