package org.soap_wsdl_maker.refs;

/**
 * @author wouldgo84@gmail.com
 * 
 * Placeholders involved in generation. 
 */
public enum PlaceHolder {
	VERB,
	OBJECT,
	PROVIDER_URL,
	SERVICE_HOST,
	VERB_LOWERCASE,
	OBJECT_LOWERCASE,
	OPERATION, 
	OPERATION_TYPES,
	OPERATION_MESSAGES,
	OPERATION_PORT,
	OPERATION_BINDING;
	
	public static String getPlaceHolder(PlaceHolder aPlaceHolder) throws IllegalArgumentException {
		if (aPlaceHolder == VERB) {
			return "\\$VERBO\\$";
		} else if (aPlaceHolder == OBJECT) {
			return "\\$OGGETTO\\$";
		} else if (aPlaceHolder == PROVIDER_URL) {
			return "\\$SERVICE_PROVIDER_URL\\$";
		} else if (aPlaceHolder == SERVICE_HOST) {
			return "\\$SERVICE_HOST\\$";
		} else if (aPlaceHolder == VERB_LOWERCASE) {
			return "\\$VERBOLC\\$";
		} else if (aPlaceHolder == OBJECT_LOWERCASE) {
			return "\\$OGGETTOLC\\$";
		} else if (aPlaceHolder == OPERATION) {
			return "\\$OPERATION\\$";
		} else if (aPlaceHolder == OPERATION_TYPES) {
			return "$OPERATIONTYPES$";
		} else if (aPlaceHolder == OPERATION_MESSAGES) {
			return "$OPERATIONMESSAGES$";
		} else if (aPlaceHolder == OPERATION_PORT) {
			return "$OPERATIONPORT$";
		} else if (aPlaceHolder == OPERATION_BINDING) {
			return "$OPERATIONBINDING$";
		} else {
			throw new IllegalArgumentException("PlaceHolder "+aPlaceHolder+" not supported.");
		}
	}
}
