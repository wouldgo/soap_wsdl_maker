		<wsdl:operation name="$OPERATION$">
			<soap:operation soapAction="http://$SERVICE_PROVIDER_URL$/$VERBO$_$OGGETTO$/interface/$OPERATION$" />
			<wsdl:input>
				<soap:body use="literal" />
			</wsdl:input>
			<wsdl:output>
				<soap:body use="literal" />
			</wsdl:output>
			<wsdl:fault name="TheException">
				<soap:fault use="literal" name="TheException"  />
			</wsdl:fault>
		</wsdl:operation>