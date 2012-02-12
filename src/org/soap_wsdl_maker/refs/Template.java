package org.soap_wsdl_maker.refs;

/**
 * @author wouldgo84@gmail.com
 * 
 * Template files name for generation. 
 */
public enum Template {
	SERVICE_TEMPLATE,
	INTERFACE_TEMPLATE,
	OPERATIONTYPES_TEMPLATE,
	OPERATIONBINDING_TEMPLATE,
	OPERATIONMESSAGES_TEMPLATE,
	OPERATIONPORT_TEMPLATE;
	
	public static String getResource(Template aTemplate) throws IllegalArgumentException {
		if (aTemplate == SERVICE_TEMPLATE) {
			return "org/soap_wsdl_maker/templates/service_file.resource";
		} else if (aTemplate == INTERFACE_TEMPLATE) {
			return "org/soap_wsdl_maker/templates/interface_file.resource";
		} else if (aTemplate == OPERATIONTYPES_TEMPLATE) {
			return "org/soap_wsdl_maker/templates/operationtypes_file.resource";
		} else if (aTemplate == OPERATIONBINDING_TEMPLATE) {
			return "org/soap_wsdl_maker/templates/operationbinding_file.resource";
		} else if (aTemplate == OPERATIONMESSAGES_TEMPLATE) {
			return "org/soap_wsdl_maker/templates/operationmessages_file.resource";
		} else if (aTemplate == OPERATIONPORT_TEMPLATE) {
			return "org/soap_wsdl_maker/templates/operationport_file.resource";
		} else {
			throw new IllegalArgumentException("Template "+aTemplate.toString()+" not supported.");
		}
	}
}
