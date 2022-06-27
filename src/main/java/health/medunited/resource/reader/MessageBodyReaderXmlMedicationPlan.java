package health.medunited.resource.reader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import health.medunited.bmp.MedikationsPlan;

public class MessageBodyReaderXmlMedicationPlan implements MessageBodyReader<MedikationsPlan> {

    private static final Logger log = Logger.getLogger(MessageBodyReaderXmlMedicationPlan.class.getName());

    public static JAXBContext jaxbContext;
    static {
        try {
            jaxbContext = JAXBContext.newInstance(MedikationsPlan.class);
        } catch (JAXBException e) {
            log.log(Level.SEVERE, "Could not create jaxbContext for MedikationsPlan", e);
        }
    } 

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.equals(MedikationsPlan.class) && mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE);
    }

    @Override
    public MedikationsPlan readFrom(Class<MedikationsPlan> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            return (MedikationsPlan) MessageBodyReaderXmlMedicationPlan.jaxbContext.createUnmarshaller().unmarshal(entityStream);
        } catch (JAXBException e) {
            log.log(Level.SEVERE, "Could not parse medication plan", e);
            throw new WebApplicationException(e);
        }
    }
    
}
