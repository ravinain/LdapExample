import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Ravi Nain on 4/22/2017.
 */
public class LdapMain2 {

    private DirContext getContext() throws NamingException {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = LdapMain2.class.getClassLoader().getResourceAsStream("ldap-config2.properties");
            prop.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new InitialDirContext(prop);
    }

    private SearchControls getSearchControls() {
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        return controls;
    }

    private void displayEmployeeInfo(final String employeeNumber, final DirContext context, final SearchControls controls) throws NamingException {
        String searchFilter = "(&(objectClass=person))";
        String name="employeeNumber=00001,ou=people";
        NamingEnumeration<SearchResult> results = context.search(name, searchFilter, controls);
        while (results.hasMore()) {
            System.out.println("Found object");
            SearchResult result = results.next();
            System.out.println(result.getAttributes());
        }
    }

    public void run() {
        try {
            DirContext context = getContext();
            displayEmployeeInfo("00001", context, getSearchControls());
//            String name = "employeeNumber=00001,ou=people";
            /*createLDAPObject(context, name);
            createAttribute(context, name, "displayName", "SS");
            viewAttribute(context, name, "displayName");
            updateAttribute(context, name, "displayName", "AD");*/
            //removeAttribute(context, name, "displayName");
            //removeLDAPObject(context, name);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LdapMain2().run();
    }
}
