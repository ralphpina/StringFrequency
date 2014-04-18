StringFrequency
===============

Class that implements a method that returns a list of number n ordered by the numbers of appearances of the most common n words.

To run this file you would edit StringFrequencyExample to change the number of words you would like the list to return.

Then you would do: java StringFrequencyExample test_docs/war_and_peace.txt
I am using war_and_peace.txt, but you can pass it any other txt file.

To run the tests you would execute in the main directory: java -cp .:junit-4.11.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore StringFrequencyTest

