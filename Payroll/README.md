Assumptions
-----------
* Input records not containing the required fields are ignored
* Input records containing invalid fields are ignored
* **_Payment Start Date_** input field contains the **_Start Date (day,month)_** and **_End date (day,month)_**
  * All dates fall under 2017-18 financial year (01 July 2017 - 30 June 2018)
  * **_Payment Start Date_** is invalid...
    * if it is not of the form **_Day Month - Day Month_**
    * if **_Month_** is invalid (short month names are not allowed)
    * if **_StartDate_** > **_EndDate_** as per 2017-18 financial year 
* For input records containing extra fields, the input record is valid if required fields are valid. 
The extra fields are ignored.

Notes
-----
- The application is written in Scala
- Input is from STDIN with Ctrl-D signaling the end of input
- Output is to STDOUT
- A single input record can lead to 1 or more output pay slip records depending on the payment duration specified. 
If the payment duration spans more than a month, pay slip records are generated for each month.
- If the payment start/end date is not the first/last day of the month respectively, then only the days 
worked in the month are considered for income calculations.

Compilation
-----------
From project root directory, run following command to compile the app

    $ sbt compile

Tests
-----
From project root directory, run following command to run test cases

    $ sbt test
    
Packaging
---------
From project root directory, run following command to generate the application JAR.
This will generate the application JAR at `target/scala-<version>/payroll_<version>.jar`
    
    $ sbt package

Running the App
---------------

From inside the project directory, run scala command and enter the input one record per line.
The end of input is signalled by Ctrl-D.
The output appears in the succeeding line(s).

    $ scala target/scala-2.12/payroll_2.12-0.1.jar 

