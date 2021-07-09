import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class GeekTrust {

	private static final HashMap<String, String> KINGDOM_VS_EMBLEM = new HashMap<>();
    private static final int ALPHABETS_COUNT = 26, MIN_ALLIES_SUPPORT = 3;

    static {
        // initializing kingdoms versus emblems in a hashmap
        KINGDOM_VS_EMBLEM.put("LAND", "PANDA");
        KINGDOM_VS_EMBLEM.put("WATER", "OCTOPUS");
        KINGDOM_VS_EMBLEM.put("ICE", "MAMMOTH");
        KINGDOM_VS_EMBLEM.put("AIR", "OWL");
        KINGDOM_VS_EMBLEM.put("FIRE", "DRAGON");
    }

    public static void main(String[] args) {
        try {
            if (args.length < 1 ) {
                System.out.println("No input file");
                return;
            }

            String currentWorkingDirectory = System.getProperty("user.dir");
            String filePath=currentWorkingDirectory+"\\"+args[0] ;

            // read the input from a text file
            FileReader file = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(file);

            String inputLineStr;
            Set<String> allies = null;
            while ((inputLineStr = reader.readLine()) != null) {

                inputLineStr = inputLineStr.toUpperCase().trim() ;

                // split the input line into 2 parts
                // first part is kingdom name and second part is secret message
                String[] inputLineStrArr = inputLineStr.split(" ",2);

                // if the Kingdom is same as SPACE than it will not count as won kingdom 
                if( inputLineStrArr[0].equals("SPACE") ){
                    continue;
                }

                // get the cipher key for the input kingdom
                int cipherKey = KINGDOM_VS_EMBLEM.get(inputLineStrArr[0]).length();

                String secretMessage = inputLineStrArr[1];
                // Decrypt the input message using the cipher key
                String decryptedMessage = getDecryptedMessage(secretMessage,cipherKey) ;
                
                // get the correspond emblem from from static KINGDOM_VS_EMBLEM map
                String emblem = KINGDOM_VS_EMBLEM.get(inputLineStrArr[0]);
                
                // get the frequency of each character in string 
                HashMap<Character,Integer> emblemFrequency=countCharacterFrequency(emblem) ;
                HashMap<Character,Integer> decryptedMessageFrequency=countCharacterFrequency(decryptedMessage) ;

                // check weather decryptedMessage message have all the characters of emblem
                if( isDecryptMessageHasEmblem(emblemFrequency,decryptedMessageFrequency) ){
                    if( allies==null ){
                        allies=new LinkedHashSet<String>() ;
                    }
                    allies.add(inputLineStrArr[0]) ;
                }
                
            }

            // SPACE kingdom did not get enough allies support
            if (allies.size() < MIN_ALLIES_SUPPORT) {
                System.out.print("NONE");
            } else {
                String result="SPACE ";
                for (String ally : allies) {
                    result += ally + " ";
                }
                result =result.trim();
                System.out.print(result);
            }

        } catch (Exception ex) {
            System.out.println("Exception = " + ex.getMessage());
        }
        
    }

    /**
     * return the decrypted message of the secret message using the cipherKey
     * @param secretMessage
     * @param cipherKey
     * @return
     */
    public static String getDecryptedMessage(String secretMessage,int cipherKey){
        String decryptedMessage="";
        for (int i = 0; i < secretMessage.length(); i++) {
            int c = secretMessage.charAt(i) - cipherKey;

            if( c<65 ){
                c += ALPHABETS_COUNT ; 
            }
            decryptedMessage += (char)c;
        }
        return decryptedMessage ;
    }

    /**
     * return the hashmap in count of each character of string
     * @param message
     * @return
     */
    public static HashMap<Character,Integer> countCharacterFrequency(String message){
        HashMap<Character,Integer> freq=new HashMap<Character,Integer>();

        for(int i=0;i<message.length();i++){
           char c=message.charAt(i);
           if( freq.containsKey(c)){
               freq.put(c,freq.get(c)+1);
           }else{
               freq.put(c,1);
           }
        }
        return freq;
    }

    /**
     * check weather the drcrypted message have all the character of the secret message using count hashmap
     * @param emblemFrequency
     * @param decryptedMessageFrequency
     * @return
     */
    public static boolean isDecryptMessageHasEmblem(HashMap<Character,Integer> emblemFrequency,HashMap<Character,Integer> decryptedMessageFrequency){
        for(Character ch:emblemFrequency.keySet() ){
           if( !decryptedMessageFrequency.containsKey(ch) || emblemFrequency.get(ch)>decryptedMessageFrequency.get(ch) ){
                return false;
           }
        }
        return true;
    }
}