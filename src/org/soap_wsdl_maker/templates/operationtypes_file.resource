			<xsd:complexType name="WrapperInput$OPERATION$Type">
				<xsd:sequence>
					<xsd:element name="<!-- INPUT OBJECT -->" minOccurs="1" maxOccurs="1" type="<!-- INPUT OBJECT -->" />
				</xsd:sequence>
			</xsd:complexType>
			
			<xsd:complexType name="WrapperOutput$OPERATION$Type">
				<xsd:sequence>
					<xsd:element name="<!-- INPUT OBJECT -->" minOccurs="1" maxOccurs="1" type="<!-- INPUT OBJECT -->" />
				</xsd:sequence>
			</xsd:complexType>

			<xsd:element name="$OPERATION$" type ="int:WrapperInput$OPERATION$Type" />
			<xsd:element name="Output$OPERATION$" type ="int:WrapperOutput$OPERATION$Type" />
