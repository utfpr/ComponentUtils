package br.edu.utfpr.cm.tsi.utils;

/**
 *
 * @author marcelo
 */
public class B extends BundleActionsImpl2 {
   
    public static String getStringSource(Object source,String key, Object... args) {
        return new B()._getStringSource(source, key, args);
    }
    
    
    public static String getString(String key, Object... args) {
        return new B()._getString(key, args);
    }


    private String _getString(String key, Object... args) {
        return getStringBundle(key, args);
    }
    private String _getStringSource(Object source,String key, Object... args) {
        return getStringSource(source, key, args);
    }

}
