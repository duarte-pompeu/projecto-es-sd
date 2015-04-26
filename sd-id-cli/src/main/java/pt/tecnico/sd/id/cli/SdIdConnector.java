package pt.tecnico.sd.id.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;
import java.util.logging.Logger;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import example.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;

public class SdIdConnector {
	SDId port = null;
	
	public void connect(String uddiUrl, String wsName) throws WebServiceException, JAXRException, SdIdRemoteException {
		Logger logger = Logger.getLogger("pt.tecnico.ulisboa.essd.sd-id-cli");

		logger.info("Contacting UDDI at " + uddiUrl);
		UDDINaming uddiNaming = new UDDINaming(uddiUrl);

		logger.info("Looking for " + wsName);
		String endpointAddress = uddiNaming.lookup(wsName);

		if (endpointAddress == null) {
			logger.severe("Endpoint not found!");
			throw new SdIdRemoteException("endpoint address not found");
		} else {
			logger.info("Found " + endpointAddress);
		}

		SDId_Service service = new SDId_Service(); 
		this.port = service.getSDIdImplPort();

		logger.info("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);		
	}
	
	public SDId getPort() {
		return port;
	}
}
