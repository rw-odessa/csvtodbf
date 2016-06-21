/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csvtodbf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author ua053903
 */
public class Csvtodbf {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String csvFile, iniFile, dbfFile;
        BufferedReader br = null;
        ArrayList<String> al = new ArrayList();
        //String cvsSplitBy = ", ";
        //String Stru = "FCFFFCDDDDFFCFCFFCFFFFCF";
        //String _Stru = "";

//---------------------------------------------------
// Прочитаем параметры командной строки.
        if (args.length < 2 || args.length > 3) {
            System.out.println("Should by 2 or 3 command line parameter. Got: " + args.length);
            System.out.println("EXEMPLE -");
            System.out.println("java -jar csvtodbf.jar file.csv file.cnfg");
            System.out.println("OR -");
            System.out.println("java -jar csvtodbf.jar file.csv file.cnfg file.dbf");
            System.exit(1);
        }

// Проверка csv файла.        
        csvFile = args[0];
        //csvFile = "D:\\RW\\dbf\\OP4_0603.csv";
        File f = new File(csvFile);
        if (!f.exists()) {
            System.out.println("ERROR csv file - " + csvFile + " not found");
            System.exit(1);
        }
        
// Если параметров передано 2 то имя dbf файла создадим из имени csv файла.
        if (args.length == 2) {
            dbfFile = f.getPath();
            if (dbfFile.substring(dbfFile.length()-3, dbfFile.length()).equals("csv")) {
                dbfFile = dbfFile.substring(0, dbfFile.length()-3) + "dbf";
            }

// Если параметров передано 3 то имя dbf файла это 3-ий параметр.
        } else {
            dbfFile = args[2];
        }
        //System.out.println("dbfFile - " + dbfFile);
        //System.exit(0);

// Проверка ini файла.         
        iniFile = args[1];
        //iniFile = "OP4.ini";
        f = new File(iniFile);
        if (!f.exists()) {
            System.out.println("ERROR config file - " + iniFile + " not found");
            System.exit(1);
        }

//---------------------------------------------------
// Прочитаем все настройки из INI файла.
        csvtodbf.IniFile ini = null;
        try {
            ini = new csvtodbf.IniFile(iniFile);
            //System.out.println("General/Encoding - " + ini.getString("General", "Encoding", "Cp866"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }

// Проверка dbf файла.
        f = new File(dbfFile);
        if (f.exists() && !ini.getBoolean("General", "RewriteExisted", false)) {
            System.out.println("ERROR, Exist Destination DBF file - " + dbfFile);
            System.exit(1);
        }

//---------------------------------------------------
// Прочитаем все строки в ArrayList
        /*
         try {
         br = new BufferedReader(new FileReader(csvFile));
         String line;
         while ((line = br.readLine()) != null) {
         al.add(line);
         //System.out.println(line);
         }
         } catch (FileNotFoundException e) {
         //e.printStackTrace();
         System.out.println("FileNotFoundException - " + e);
         } catch (IOException e) {
         //e.printStackTrace();
         System.out.println("IOException - " + e);
         } finally {
         if (br != null) {
         try {
         br.close();
         } catch (IOException e) {
         //e.printStackTrace();
         System.out.println("IOException - " + e);
         }
         }
         }
         */

//---------------------------------------------------
// Прочитаем все строки в ArrayList с учетом кодировки файла.
        String cvsSplitBy = ini.getString("General", "cvsSpliter", ";").replaceAll("<space>", " ");
        //System.out.println("cvsSplitBy - " + cvsSplitBy + ".");
        try {
            File fileDir = new File(csvFile);
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    //new FileInputStream(fileDir), "Cp866"))) {
                    new FileInputStream(fileDir), ini.getString("General", "CsvEncoding", "Cp866")))) {
                String str;
                while ((str = in.readLine()) != null) {
                    al.add(str);
                    //System.out.println(str);
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException - " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOException - " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Exception - " + e.getMessage());
            System.exit(1);
        }

//---------------------------------------------------
// Опредилим количество строк в ArrayList
        //System.out.println("====================================");
        //System.out.println("Lines count = " + al.size());

//---------------------------------------------------
// Опредилим количество ячеек в строке
        //System.out.println("====================================");
        String[] cell = al.get(0).split(cvsSplitBy);
        //System.out.println("Cell count = " + cell.length);
        //System.out.println("====================================");

//---------------------------------------------------
// Создадим структуру файла DBF
        Object[] record = new Object[cell.length];
        com.hexiong.jdbf.JDBField[] fields = new com.hexiong.jdbf.JDBField[cell.length];

        // Загрузим названия полей.
        // Если название полей в первой строке берем их там.
        if (ini.getBoolean("General", "NameInFirst", false)) {
            cell = al.get(0).split(cvsSplitBy);
        }

        for (int i = 0; i < fields.length; i++) {
            try {
                // Прочитаем структуру полей.
                // Загрузим названия полей.
                // Если название полей не в строке берем их из ini файла.
                if (!ini.getBoolean("General", "NameInFirst", false)) {
                    cell[i] = ini.getString("Column-" + (i + 1), "Name", "Column-" + (i + 1));
                }

                // Укоротим имена полей до 10 символов.
                if (cell[i].length() > 10) {
                    cell[i] = cell[i].substring(0, 9);
                }

                fields[i] = new com.hexiong.jdbf.JDBField(
                        cell[i],
                        ini.getString("Column-" + (i + 1), "Tipe", "C").charAt(0),
                        ini.getInt("Column-" + (i + 1), "Lenght", 8),
                        ini.getInt("Column-" + (i + 1), "FractionLenght", 2));
            } catch (com.hexiong.jdbf.JDBFException ex) {
                //Logger.getLogger(CsvReader.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("JDBFException - " + ex + "\t" + cell[i]);
                System.exit(1);
            }
        }

//---------------------------------------------------
// Запись данных в файл DBF        
        com.hexiong.jdbf.DBFWriter dbfwriter = null;
        SimpleDateFormat date;
        try {
            dbfwriter = new com.hexiong.jdbf.DBFWriter(dbfFile, fields, ini.getString("General", "DBFEncoding", "Cp866"));

            //---------------------------------------------------
            // Разберем строки на ячейки и запишем в файл DBF
            for (int i = 1; i < al.size(); i++) {

                // преобразование строк в необходимые типы данных.

                cell = al.get(i).split(cvsSplitBy);
                //System.arraycopy(cell, 0, record, 0, cell.length);
                for (int k = 0; k < cell.length; k++) {
                    if (cell[k].length() != 0) {
                        if (fields[k].getType() == 'F') {
                            try {
                                record[k] = Double.parseDouble(cell[k]);
                            } catch (NumberFormatException numberFormatException) {
                                System.out.println("NumberFormatException - " + numberFormatException + "\t" + cell[k] + "\tColumn-" + (k + 1));
                                System.exit(1);
                            }
                        } else if (fields[k].getType() == 'N') {
                            try {
                                record[k] = Integer.parseInt(cell[k]);
                            } catch (NumberFormatException numberFormatException) {
                                System.out.println("NumberFormatException - " + numberFormatException + "\t" + cell[k] + "\tColumn-" + (k + 1));
                                System.exit(1);
                            }
                        } else if (fields[k].getType() == 'D') {
                            //record[k] = dateFormat.parse(cell[k]);
                            //System.out.println("IniDateFormat - " + ini.getString("Column-" + (k + 1), "Format", "YYYYMMDD") + "\t" + cell[k]);
                            date = new SimpleDateFormat(ini.getString("Column-" + (k + 1), "Format", "YYYYMMDD"));
                            try {
                                record[k] = date.parse(cell[k]);
                                //System.out.println("DateFormat - " + record[k]);
                            } catch (ParseException ex) {
                                System.out.println("ParseDateException - " + ex + "\t" + cell[k] + "\tColumn-" + (k + 1) + "\t Date format - " + ini.getString("Column-" + (k + 1), "Format", "YYYYMMDD"));
                                System.exit(1);
                            }
                        } else if (fields[k].getType() == 'L') {
                            try {
                                record[k] = Boolean.parseBoolean(cell[k]);
                            } catch (Exception e) {
                                System.out.println("Exception - " + e + "\t" + cell[k] + "\tColumn-" + (k + 1));
                                System.exit(1);
                            }
                        } else if (fields[k].getType() == 'C') {
                            record[k] = cell[k];
                        }
                    } else {
                        record[k] = null;
                    }


                }
                dbfwriter.addRecord(record);
            }

        } catch (com.hexiong.jdbf.JDBFException ex) {
            //Logger.getLogger(CsvReader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("JDBFException - " + ex);
            System.exit(1);
        } finally {
            if (dbfwriter != null) {
                try {
                    dbfwriter.close();
                } catch (com.hexiong.jdbf.JDBFException ex) {
                    //Logger.getLogger(CsvReader.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("JDBFException - " + ex);
                    System.exit(1);
                }
            }
        }

//---------------------------------------------------
// Завершение с положительным результатом.
        //System.out.println("Done");
        System.out.println("File - " + csvFile + " successfully converted to - " + dbfFile);
        System.exit(0);
    }
}
