		<wsdl:operation name="$OPERATION$">
			<wsdl:input  message="int:$OPERATION$Request" />
			<wsdl:output message="int:$OPERATION$Response" />
			<wsdl:fault  name="<!-- Exception Name -->" message="<!-- Exception Type -->" />
		</wsdl:operation>