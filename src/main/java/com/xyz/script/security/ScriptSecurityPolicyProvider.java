package com.xyz.script.security;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mohammad Uzair
 */
public class ScriptSecurityPolicyProvider extends Policy {
    private final Set<URL> locations;

    public ScriptSecurityPolicyProvider() {
        try {
            locations = new HashSet<URL>();
            locations.add(new URL("file", "", "/groovy/shell"));
            locations.add(new URL("file", "", "/groovy/script"));
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public PermissionCollection getPermissions(CodeSource codeSource) {
        PermissionCollection perms = new Permissions();
        if (!locations.contains(codeSource.getLocation())) {
            perms.add(new AllPermission());
        }
        return perms;
    }
}