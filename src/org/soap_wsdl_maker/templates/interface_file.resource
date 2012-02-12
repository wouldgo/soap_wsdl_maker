<?xml version="1.0" encoding="UTF-8" ?>
<wsdl:definitions name="$VERBO$_$OGGETTO$_interface"
	targetNamespace="http://$SERVICE_PROVIDER_URL$/$VERBO$_$OGGETTO$/interface"
	xmlns:int="http://$SERVICE_PROVIDER_URL$/$VERBO$_$OGGETTO$/interface"
	xmlns:common="http://dtgov.$SERVICE_PROVIDER_URL$/Common/Schema"

	xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
	xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
>
	<wsdl:types>
		<xsd:schema targetNamespace="http://$SERVICE_PROVIDER_URL$/$VERBO$_$OGGETTO$/interface">
			<xsd:import schemaLocation="<!-- INSERT HERE YOUR IMPORTS (*.xsd) -->" namespace="http://dtgov.$SERVICE_PROVIDER_URL$/Common/Schema" />

$OPERATIONTYPES$
		</xsd:schema>
	</wsdl:types>
	
$OPERATIONMESSAGES$	
	
	<wsdl:message name="TheException">
		<wsdl:part name="TheException" element="common:<!-- Excepion Type -->" />
	</wsdl:message>
	
	<wsdl:portType name="$VERBO$_$OGGETTO$_PortType">
	
$OPERATIONPORT$	
	
	</wsdl:portType>
		
	<wsdl:binding name="$VERBO$_$OGGETTO$_Binding" type="int:$VERBO$_$OGGETTO$_PortType">
		<soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http" />
		
$OPERATIONBINDING$		

	</wsdl:binding>
</wsdl:definitions>
