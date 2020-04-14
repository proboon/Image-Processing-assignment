
import java.io.*;

public class AnalysisImage {

    public static void main(String[] args) {
        int value;
        int count = 1; //count column of image data
        int k = 0;

        try {
            File file = new File("b.tif");
            FileInputStream myInputFile = new FileInputStream(file);
            String[] arr = new String[myInputFile.available()];

            //store value into array(string)
            while ((value = myInputFile.read()) != -1) {
                arr[k++] = Integer.toHexString(value);
            }

            //make each byte two digit
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].length() == 1) {
                    arr[i] = 0 + arr[i];
                }
            }

            // or file.getName() to get the name of the file
            System.out.println("File name\t:" + file.toString() + "\n");

            //header info
            String order = arr[0] + arr[1];
            String version = arr[3] + arr[2];
            String offset = arr[7] + arr[6] + arr[5] + arr[4];

            System.out.print("-----------------------------");
            System.out.println("Header Info-----------------------------");
            System.out.println("Byte order\t:" + order);
            System.out.println("Version\t\t:" + version.replaceFirst("^0+(?!$)", ""));
            System.out.println("Offset\t\t:" + offset.replaceFirst("^0+(?!$)", ""));
            System.out.println("");

            //ifd data
            String m = arr[9] + arr[8];//m=10(hex)
            int m1 = Integer.parseInt(m, 16);//hex to dec  //m1=16(dec)
            int totalDE = m1 * 12;
            int sizeIFD = totalDE + 2;
//            int offset1 = Integer.parseInt(arr[8 + sizeIFD + 6 - 1] + arr[8 + sizeIFD + 5 - 1]);

            System.out.print("-----------------------------");
            System.out.println("IFD Data-------------------------------");
            System.out.println("Total DE\t\t:" + totalDE);
            System.out.println("Size of IFD\t\t:" + sizeIFD);
//            System.out.println("Consecutive Offset IFD\t:" + offset1);
            System.out.println("");

            //data entry
            String[] arrDE = new String[totalDE];//array of DE
            int u = 0; //count arrDE

            //array of data entry
            for (int i = 10; i < totalDE + 10; i++) {
                arrDE[u++] = arr[i];
            }
            String[] arrTag = new String[m1];
            String[] arrType = new String[m1];
            String[] arrLength = new String[m1];
            String[] arrValue = new String[m1];
            String type = "";
            String tag = "";
            int stripOffsets = 0;

            for (int i = 0; i < m1; i++) {
                arrTag[i] = arrDE[1 + 12 * i] + arrDE[0 + 12 * i];
                arrType[i] = arrDE[3 + 12 * i] + arrDE[2 + 12 * i];
                arrLength[i] = arrDE[7 + 12 * i] + arrDE[6 + 12 * i] + arrDE[5 + 12 * i] + arrDE[4 + 12 * i];
                arrValue[i] = arrDE[11 + 12 * i] + arrDE[10 + 12 * i] + arrDE[9 + 12 * i] + arrDE[8 + 12 * i];
            }

            System.out.print("-----------------------------");
            System.out.println("Data Entry-----------------------------");
            System.out.println("Tag					Type		Count	Value");
            System.out.println("---------------------------------------------------------------------");

            for (int i = 0; i < arrTag.length; i++) {
                if (Integer.parseInt(arrTag[i], 16) == 273) {
                    stripOffsets = Integer.parseInt(arrValue[i], 16);
                }
                switch (Integer.parseInt(arrTag[i], 16)) {
                    case 254:
                        tag = " (New sub file type)\t\t\t";
                        break;
                    case 256:
                        tag = " (Image width)\t\t\t";
                        break;
                    case 257:
                        tag = " (Image height)\t\t\t";
                        break;
                    case 258:
                        tag = " (Bits per sample)\t\t\t";
                        break;
                    case 259:
                        tag = " (Compression)\t\t\t";
                        break;
                    case 262:
                        tag = " (Photometric interpretation)\t";
                        break;
                    case 273:
                        tag = " (Strip offsets)\t\t\t";
                        break;
                    case 277:
                        tag = " (Samples per pixel)\t\t\t";
                        break;
                    case 278:
                        tag = " (Rows per strip)\t\t\t";
                        break;
                    case 279:
                        tag = " (Strip byte counts)\t\t\t";
                        break;
                    case 282:
                        tag = " (X resolution)\t\t\t";
                        break;
                    case 283:
                        tag = " (Y resolution)\t\t\t";
                        break;
                    case 296:
                        tag = " (Resolution unit)\t\t\t";
                        break;
                    default:
                        tag = " (unknown)\t\t\t\t";
                        break;
                }
                switch (Integer.parseInt(arrType[i], 16)) {
                    case 1:
                        type = "(BYTE)\t\t";
                        break;
                    case 2:
                        type = "(ASCII)\t";
                        break;
                    case 3:
                        type = "(SHORT)\t";
                        break;
                    case 4:
                        type = "(LONG)\t\t";
                        break;
                    case 5:
                        type = "(RATIONAL)\t";
                        break;
                    default:
                        type = "(UNKNOWN)\t";
                        break;
                }
                System.out.println(arrTag[i].replaceFirst("^0+(?!$)", "")
                        + tag + arrType[i].replaceFirst("^0+(?!$)", "") + type
                        + arrLength[i].replaceFirst("^0+(?!$)", "") + "\t"
                        + Integer.parseInt(arrValue[i], 16));
            }

            //image data
            System.out.println("");
            System.out.print("-----------------------------");
            System.out.println("Image  Data-----------------------------");
            for (int i = stripOffsets; i < arr.length; i++) {
                System.out.printf("%3s", arr[i]);
                if (count % 8 == 0) {
                    System.out.print(" ");
                }
                if (count % 16 == 0) {
                    System.out.println(" ");
                }
                count++;
            }
            myInputFile.close();

        } catch (IOException ex) {
            System.out.println("File error!");
        }
    }
}
