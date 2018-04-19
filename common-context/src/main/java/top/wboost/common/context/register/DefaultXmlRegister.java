package top.wboost.common.context.register;

public class DefaultXmlRegister extends AbstractXmlRegister {

    private XmlRegisterConfiguration xmlRegisterConfiguration;

    public DefaultXmlRegister(XmlRegisterConfiguration xmlRegisterConfiguration) {
        super();
        this.xmlRegisterConfiguration = xmlRegisterConfiguration;
    }

    @Override
    public XmlRegisterConfiguration getXmlRegisterConfiguration() {
        return xmlRegisterConfiguration;
    }

}
