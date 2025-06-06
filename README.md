# Diabetes HbA1c Analysis Using MapReduce

## Objective
This project implements a MapReduce job using Hadoop to analyze the Diabetes Clinical Dataset (100,000 rows) to compute the average HbA1c level by gender and race, identifying patterns in diabetes risk factors.

## Dataset
- **Source**: Diabetes Clinical Dataset (publicly available, e.g., Kaggle).
- **Description**: Contains 100,000 rows with columns: year, gender, age, location, race (AfricanAmerican, Asian, Caucasian, Hispanic, Other), hypertension, heart_disease, smoking_history, bmi, hbA1c_level, blood_glucose_level, diabetes, clinical_notes.
- **Task**: Calculate average HbA1c level for each gender-race combination.

## Prerequisites
- Java 8 or higher
- Hadoop 3.x (tested with 3.4.0)
- Maven (for dependency management)

## Setup Instructions
1. **Install Hadoop**:
    - Download Hadoop 3.4.0 from [Apache Hadoop](https://hadoop.apache.org/releases.html).
    - Extract :
        ```bash
        tar xzf hadoop-3.4.0.tar.gz
        ```
    - Configure Hadoop Environment Variables (bashrc)
        1. Edit the .bashrc shell configuration file using a text editor of your choice (we will use nano):
            ```bash
            nano .bashrc
            ```
        2. Define the Hadoop environment variables by adding the following content to the end of the file:
            ![bashrc](/Images/bashrc.png)

        3. Run the command below to apply the changes to the current running environment:
            ```bash
            source ~/.bashrc
            ```
    - Edit hadoop-env.sh File
        1. Use the previously created $HADOOP_HOME variable to access the hadoop-env.sh file:
            ```bash
            nano $HADOOP_HOME/etc/hadoop/hadoop-env.sh
            ```
        2. Uncomment the $JAVA_HOME variable (i.e., remove the # sign) and add the full path to the OpenJDK installation on your system. If you have installed the  same version as presented in the first part of this tutorial, add the following line:
            ```bash
            export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
            ```
            ![env-sh](/Images/env-sh.png)
        
    - Edit core-site.xml File
        1. Open the core-site.xml file in a text editor:
            ```bash
            nano $HADOOP_HOME/etc/hadoop/core-site.xml
            ```
        2. Add the following configuration to override the default values for the temporary directory and add your HDFS URL to replace the default local file system setting:
            ![core-site](/Images/core-site.png) 

    - Edit hdfs-site.xml File
        1. Use the following command to open the hdfs-site.xml file for editing:
            ```bash
            sudo nano $HADOOP_HOME/etc/hadoop/hdfs-site.xml
            ```
        2. Add the following configuration to the file and, if needed, adjust the NameNode and DataNode directories to your custom locations:
            ![hdfs-site](/Images/hdfs-site.png)
    
    - Edit mapred-site.xml File
        1. Use the following command to access the mapred-site.xml file and define MapReduce values:
            ```bash
            sudo nano $HADOOP_HOME/etc/hadoop/mapred-site.xml
            ```
        2. Add the following configuration to change the default MapReduce framework name value to yarn:
            ![map-site](/Images/map-site.png)

    - Edit yarn-site.xml File
        1. Open the yarn-site.xml file in a text editor:
            ```bash
            nano $HADOOP_HOME/etc/hadoop/yarn-site.xml
            ```
        2. Append the following configuration to the file:
            ![yarn-site](/Images/yarn-site.png)

   - Format HDFS NameNode
     ```bash
     hdfs namenode -format
     ```

     ![format](/Images/format.png)

   - Start Hadoop:
     ```bash
     $HADOOP_HOME/sbin/start-dfs.sh
     $HADOOP_HOME/sbin/start-yarn.sh
     ```

     ![start-hadoop](/Images/start-hadoop.png)

2. **Prepare Dataset**:
   - Place the dataset CSV file (e.g., `diabetes.csv`) in HDFS:
     ```bash
     hdfs dfs -mkdir /input
     hdfs dfs -put diabetes.csv /input
     ```

3. **Build the Project**:
   - Create a Maven project with the following `pom.xml`:
     ```xml
     <project>
       <groupId>com.example</groupId>
       <artifactId>diabetes-mapreduce</artifactId>
       <version>1.0</version>
       <dependencies>
         <dependency>
           <groupId>org.apache.hadoop</groupId>
           <artifactId>hadoop-common</artifactId>
           <version>3.3.6</version>
         </dependency>
         <dependency>
           <groupId>org.apache.hadoop</groupId>
           <artifactId>hadoop-mapreduce-client-core</artifactId>
           <version>3.3.6</version>
         </dependency>
       </dependencies>
     </project>
     ```
   - Compile the code:
     ```bash
     mvn clean package
     ```

4. **Run the MapReduce Job**:
   - Execute the job:
     ```bash
     hadoop jar target/DiabetesAnalysis-1.0-SNAPSHOT.jar DiabetesMapReduce /input/diabetes.csv /output/diabetes_analysis
     ```
     
     ![running-result](/Images/runing-result.jpg)
   - Retrieve results:
     ```bash
     hdfs dfs -cat /output/diabetes_analysis/part-r-00000
     ```

## Output
- **Format**: `gender_race<TAB>average_HbA1c`
- **Example**:
    ![output](/Images/output.jpg)
    
    ![data-node](/Images/data-node.jpg)