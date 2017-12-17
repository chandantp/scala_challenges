Assumptions
-----------
* Tax rates are only provided for financial year 2017-18, hence all period dates are assumed to be in the same year.
* Financial year 2017-18 ranges from 01 July 2017 - 30 June 2018
* **_PayPeriod_** input field is of the format **_Start Date (day,month) - End date (day,month)_**
  * As the year is not specified it is calculated as follows:,
    * if **_Month == July - Dec_**, **_Year = 2017_**
    * if **_Month == Jan - June_**, **_Year = 2018_**
  * **_PayPeriod_** is invalid...
    * if it is not of the form **_Day Month - Day Month_**
    * if **_Day_** is invalid
    * if **_Month_** is invalid 
    * if **_StartDate_** > **_EndDate_** as per 2017-18 financial year 
  * Only full month names allowed

Notes
-----
- The application is written in Scala
- Input is from STDIN with Ctrl-D signaling the end of input
- Output is to STDOUT
- If the payment period spans more than a month, pay slip records are generated for each month in that period (see sample run for details)
- If the payment start/end date is not the first/last day of the month respectively, then only worked days are considered for income calculations (see sample run for details)

Compiling
---------
From project directory, run below command to compile the application

    $ sbt compile

Testing
-------
From project directory, run below command to run test cases

    $ sbt test
    
Packaging
---------
From project directory, run below command to generate application JAR.
This JAR is generated at `target/scala-<version>/payroll_<version>.jar`
    
    $ sbt package

Running the App
---------------

Run the application using either of the below commands
    
    $ sbt run
    $ scala target/scala-2.12/payroll_2.12-0.1.jar
     
The end of input is signalled by Ctrl-D and the output appears in the succeeding line(s).


Sample Execution
----------------
```
$ scala target/scala-2.12/payroll_2.12-0.1.jar 
Enter Payroll Records one per line in CSV format specified below (Ctrl-D for EOI):
FirstName,LastName,AnnualSalary,SuperRate,PayPeriod
David,Rudd,60050,9%,01 March - 30 June
Ryane,Chen,120000,10%,16 September - 01 April
^D
#### Payslips ####:
Name,PayPeriod,GrossIncome,IncomeTax,NetIncome,Super
David Rudd,01:Mar:2018 - 31:Mar:2018,5004,922,4082,450
David Rudd,01:Apr:2018 - 30:Apr:2018,5004,922,4082,450
David Rudd,01:May:2018 - 31:May:2018,5004,922,4082,450
David Rudd,01:Jun:2018 - 30:Jun:2018,5004,922,4082,450
Ryane Chen,16:Sep:2017 - 30:Sep:2017,5000,1335,3665,500
Ryane Chen,01:Oct:2017 - 31:Oct:2017,10000,2669,7331,1000
Ryane Chen,01:Nov:2017 - 30:Nov:2017,10000,2669,7331,1000
Ryane Chen,01:Dec:2017 - 31:Dec:2017,10000,2669,7331,1000
Ryane Chen,01:Jan:2018 - 31:Jan:2018,10000,2669,7331,1000
Ryane Chen,01:Feb:2018 - 28:Feb:2018,10000,2669,7331,1000
Ryane Chen,01:Mar:2018 - 31:Mar:2018,10000,2669,7331,1000
Ryane Chen,01:Apr:2018 - 01:Apr:2018,333,89,244,33
```
 

