import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        Vector<Vector<String>> myList = file_scan_data("myListEdited.txt");
        if (myList == null) myList = file_scan_data("myList.txt");

        Vector<Vector<String>> myListComplete = file_scan_data("myListComplete.txt");
        runMenu(myList, myListComplete);
    }
    public static Vector<Vector<String>> file_scan_data(String filename){
        Vector<Vector<String>> myList = new Vector<Vector<String>>();
        try {
            File myListFile = new File(filename);
            Scanner myReader = new Scanner(myListFile);
            int category_index = -1;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.isBlank())
                {
                    if (data.charAt(0) == '-')
                    {
                        myList.add(new Vector<String>());
                        category_index++;
                        myList.get(category_index).add(data);
                        continue;
                    }
                    myList.get(category_index).add(data);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(RED + "Brak listy." + RESET);
            return null;
        }
        return myList;
    }
    public static void list_print_all(Vector<Vector<String>> myList) {
        for (int i = 0; i < myList.size(); i++)
        {
            printCategory(myList, i);
        }
    }
    public static void printCategory(Vector<Vector<String>> myList, int category_index) {
        for (int j = 0; j < myList.get(category_index).size(); j++)
        {
            if (j==0)
            {
                System.out.println("\n" + YELLOW + (category_index+1) + RESET + " " + BLUE_UNDERLINED + myList.get(category_index).get(j) + "-" + RESET);
                continue;
            }
            System.out.println(CYAN + myList.get(category_index).get(j) + RESET);
        }
    }
    public static void printCategory_indexed_products(Vector<Vector<String>> myList, int category_index) {
        for (int j = 0; j < myList.get(category_index).size(); j++)
        {
            if (j==0)
            {
                System.out.println("\n" + BLUE_UNDERLINED + myList.get(category_index).get(j) + "-" + RESET);
                continue;
            }
            System.out.println(YELLOW + j + RESET + " " + CYAN + myList.get(category_index).get(j) + RESET);
        }
    }
    public static void list_print_categories_only(Vector<Vector<String>> myList) {
        for (int i = 0; i < myList.size(); i++)
        {
            System.out.println(YELLOW + (i+1) + " " + RESET + BLUE + myList.get(i).get(0) + "-" + RESET);
        }
    }
    public static int getCategoryIndex(Vector<Vector<String>> myList) {
        list_print_categories_only(myList);

        System.out.println("Wybierz kategorie poprzez wpisanie odpowiedniej cyfry:");
        int index;
        while (true) {
            index = myScanInt();
            if (index==0 || index > myList.size())
            {
                System.out.println(RED + "Nieprawidlowy numer kategorii! Sprobuj ponownie: " + RESET);
            }
            else
            {
                break;
            }
        }
        return index;
    }
    public static int getProductIndex(Vector<Vector<String>> myList, int index) {
        System.out.println("Wybierz numer produktu, ktory chcesz dodac do listy zakupow: ");
        int productnumber;
        while (true) {
            productnumber = myScanInt();
            if (productnumber < 1 || productnumber >= myList.get(index-1).size())
            {
                System.out.println(RED + "Nieprawidlowy numer produktu! Sprobuj ponownie: " + RESET);
            }
            else
            {
                break;
            }
        }
        return productnumber;
    }
    public static void addProduct(Vector<Vector<String>> myList, Vector<Vector<String>> myListComplete) {
        int index = getCategoryIndex(myList);
        printCategory_indexed_products(myListComplete, (index-1));

        int productnumber = getProductIndex(myListComplete, index);
        String product = myListComplete.get(index-1).get(productnumber);
        myList.get(index-1).add(product);
    }
    public static void deleteProduct(Vector<Vector<String>> myList) {
        int index = getCategoryIndex(myList);
        printCategory_indexed_products(myList, (index-1));

        int productnumber = getProductIndex(myList, index);
        myList.get(index-1).remove(productnumber);
    }
    public static int myScanInt(){
        int action;
        Scanner scan = new Scanner(System.in);
        while (true)
        {
            try {
                action = scan.nextInt();
                scan.nextLine();
                break;
            }
            catch (java.util.InputMismatchException e)
            {
                System.out.println(RED + "Niepoprawny typ danych! Spróbuj ponownie:" + RESET);
                scan.nextLine();
            }
        }
        return action;
    }
    public static void deleteAllProducts(Vector<Vector<String>> myList) {
        for (int i = 0; i < myList.size(); i++)
        {
            deleteCategoryProducts(myList, i);
        }
    }
    public static void deleteCategoryProducts(Vector<Vector<String>> myList, int category_index) {
        int size = myList.get(category_index).size();
        for (int j = 1; j < size; j++)
        {
            myList.get(category_index).remove(1);
        }
    }
    public static void createFile(String filename) {
        try {
            File plik = new File(filename);
            if (plik.createNewFile()) {
                System.out.println(GREEN + "Plik " + plik.getName() + " zostal utworzony!" + RESET);
            } else {
                PrintWriter writer = new PrintWriter(plik);
                writer.print("");
                writer.close();
                System.out.println(GREEN + "Plik juz istnieje!" + RESET);
            }
        } catch (IOException e) {
            System.out.println(RED + "Wystapil problem!" + RESET);
            e.printStackTrace();
        }
    }
    public static void fileCreator(Vector<Vector<String>> myList) {
        createFile("myListEdited.txt");

        try {
            FileWriter fscan = new FileWriter("myListEdited.txt");
            for (int i = 0; i < myList.size(); i++)
            {
                for (int j = 0; j < myList.get(i).size(); j++)
                {
                    String data = myList.get(i).get(j);
                    fscan.write(data + "\n");
                }
                fscan.write("\n");
            }
            fscan.close();
            System.out.println(GREEN + "Zapis zostal wykonany pomyslnie!" + RESET);
        } catch (IOException e) {
            System.out.println(RED + "Wystapil blad podczas zapisu pliku." + RESET);
            e.printStackTrace();
        }
    }
    public static void printMenu() {
        System.out.println("\n\n------------------------");
        System.out.println(YELLOW + "1" + PURPLE + " - Wyswietl wszystkie produkty z listy zakupow");
        System.out.println(YELLOW + "2" + PURPLE + " - Wyswietl wszystkie produkty z listy zakupow z danej kategorii");
        System.out.println(YELLOW + "3" + PURPLE + " - Dodaj produkt do listy zakupow");
        System.out.println(YELLOW + "4" + PURPLE + " - Usun wszystkie produkty z listy zakupow");
        System.out.println(YELLOW + "5" + PURPLE + " - Usun wszystkie produkty z listy zakupow z danej kategorii");
        System.out.println(YELLOW + "6" + PURPLE + " - Usun wybrany produkt z listy zakupow");
        System.out.println(YELLOW + "7" + PURPLE + " - Zapisz liste zakupow na dysku");
        System.out.println(YELLOW + "8" + PURPLE + " - Przywroc poczatkowa liste zakupow");
        System.out.println(YELLOW + "0" + PURPLE + " - Zakoncz program" + RESET);
        System.out.println("------------------------\n");
        System.out.println("Podaj cyfre akcji ktora chcesz wykonac:");
    }
    public static void runMenu(Vector<Vector<String>> myList, Vector<Vector<String>> myListComplete) {
        while (true)
        {
            printMenu();
            Scanner scan = new Scanner(System.in);
            int action;

            while (true)
            {
                try {
                    action = scan.nextInt();
                    scan.nextLine();
                    if (action<0 || action>8)
                    {
                        System.out.println(RED + "Nieprawidlowy numer akcji! Sprobuj ponownie: " + RESET);
                        continue;
                    }
                    break;
                }
                catch (java.util.InputMismatchException e)
                {
                    System.out.println(RED + "Niepoprawny typ danych! Spróbuj ponownie:" + RESET);
                    scan.nextLine();
                }
            }

            switch (action)
            {
                case 0:
                    System.out.println(GREEN + "Koniec programu!" + RESET);
                    return;
                case 1:
                    list_print_all(myList);
                    break;
                case 2:
                    int category_index;
                    list_print_categories_only(myList);
                    System.out.println("Podaj numer kategorii:");
                    while (true) {
                        category_index = myScanInt();
                        if (category_index>0 && category_index <= myList.size())
                        {
                            printCategory(myList, (category_index-1));
                            break;
                        }
                        else
                        {
                            System.out.println(RED + "Nieprawidlowy numer kategorii! Sprobuj ponownie: " + RESET);
                        }
                    }
                    break;
                case 3:
                    addProduct(myList, myListComplete);
                    System.out.println(GREEN + "Produkt zostal dodany!" + RESET);
                    break;
                case 4:
                    deleteAllProducts(myList);
                    System.out.println(GREEN + "Produkty zostaly usuniete!" + RESET);
                    break;
                case 5:
                    int category_index2;
                    list_print_categories_only(myList);
                    System.out.println("Podaj numer kategorii:");
                    while (true) {
                        category_index2 = myScanInt();
                        if (category_index2>0 && category_index2 <= myList.size())
                        {
                            deleteCategoryProducts(myList, (category_index2-1));
                            System.out.println(GREEN + "Produkty z kategorii zostaly usuniete!" + RESET);
                            break;
                        }
                        else
                        {
                            System.out.println(RED + "Nieprawidlowy numer kategorii! Sprobuj ponownie: " + RESET);
                        }
                    }
                    break;
                case 6:
                    deleteProduct(myList);
                    System.out.println(GREEN + "Produkt zostal usuniety!" + RESET);
                    break;
                case 7:
                    fileCreator(myList);
                    break;
                case 8:
                    myList = file_scan_data("myList.txt");
                    System.out.println(GREEN + "Lista przywrocona pomyslnie!" + RESET);
                    break;
            }

        }
    }


    public static final String RESET = "\033[0m";  // Text Reset
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
}
