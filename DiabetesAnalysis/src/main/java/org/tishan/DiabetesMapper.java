package org.tishan;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class DiabetesMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Skip header row
        if (key.get() == 0 && value.toString().contains("year,gender,age")) {
            return;
        }

        // Split CSV line
        String[] fields = value.toString().split(",");
        if (fields.length < 17) return; // Ensure valid row

        try {
            // Extract gender and race fields
            String gender = fields[1].trim();
            String race = getRace(fields);
            double hbA1c = Double.parseDouble(fields[13].trim()); // HbA1c_level

            // Create composite key: gender_race
            String compositeKey = gender + "_" + race;

            // Emit key-value pair
            context.write(new Text(compositeKey), new DoubleWritable(hbA1c));
        } catch (NumberFormatException e) {
            // Skip rows with invalid numeric data
        }
    }

    // Helper method to extract race
    private String getRace(String[] fields) {
        if (fields[4].trim().equals("1")) return "AfricanAmerican";
        if (fields[5].trim().equals("1")) return "Asian";
        if (fields[6].trim().equals("1")) return "Caucasian";
        if (fields[7].trim().equals("1")) return "Hispanic";
        return "Other";
    }
}
