package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class BCC_CalcController {

    @FXML
    private TextField charInput;
    @FXML
    private TextField calcOutput;

    @FXML
    void onBCCCalc(ActionEvent event) {

        //Get the binary values
        ArrayList hexdec_num = hexData();

        /**
         * The string data type of the binaries
         */
        System.out.println("Equivalent Binaries: ");
        List<String> binaryStringList = new ArrayList<>(Arrays.asList());
        for (Object hex : hexdec_num) {
            int dec_num, i = 1, j;
            int bin_num[] = new int[100];

            //Converting hexadecimal to decimal
            dec_num = hexToDecimal(hex.toString());

            //Converting decimal to binary
            while (dec_num != 0) {
                bin_num[i++] = dec_num % 2;
                dec_num = dec_num / 2;
            }

            //Getting the binary string
            StringBuilder bin = new StringBuilder();
            for (j = i - 1; j > 0; j--) {
                bin.append(bin_num[j]);
            }

            //Formatting the binary into 8 bit as string array
            NumberFormat formatBin = new DecimalFormat("00000000");
            String binVal = formatBin.format(Integer.parseInt(bin.toString()));
            binaryStringList.add(binVal);
        }

        /**
         * Converting binary string array into int array
         */
        //The integer data type of the binaries
        List<ArrayList> binaryIntList = new ArrayList<>(Arrays.asList());

        //The number of iterations of binaries (will vary depends on the inputted command)
        int binArrNum = 0;
        for (String binString : binaryStringList) {
            ArrayList<Integer> binArrayList = new ArrayList<>();

            List<String> binArray = new ArrayList<>(Arrays.asList(binString.split("")));
            for (String binChar : binArray) {
                char[] ch = binChar.toCharArray();
                StringBuilder builder = new StringBuilder();
                for (char c : ch) {
                    binArrayList.add(Integer.parseInt(builder.append(c).toString()));
                }
            }
            System.out.println("For HEX_" + hexdec_num.get(binArrNum) + "H: " + binArrayList);
            binArrNum++;
            binaryIntList.add(binArrayList);
        }

        /**
         * XOR Gate and BCC calculation
         */
        ArrayList<Integer> bccVal = new ArrayList<>();
        ArrayList<Integer> xorComp = new ArrayList<>();
        System.out.println("BCC Calculation:");
        for (ArrayList binArr : binaryIntList) {
            if (xorComp.size() == 0) {
                for (int i = 0; i < binArr.size(); i++) {
                    xorComp.add((Integer) binArr.get(i));
                }
            } else {
                bccVal.clear();
                for (int i = 0; i < binArr.size(); i++) {
                    if (xorComp.get(i) == binArr.get(i)) {
                        bccVal.add(0);
                    } else {
                        bccVal.add(1);
                    }
                }

                //Updating the BCC value
                xorComp.clear();
                for (int i = 0; i < bccVal.size(); i++) {
                    xorComp.add((Integer) bccVal.get(i));
                }
            }
            if (bccVal.isEmpty()) {
                System.out.println("BCC: " + "[0, 0, 0, 0, 0, 0, 0, 0]");
            } else {
                System.out.println("BCC: " + bccVal);
            }
        }

        /**
         * Converting BCC to 3 digit decimal
         */
        HashMap<Integer, Integer> hm = new HashMap<>();
        hm.put(0, 128);
        hm.put(1, 64);
        hm.put(2, 32);
        hm.put(3, 16);
        hm.put(4, 8);
        hm.put(5, 4);
        hm.put(6, 2);
        hm.put(7, 1);

        int bccSum = 0;
        try {
            for (Map.Entry m : hm.entrySet()) {
                if (bccVal.get((int) m.getKey()) != 0) {
                    bccSum = bccSum + (int) m.getValue();
                }
            }
        } catch (Exception e) {
            System.out.println("INFO: " + e.getMessage());
        }

        /**
         * Output the BCC calculation result
         */
        NumberFormat formatBCC = new DecimalFormat("000");
        String bccOutput = formatBCC.format(bccSum);
        System.out.println("BCC_Calc: " + bccOutput);
        calcOutput.setText(bccOutput);
    }

    @FXML
    void onCopy(ActionEvent event) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(calcOutput.getText());
        clipboard.setContent(content);
    }


    /**
     * Getting the decimal equivalent of the calculated HEX value
     *
     * @param s
     * @return
     */
    public static int hexToDecimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    } //end of hex_to_decimal

    /**
     * Getting the HEX equivalent of the inputted command code (ascii)
     *
     * @return
     */
    public ArrayList hexData() {
        String input;

        //To list up all the HEX data
        ArrayList<String> hexValue = new ArrayList<>();

        //Get the user input
        input = charInput.getText();

        //Check the number of command characters, if it only contains 1 character, add two zero prefix
        if (input.length() == 1) {
            input = ("000" + input).substring(input.length());
        }

        //Convert ASCII binString to char array
        List<String> hexList = new ArrayList<>(Arrays.asList(input.split("")));

        for (String hexString : hexList) {
            char[] hexChar = hexString.toCharArray();

            //Iterate over char array and cast each element to Integer.
            StringBuilder buildHex = new StringBuilder();
            for (char c : hexChar) {
                int x = (int) c;

                //Convert integer value to hex using toHexString() method.
                buildHex.append(Integer.toHexString(x).toUpperCase());
                hexValue.add(buildHex.toString());
            }
        }
        System.out.println("Hex Value: " + hexValue);
        return hexValue;
    }

}
