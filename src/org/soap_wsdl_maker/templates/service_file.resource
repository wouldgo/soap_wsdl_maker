<?xml version="1.0" encoding="UTF-8" ?>
<wsdl:definitions name="$VERBO$_$OGGETTO$_service"
	targetNamespace="http://$SERVICE_PROVIDER_URL$/$VERBO$_$OGGETTO$/service"
	xmlns:tns="http://$SERVICE_PROVIDER_URL$/$VERBO$_$OGGETTO$/service"
	xmlns:int="http://$SERVICE_PROVIDER_URL$/$VERBO$_$OGGETTO$/interface"
	xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
	xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
>	
	<wsdl:import namespace="http://$SERVICE_PROVIDER_URL$/$VERBO$_$OGGETTO$/interface"
		location="$VERBO$_$OGGETTO$_interface.wsdl" />
	<wsdl:service name="$VERBO$_$OGGETTO$_service">
		<wsdl:port name="$VERBO$_$OGGETTO$_Port" binding="int:$VERBO$_$OGGETTO$_Binding" >
			<soap:address location="http://$SERVICE_HOST$/$VERBO$_$OGGETTO$_service/$VERBO$$OGGETTO$" />
		</wsdl:port>
	</wsdl:service>
</wsdl:definitions>
