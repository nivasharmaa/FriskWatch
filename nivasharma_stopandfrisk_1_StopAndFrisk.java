import java.util.ArrayList;

/**
 * The StopAndFrisk class represents stop-and-frisk data, provided by
 * the New York Police Department (NYPD), that is used to compare
 * during when the policy was put in place and after the policy ended.
 * 
 * @author Tanvi Yamarthy
 * @author Vidushi Jindal
 */
public class StopAndFrisk {

    /*
     * The ArrayList keeps track of years that are loaded from CSV data file.
     * Each SFYear corresponds to 1 year of SFRecords. 
     * Each SFRecord corresponds to one stop and frisk occurrence.
     */ 
    private ArrayList<SFYear> database; 

    /*
     * Constructor creates and initializes the @database array
     * 
     * DO NOT update nor remove this constructor
     */
    public StopAndFrisk () {
        database = new ArrayList<>();
    }

    /*
     * Getter method for the database.
     * *** DO NOT REMOVE nor update this method ****
     */
    public ArrayList<SFYear> getDatabase() {
        return database;
    }

    /**
     * This method reads the records information from an input csv file and populates 
     * the database.
     * 
     * Each stop and frisk record is a line in the input csv file.
     * 
     * 1. Open file utilizing StdIn.setFile(csvFile)
     * 2. While the input still contains lines:
     *    - Read a record line (see assignment description on how to do this)
     *    - Create an object of type SFRecord containing the record information
     *    - If the record's year has already is present in the database:
     *        - Add the SFRecord to the year's records
     *    - If the record's year is not present in the database:
     *        - Create a new SFYear 
     *        - Add the SFRecord to the new SFYear
     *        - Add the new SFYear to the database ArrayList
     * 
     * @param csvFile
     */
    public void readFile ( String csvFile ) {

        // DO NOT remove these two lines
        StdIn.setFile(csvFile); // Opens the file
        StdIn.readLine();       // Reads and discards the header line

        // WRITE YOUR CODE HERE
        while(StdIn.hasNextLine())
        {        
            boolean dne = true;
            String[] recordEntries = StdIn.readLine().split(",");
            int year = Integer.parseInt(recordEntries[0]);

            String description = recordEntries[2];

            String gender = recordEntries[52];

            String race = recordEntries[66];

            String location = recordEntries[71];

            Boolean arrested = recordEntries[13].equals("Y");

            Boolean frisked = recordEntries[16].equals("Y");
            SFRecord sf = new SFRecord(description, arrested,frisked, gender, race, location);
            
            for(int i = 0; i < database.size();i++){
                if(year == database.get(i).getcurrentYear())
                {
                    database.get(i).addRecord(sf);
                    dne = false;
                }
            }
            if(dne)
            {
                SFYear y = new SFYear(year);
                y.addRecord(sf);
                database.add(y);
            }
        }
        
    }

    /**
     * This method returns the stop and frisk records of a given year where 
     * the people that was stopped was of the specified race.
     * 
     * @param year we are only interested in the records of year.
     * @param race we are only interested in the records of stops of people of race. 
     * @return an ArrayList containing all stop and frisk records for people of the 
     * parameters race and year.
     */

    public ArrayList<SFRecord> populationStopped ( int year, String race ) {

        // WRITE YOUR CODE HERE
        ArrayList<SFRecord> pop = new ArrayList<>();
        for(int i = 0; i < database.size(); i++)
        {
            if(database.get(i).getcurrentYear() == year)
            {
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++)
                {
                    if(database.get(i).getRecordsForYear().get(j).getRace().equals(race))
                    {
                        pop.add(database.get(i).getRecordsForYear().get(j));
                    }
                }
            }
        }
        return pop;
    }

    /**
     * This method computes the percentage of records where the person was frisked and the
     * percentage of records where the person was arrested.
     * 
     * @param year we are only interested in the records of year.
     * @return the percent of the population that were frisked and the percent that
     *         were arrested.
     */
    public double[] friskedVSArrested ( int year ) {
        
        // WRITE YOUR CODE HERE
        double[] percentage = new double[2];
        int countf = 0;
        int counta = 0;
        for(int i = 0; i < database.size(); i++)
        {
            if(database.get(i).getcurrentYear() == year)
            {
                for(int j = 0; j < database.get(i).getRecordsForYear().size(); j++)
                {
                    if(database.get(i).getRecordsForYear().get(j).getFrisked())
                    {
                        countf++;
                    }
                    if(database.get(i).getRecordsForYear().get(j).getArrested())
                    {
                        counta++;
                    }
                }
                percentage[0] = ((double)countf/database.get(i).getRecordsForYear().size())* 100;
                percentage[1] = ((double)counta/database.get(i).getRecordsForYear().size())* 100;
            }
        }
        return percentage; // update the return value
    }

    /**
     * This method keeps track of the fraction of Black females, Black males,
     * White females and White males that were stopped for any reason.
     * Drawing out the exact table helps visualize the gender bias.
     * 
     * @param year we are only interested in the records of year.
     * @return a 2D array of percent of number of White and Black females
     *         versus the number of White and Black males.
     */
    public double[][] genderBias ( int year ) {

        // WRITE YOUR CODE HERE
        double bf = 0;
        double wf = 0;
        double wm = 0;
        double bm = 0;
        double bt = 0;
        double wt = 0;
        double[][] tab = new double[2][3];
        for(int i = 0; i < database.size(); i++)
        {
            
            if(database.get(i).getcurrentYear() == year)
            {
                for(int j = 0; j < database.get(i).getRecordsForYear().size();j++)
                {
                    if(database.get(i).getRecordsForYear().get(j).getRace().equals("B"))
                    {
                        bt++;
                    }
                    if(database.get(i).getRecordsForYear().get(j).getRace().equals("W"))
                    {
                        wt++;
                    }
                    if(database.get(i).getRecordsForYear().get(j).getGender().equals("M"))
                    {
                        if(database.get(i).getRecordsForYear().get(j).getRace().equals("B"))
                        {
                            bm++;
                        }
                        if(database.get(i).getRecordsForYear().get(j).getRace().equals("W"))
                        {
                            wm++;
                        }
                    }
                    if(database.get(i).getRecordsForYear().get(j).getGender().equals("F"))
                    {
                        if(database.get(i).getRecordsForYear().get(j).getRace().equals("B"))
                        {
                            bf++;
                        }
                        if(database.get(i).getRecordsForYear().get(j).getRace().equals("W"))
                        {
                            wf++;
                        }
                    }
                }
                tab[0][0] = ((bf)/(bt))* 0.5* 100;
                tab[0][1] = ((wf)/(wt))* 0.5* 100;
                tab[1][0] = ((bm)/(bt))*0.5* 100;
                tab[1][1] = ((wm)/(wt))*0.5* 100;
                tab[0][2] = (tab[0][0]+tab[0][1]);
                tab[1][2] = (tab[1][0]+tab[1][1]);

            }
        }
        return tab; // update the return value
    }

    /**
     * This method checks to see if there has been increase or decrease 
     * in a certain crime from year 1 to year 2.
     * 
     * Expect year1 to preceed year2 or be equal.
     * 
     * @param crimeDescription
     * @param year1 first year to compare.
     * @param year2 second year to compare.
     * @return 
     */
    public double crimeIncrease ( String crimeDescription, int year1, int year2 ) {
        
        // WRITE YOUR CODE HERE
        double year1crimes=0;
        double year2crimes =0;
        double year1percentage=0;
        double year2percentage =0;
        for ( int i = 0; i < database.size(); i++) {
            if ( database.get(i).getcurrentYear()==year1)
            {
                ArrayList<SFRecord> darktimes = database.get(i).getRecordsForYear();
                for ( int j = 0; j < darktimes.size(); j++) {
                    if(darktimes.get(j).getDescription().indexOf(crimeDescription)!=-1){
                            year1crimes++;
                    }
                }
                year1percentage = year1crimes/darktimes.size();
            }
            if ( database.get(i).getcurrentYear()==year2)
            {
                ArrayList<SFRecord> lopez = database.get(i).getRecordsForYear();
                for ( int k = 0; k < lopez.size(); k++) {
                    if(lopez.get(k).getDescription().indexOf(crimeDescription)!=-1){
                            year2crimes++;
                    }
                }
               year2percentage = year2crimes/lopez.size();
            }
        }

        return (year2percentage-year1percentage)*100;  // update the return value
    }

    /**
     * This method outputs the NYC borough where the most amount of stops 
     * occurred in a given year. This method will mainly analyze the five 
     * following boroughs in New York City: Brooklyn, Manhattan, Bronx, 
     * Queens, and Staten Island.
     * 
     * @param year we are only interested in the records of year.
     * @return the borough with the greatest number of stops
     */
    public String mostCommonBorough ( int year ) {

        // WRITE YOUR CODE HERE
        int brooklyn=0;
        int manhattan=0;
        int bronx=0;
        int queens=0;
        int statenisland=0;
        for ( int i = 0; i < database.size(); i++) {
            if ( database.get(i).getcurrentYear() == year ) {
                ArrayList<SFRecord> darktimes = database.get(i).getRecordsForYear();
                for ( int j = 0; j < darktimes.size(); j++ ) {
                    if ( darktimes.get(j).getLocation().equalsIgnoreCase("Brooklyn")) {
                        brooklyn++;
                    }
                    if ( darktimes.get(j).getLocation().equalsIgnoreCase("Manhattan")) {
                        manhattan++;
                    }
                    if ( darktimes.get(j).getLocation().equalsIgnoreCase("Bronx")) {
                        bronx++;
                    }
                    if ( darktimes.get(j).getLocation().equalsIgnoreCase("Queens")) {
                        queens++;
                    }
                    if ( darktimes.get(j).getLocation().equalsIgnoreCase("Staten Island")) {
                        statenisland++;
                    }
                }

            }

        }
        ArrayList<Integer> population = new ArrayList<Integer>();
        ArrayList<String> boroughs = new ArrayList<String>();

        population.add(brooklyn);
        population.add(manhattan);
        population.add(bronx);
        population.add(queens);
        population.add(statenisland);
        boroughs.add("Brooklyn");
        boroughs.add("Manhattan");
        boroughs.add("Bronx");
        boroughs.add("Queens");
        boroughs.add("Staten Island");
        int max=0;
        int maxValue=-1;
        for(int k =0; k<population.size();k++)
        {
            if(population.get(k)>max)
            {
                max = population.get(k);
                maxValue = k;

            }
        }
        return boroughs.get(maxValue); // update the return value
        }

}
