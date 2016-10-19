/**
 * Print out total number of babies born, as well as for each gender, in a given CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import edu.duke.DirectoryResource;
import edu.duke.FileResource;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BabyBirths {
    
    private <T> void pl(T t) {System.out.println(t);}
	
    public void totalBirths(FileResource fr) {
    
        CSVParser parser = fr.getCSVParser(false); 
        
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        
        for (CSVRecord record : parser) {
            
            int births =  Integer.parseInt(record.get(2));
            totalBirths += births;
            
            if (record.get(1).equals("M")) totalBoys += births;
            else totalGirls += births;
        
        }
        
       pl("Total births: " + totalBirths);
       pl("Girls births: " + totalGirls);
       pl("Boys births: " + totalBoys);
    }
    
    
    /**
     * print the number of unique girls names , 
     * the number of unique boys names and the total names in the selected file.
    */
    public void totalBirths() {
    
        FileResource fr = new FileResource("C:/Users/sg0952655/Desktop/Solving Problems with Software/week4/Data zip/quiz/us_babynames_by_year/yob1905.csv");
        totalBirths(fr);
        
    }
    
    
    /**
     * Helper factory for crating CSVParser objects
     * @param   year    year identitying the file
     * @return  CSVParser from file
    */
    private CSVParser parserFactory(int year) {
        
        final String path =  "C:/Users/sg0952655/Desktop/Solving Problems with Software/week4/Data zip/quiz/us_babynames_by_year/";
        String fileName = path+"yob"+year+".csv";
        FileResource fr = new FileResource(fileName);
        return fr.getCSVParser(false);
        
    }
    
     
    /**
     * This method returns the rank of the name in the file for the given gender, 
     * where rank 1 is the name with the largest number of births.
     * @param   year    year, identifying the file to be searched
     * @param   name    name to be ranked
     * @param   gender  gender to be ranked
     * @return  rank of the given name in a given year/gender
     *          -1 if not found
    */
    public int getRank(int year, String name, String gender) {
        
       CSVParser parser = parserFactory(year);
       return getRank(parser, name, gender);
 
    }
    
    
    /**
     * This method returns the rank of the name in the file for the given gender, 
     * where rank 1 is the name with the largest number of births.
     * @param   parser  CSVParser for file to be searched
     * @param   name    name to be ranked
     * @param   gender  gender to be ranked
     * @return  rank of the given name in a given year/gender
     *          -1 if not found
    */
    public int getRank(CSVParser parser, String name, String gender) {
    
    int rank = 0;
       
       for (CSVRecord record : parser) {
           
           if (record.get(1).equals(gender)) {
               
               rank++;
               // stop counting rank if name is found
               if (record.get(0).equals(name)) {
                   
                   return rank;
                }
            }
        }
    
        return -1;
    }
    

    public void testgetRank() {
        int year = 1971;
        String name = "Frank";
        String gender = "M";
        int rank = getRank(year, name, gender);
        pl("rank: " + rank);
        
    }
    
    
    /**Write the method named getName that has three parameters: an integer named year, an integer named rank, 
    *and a string named gender (F for female and M for male). This method returns the name of the person in the file at this rank, 
    *for the given gender, where rank 1 is the name with the largest number of births.
    *If the rank does not exist in the file, then “NO NAME” is returned.
    */
    
     public String getName(int year, int rank, String gender) {
        
        CSVParser parser = parserFactory(year);
        
        for (CSVRecord record : parser) {
        
            // skip if wrong gender
            if (!record.get(1).equals(gender)) continue;
            
            // return name if found matching rank
            String name = record.get(0);
            if (rank == getRank(year, name, gender)) return name;  
            
        }
        
        // return if rank not found
        return "NO NAME";
    }
    
     public void testgetName() {
        int year = 1982;
        int rank = 450;
        String gender = "M";
        String name = getName(year, rank, gender);
        pl("name: " + name);
        
    }
    
    
    
    /**
     *This method determines what name would have been named if they were born in a different year, based on the same popularity. 
     *That is, you should determine the rank of name in the year they were born, and then print the name born in newYear that is at the same rank and same gender. 
     *For example, using the files "yob2012short.csv" and "yob2014short.csv", notice that in 2012 Isabella is the third most popular girl's name. 
     *If Isabella was born in 2014 instead, she would have been named Sophia, the third most popular girl's name that year. 
     *The output might look like this:
     *Isabella born in 2012 would be Sophia if she was born in 2014.
    */
     public void whatIsNameInYear(String name, int year, int newYear, String gender) {
        int rank = getRank(year, name, gender);
        String Name = getName(newYear, rank, gender);
        pl(name+" born in "+year+" would be "+Name+" if she was born in "+newYear+".");        
    }
     
    public void testwhatIsNameInYear() {
        String name = "Owen";
        int year= 1974;
        int newYear= 2014;
        String gender="M";
        whatIsNameInYear(name, year, newYear, gender);
    }
    
    
    /**
     * This method selects a range of files to process and returns an integer, 
     * the year with the highest rank for the name and gender.
     * @param   name    name to be ranked over files
     * @param   gender  gender to be ranked
     * @return  highest rank for a given name over selected files
    */
    public int yearOfHighestRank(String name, String gender) {
    
        DirectoryResource dr = new DirectoryResource();
        int highestSoFar = -1;
        int yearOfRank = -1;
        String fileName;
        
        for (File file : dr.selectedFiles()) {
            
            String fName = file.getName();
            int year = Integer.parseInt(fName.substring(3, 7));
            int rank = getRank(year, name, gender);
            
           
            // continue if rank not found in file
            if (rank == -1) continue;
            
            // for the first found rank
            if (highestSoFar == -1) {
            
                highestSoFar = rank;
                yearOfRank = year;
            }
            
            // for next ranks 
            // check if new rank is higher(lower is higher) than current highest
            if (highestSoFar > rank) {
                highestSoFar = rank;
                yearOfRank = year;
            }
            
            
        }
        
        return yearOfRank; 
    }
    
    public void testyearOfHighestRank() {
        int year;
        year = yearOfHighestRank("Mich", "M");
        pl("yearOfHighestRank: "+year);
    }
    
    
    /**
    *Write the method getAverageRank that has two parameters: a string name, and a string named gender (F for female and M for male).
    *This method selects a range of files to process and returns a double representing the average rank of the name and gender over the selected files. 
    *It should return -1.0 if the name is not ranked in any of the selected files.
    */
    
    public double getAverageRank(String name, String gender ){
        DirectoryResource dr = new DirectoryResource();
        double Average = -1;
        double total_rank = 0.0;
        double count_rank = 0.0;
        for (File file : dr.selectedFiles()) {
            
            String fName = file.getName();
            int year = Integer.parseInt(fName.substring(3, 7));
            int rank = getRank(year, name, gender);
            
           
            // continue if rank not found in file
            if (rank == -1) continue;
            
            count_rank++;
            total_rank = total_rank + rank;
        }
        if(total_rank != 0 && count_rank != 0)Average = total_rank/count_rank;
        return Average;
    }
    
    public void testgetAverageRank(){
        double Average = getAverageRank("Robert", "M" );
        pl("AverageRank: "+ Average);
    }
    
    
    /**
    *Write the method getTotalBirthsRankedHigher that has three parameters: an integer named year, a string named name, and a string named gender (F for female and M for male).
    *This method returns an integer, the total number of births of those names with the same gender and same year who are ranked higher than name.
    */
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
    
        int nameRank = getRank(year, name, gender);
        CSVParser parser = parserFactory(year);
        int totalBirths = 0;
        for (CSVRecord record : parser) {
            // skip other gender
            if (!record.get(1).equals(gender)) continue;
            
            String recordName = record.get(0);
            int recordNameRank = getRank(year, recordName, gender);
            if (recordNameRank < nameRank) {
                totalBirths += Integer.parseInt(record.get(2));
            }
            else break;
        }
        
        
        return totalBirths;
    
    }
    
    public void testgetTotalBirthsRankedHigher(){
        int totalBirths = getTotalBirthsRankedHigher(1990, "Drew", "M");
        pl("totalBirths Before the name: "+ totalBirths);
    }
}
