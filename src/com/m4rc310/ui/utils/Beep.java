package com.m4rc310.ui.utils;



final class Beep {  
/** 
* @param dwFreq A freqüência do som, em hertz. Pode estar entre 37 
* a 32767 hertz, embora seus ouvidos provavelmente não ouçam acima de 14000. 
* @param dwDuration A duração do som em milissegundos. 
* <pre> 
Beep 
 
Generates simple tones on the speaker. The function is synchronous; it performs an alertable wait and does not return control to its caller until the sound finishes. 
 
 
BOOL Beep( 
  DWORD dwFreq, 
  DWORD dwDuration 
); 
Parameters 
dwFreq  
[in] The frequency of the sound, in hertz. This parameter must be in the range 37 through 32,767 (0x25 through 0x7FFF).  
Windows Me/98/95:  The Beep function ignores this parameter. 
dwDuration  
[in] The duration of the sound, in milliseconds.  
Windows Me/98/95:  The Beep function ignores this parameter.  
 
* </pre> 
*/  
    public native static void beep (int dwFreq, int dwDuration);  
    static {  
//        System.loadLibrary ("jnibeep");  
    }  
    /** 
     * Evita que esta classe seja instanciada ou herdada. 
     */  
    private Beep() {  
    }  
      
    public static void main(String[] args) {  
//        int freq = 1000;  
//        int freq = Integer.parseInt (args[0]);  
        int dur = 500;
        java.awt.Toolkit.getDefaultToolkit().beep(); 
//        Beep.beep (freq, dur);   
        // Meu alto-falante não responde para menos que 70 Hz, e não consigo  
        // ouvir mais que 7000 Hz. Estou ficando velho...  
    }  
}  