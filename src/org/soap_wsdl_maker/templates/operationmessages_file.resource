	<wsdl:message name="$OPERATION$Request">
		<wsdl:part name="parameters" element="int:$OPERATION$" />
	</wsdl:message>
	<wsdl:message name="$OPERATION$Response">
		<wsdl:part name="parameters" element="int:Output$OPERATION$" />
	</wsdl:message>