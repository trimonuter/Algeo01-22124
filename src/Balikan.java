import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Balikan {
    // Methods
    public static void main(String[] args) throws IOException {
        // Matriks MatriksX = BicubicSplineInterpolation.MatriksX();
        // MatriksX.printMatrix();

        // Matriks Invers = Balikan.BalikanAdjoin(MatriksX);
        // Invers.printMatrix();

        // Open scanner
        Scanner scanner = new Scanner(System.in);
        
        File file = new File("./test/tes.txt");
        Matriks matriks = Matriks.ReadMatrixFromFile(file);

        Matriks invers = BalikanAdjoin(matriks);
        invers.printMatrix();
        invers.SaveMatrixToFile("tes_result.txt");

        // Close scanner
        scanner.close();
    }

    // Driver
    public static void driverBalikan(Scanner scanner) {
        // Header
        App.slowprint("Mencari Matriks Balikan");

        String[] Balikan_choice = {
            "1. Metode Gauss-Jordan",
            "2. Metode Adjoin"
        };
        App.printMenu(Balikan_choice);

        // Ask for input
        int choiceBalikan = App.askInput(1, 2, scanner);
        Matriks m = App.askMatriksInput(scanner, 2);

        // Run code for chosen method
        Matriks inv;
        if (choiceBalikan == 1) {
            inv = BalikanGaussJordan(m);
        } else {
            inv = BalikanAdjoin(m);
        }
        if (inv.row == 0) {
            App.slowprint("Karena nilai determinan dari matriks = 0, maka matriks tidak memiliki balikan.");
        } else {
            App.slowprint("Matriks balikan: ");
            inv.printMatrix();
        }
    }

    // 1. Balikan Gauss-Jordan
    public static Matriks BalikanGaussJordan(Matriks matriks) {
        double det = Determinan.DeterminanOBE(matriks);
        if (det == 0) {
            Matriks m = new Matriks(0, 0);
            return m;
        } else {
            int j;
            Matriks invers = Matriks.MatriksIdentitas(matriks.row);
            for (j = 0; j < matriks.col; j++) {
                if (matriks.matrix[j][j] == 0) {
                    int k;
                    for (k = j; k < matriks.row; k++) {
                        if (matriks.matrix[k][j] != 0) {
                            matriks.OBE(2, j, -1, k);
                            invers.OBE(2, j, -1, k);
                            break;
                        }
                    }
                }
                double divisor = 1 / (double) matriks.matrix[j][j];
                // divisor *= (matriks.matrix[j][j] < 0) ? -1 : 1;
                matriks.OBE(1, j, divisor, -1);
                invers.OBE(1, j, divisor, -1);
    
                int i;
                for (i = 0; i < matriks.row; i++) {
                    if (i != j) {
                        double adder = -1 * matriks.matrix[i][j];
                        matriks.OBE(3, i, adder, j);
                        invers.OBE(3, i, adder, j);
                    }
                }
            }
            int a;
            for (a = 0; a < matriks.row; a++) {
                matriks.OBE(3, a, 0, 1);
                invers.OBE(3, a, 0, 1);
            }
            return invers;
        }
        
    }
    // 2. Balikan Adjoin
    public static Matriks BalikanAdjoin(Matriks matriks) {
        Matriks matKofaktor = Matriks.MatriksKofaktor(matriks);
        Matriks adjoin = Matriks.Transpose(matKofaktor);
        double determinan = Determinan.DeterminanKofaktor(matriks);

        int i, j;
        for (i = 0; i < adjoin.row; i++) {
            for (j = 0; j < adjoin.col; j++) {
                adjoin.matrix[i][j] /= determinan;
            }
        }
        double det = Determinan.DeterminanOBE(matriks);
        if (det != 0) {
            return adjoin;
        } else {
            Matriks m = new Matriks(0, 0);
            return m;
        }
    }
}
