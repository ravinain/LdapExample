import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Ravi Nain on 4/22/2017.
 */
public class LdapMain {

    private DirContext getContext() throws NamingException {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = LdapMain.class.getClassLoader().getResourceAsStream("ldap-config.properties");
            prop.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new InitialDirContext(prop);
    }

    private void createLDAPObject(final DirContext context, final String name) throws NamingException {
        final Attributes attributes = new BasicAttributes();

        Attribute attribute = new BasicAttribute("objectClass");
        attribute.add("inetOrgPerson");
        attributes.put(attribute);

        Attribute sn = new BasicAttribute("sn");
        sn.add("Singh");
        attributes.put(sn);

        Attribute cn = new BasicAttribute("cn");
        cn.add("Sagar");
        attributes.put(cn);

        Attribute telephoneNumber = new BasicAttribute("telephoneNumber");
        telephoneNumber.add("9812981298");
        attributes.put(telephoneNumber);

        context.createSubcontext(name, attributes);
    }

    private void createAttribute(final DirContext context, final String name, final String attrName, final Object attrValue) throws NamingException {
        Attribute attribute = new BasicAttribute(attrName, attrValue);
        ModificationItem []items = new ModificationItem[1];
        items[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute);
        context.modifyAttributes(name, items);
    }

    private void viewAttribute(final DirContext context, final String name, final String attrName) throws NamingException {
        Attributes attributes = context.getAttributes(name);
        System.out.printf("Attribute Name: %s, Attribute Value: %s", attrName, attributes.get(attrName).get());
    }

    private void updateAttribute(final DirContext context, final String name, final String attrName, final String attrValue) throws NamingException {
        Attribute attribute = new BasicAttribute(attrName, attrValue);
        ModificationItem []items = new ModificationItem[1];
        items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);
        context.modifyAttributes(name, items);
    }

    private void removeAttribute(final DirContext context, final String name, final String attrName) throws NamingException {
        Attribute attribute = new BasicAttribute(attrName);
        ModificationItem []items = new ModificationItem[1];
        items[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attribute);
        context.modifyAttributes(name, items);
    }

    private void removeLDAPObject(final DirContext context, final String name) throws NamingException {
        context.destroySubcontext(name);
    }

    public void run() {
        try {
            DirContext context = getContext();
            String name = "employeeNumber=00001,ou=system";
            createLDAPObject(context, name);
            createAttribute(context, name, "displayName", "SS");
            viewAttribute(context, name, "displayName");
            updateAttribute(context, name, "displayName", "AD");
            //removeAttribute(context, name, "displayName");
            //removeLDAPObject(context, name);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LdapMain().run();
    }
}
