package io.redis.demos.autocomplete.schemas;

abstract public class  KeysPrefix {

    public final static String DOC_PREFIX = "ms:docs:";
    public final static String SUGGEST_PREFIX = "ms:search:suggest:";
    public final static String SEARCH_INDEX_PREFIX = "ms:search:index:";


    public String getKeyForDoc(String item, Object id) {
        return DOC_PREFIX.concat(item).concat(":").concat(id.toString());
    }

}
