package com.company;


/*

RawData.txt is the input data to your program. The attached text file below has the raw data. Every line in the file that consists of structured data in the following format.

The Raw data file has many information in it. Our focus for this assignment is restricted to following fields. Basically in this file each row is an event. Each row will have similar type of data with different values init. The line contents are in the form of Key-Value pair.

 

IP Adresss “ip”.
Page Load Time   “PageLoadTime”.
Page Type “pageType”.
 

Each line will have IP Address -    ip#$#190.25.228.161 - the key value separator is #$#.
Each line will have Page Load Time    -  PageLoadTime#$#1748   - the key value separator is #$#.
Each line will have PageType    -  pageType#$#Page   - the key value separator is #$#.
Page Type are three types   - Page, Ajax, Iframe.
 

Expected outcome from the assignment

 

Write a java program to parse the raw data file and produce the output (console log) in below indicated format.
The questions are indicated below.
Unique number of IP addresses
For each IP address, what is the average page load time (grouped by IP address).
For each IP address, the count of Page Types
Summation of All page types across all unique IP – should add to total number of lines.
Unique Number of IP Address (Number)


<<IP Address 1>>  -   Avg Page Load Time is     <<Computed Value>>
Page Type : Page=15
Page Type : Iframe=2
Page Type : Ajax=0
<<IP Address 2>>  -   Avg Page Load Time is    <<Computed Value>>
Page Type : Page=15
Page Type : Iframe=2
Page Type : Ajax=0
Etc.

 

Example console log output  :

 

 

 

Output format should achieve the below indicated functional aspects:

IP Address should be printed in sorted order .
You should only use the standard java collection classes to accomplish the above.
Print the collection and show the output.
*/

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class test {
    public static void main(String[] args) throws IOException {
        File file = new File("C:/Users/Admin/Downloads/RawData.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        InputStreamReader ip = new InputStreamReader(fileInputStream);
        List<String> stringList = new ArrayList<>();


        try (BufferedReader reader = new BufferedReader(ip)) {
            stringList = reader.lines().collect(Collectors.toList());
        }



       List<String> finals= new ArrayList<>();


       for(String s:stringList) {
           int lastpos=0;
           String parse =  s.trim();
           char[] fin = parse.toCharArray();

         if(parse.contains("ip#$#")) {
            List<Character> ch  = new ArrayList<>();
            ch.add('i');
             ch.add('p');
             ch.add('#');
             ch.add('$');
             ch.add('#');
             ch.add('$');
             ch.add('#');


          for(int i=0;i < fin.length-1;i++) {
              if(ch.isEmpty()) {
                  lastpos=i;
                  break;
              }

              if(ch.contains(fin[i])) {

                  char rem=fin[i];

                  int indexx=ch.indexOf(rem);


                  ch.remove(indexx);
              }
          }

         }


           StringBuilder stringBuilder = new StringBuilder();
         //192.169.0.1
           int dot=0;
           int numsiz=0;

           for(int i=lastpos;i< fin.length-1;i++) {

               if(dot==4 && numsiz==3) {
                   break;
               }
               if(fin[i]=='.') {
                   dot++;
               }
               if(dot==4) {
                   if(fin[i]=='1'|| fin[i]=='2'|| fin[i]=='3'|| fin[i]=='4'|| fin[i]=='5'|| fin[i]=='6'|| fin[i]=='7'|| fin[i]=='8'|| fin[i]=='9' || fin[i]=='0' ) {
                       numsiz++;
                   }
               }
               if(fin[i]=='~') {
                   break;
               }
               stringBuilder.append(fin[i]);  // end add it to  list
           }
           finals.add(stringBuilder.toString());



           if(parse.contains("#$#~$~PageLoadTime#$#")) {
               String ss="#$#~$~PageLoadTime#$#";
               char arrm[] = ss.toCharArray();
               int indes=0;

               for(int i=0;i< fin.length;i++) {

                   if(arrm.length==indes) {
                       lastpos=i;
                       break;
                   }
                   if(arrm[indes]==fin[i]) {
                      indes++;
                   }else {
                       indes=0;
                   }
               }


               StringBuilder stringBuilder2 = new StringBuilder();
               for(int i=lastpos;i< fin.length;i++) {

                   if(fin[i]=='~') {
                       break;
                   }
                   stringBuilder2.append(fin[i]);  // end add it to  list
               }

               finals.add(stringBuilder2.toString());
           }

       }

       for(String s :finals) {
           System.out.println(s);
       }


    }

}