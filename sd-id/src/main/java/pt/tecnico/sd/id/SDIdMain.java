package pt.tecnico.sd.id;

import javax.xml.ws.Endpoint;

import example.ws.uddi.*;
import javax.xml.registry.JAXRException;
import java.io.IOException;

//This class implements the server

public class SDIdMain {

    public static void main(String[] args) {
        // Check arguments
        if (args.length < 3) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL wsName wsURL%n", SDIdMain.class.getName());
            return;
        }

        String uddiURL = args[0];
        String name = args[1];
        String url = args[2];

        Endpoint endpoint = null;
        UDDINaming uddiNaming = null;

	printArt();
	
        try {
            endpoint = Endpoint.create(new SDIdImpl());

            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);

            // publish to UDDI
            System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
            uddiNaming = new UDDINaming(uddiURL);
            uddiNaming.rebind(name, url);

            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        } catch(JAXRException e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        }
        catch(IOException e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        }
        
        finally {
            try {
                if (endpoint != null) {
                    // stop endpoint
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", url);
                }
            } catch(Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
            try {
                if (uddiNaming != null) {
                    // delete from UDDI
                    uddiNaming.unbind(name);
                    System.out.printf("Deleted '%s' from UDDI%n", name);
                }
            } catch(Exception e) {
                System.out.printf("Caught exception when deleting: %s%n", e);
            }
        }

    }

    private static void printArt() {
	System.out.println("                        /\\_/\\____,");
	System.out.println("              ,___/\\_/\\ \\  ~     /");
	System.out.println("              \\     ~  \\ )   XXX");
	System.out.println("                XXX     /    /\\_/\\___,");
	System.out.println("                   \\o-o/-o-o/   ~    /");
	System.out.println("                    ) /     \\    XXX");
	System.out.println("                   _|    / \\ \\_/");
	System.out.println("   %     `      ,-/   _  \\_/   \\           *");
	System.out.println("    %&  %*    / (   /____,__|  )    %  `*");
	System.out.println("  &%&#&&#&    (  |_ (    )  \\) _|  &#& &`*");
	System.out.println(" &#&##&###&  _/ _)   \\   \\__/   (_  &##&#&&`");
	System.out.println("&###&#%%&##&(,-(,(,(,/      \\,),),)&##&%#&#&");
	System.out.println("======== KERBEROS == GUARDIAN OF HADES ========");
    }

}
