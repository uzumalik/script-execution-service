package com.xyz.script.service.provider;

import com.xyz.script.config.GroovyConfig;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.syntax.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * This class provides the customize compilation to run the scripts. It uses {@link SecureASTCustomizer}
 * to run groovy scripts in sandbox model.
 * @author Mohammad Uzair
 *
 */
public class CompilationProvider {

    public CompilationCustomizer createCompilationCustomizer(GroovyConfig groovyConfig) {
        GroovyConfig.SecurityConf securityConf = groovyConfig.getSecurity();
        final SecureASTCustomizer compilationCustomizer = new SecureASTCustomizer();
        compilationCustomizer.setImportsBlacklist(securityConf.getImportsBlacklist());
        compilationCustomizer.setStarImportsBlacklist(securityConf.getStarImportsBlacklist());
        compilationCustomizer.setIndirectImportCheckEnabled(true);
        compilationCustomizer.addExpressionCheckers(new CustomExpressionChecker(groovyConfig.getSecurity()));
        return compilationCustomizer;
    }

    /**
     * <p>This class provides custom check to protect host machines against illegal access of resources
     * and stops {@link groovy.lang.GroovyShell} to run any malicious code. It implements {@link SecureASTCustomizer.ExpressionChecker}
     * interface to provide custom checks and tries to avoid many hacks that a programmer can write in groovy scripts
     * to bypass System's security mechanisms. <p/>
     * <p>
     * Additional blacklisted methods, classes, imports etc. can be provided
     * in application.yaml under {@link groovy-config#security} tag <p/>
     */
    private final class CustomExpressionChecker implements SecureASTCustomizer.ExpressionChecker {

        private GroovyConfig.SecurityConf securityConf;
        private CustomExpressionChecker(GroovyConfig.SecurityConf securityConf){
            this.securityConf = securityConf;
        }

        @Override
        public boolean isAuthorized(final Expression expression) {
            if (expression instanceof MethodCallExpression) {
                final Expression objectExpression = ((MethodCallExpression) expression).getObjectExpression();
                if (objectExpression instanceof ClassExpression) {
                    if (securityConf.getIllegalClasses().contains(objectExpression.getType().getName())) {
                        return false;
                    }
                    if (securityConf.getIllegalMethods().containsKey(objectExpression.getType().getName())) {
                        if (securityConf.getIllegalMethods().get(objectExpression.getType().getName()).contains(((MethodCallExpression) expression).getMethodAsString())) {
                            return false;
                        }
                    }
                }
            }
            if (expression instanceof ConstructorCallExpression) {
                if (securityConf.getIllegalClasses().contains(expression.getType().getName())) {
                    return false;
                }
            }
            if (expression instanceof DeclarationExpression) {
                DeclarationExpression declarationExpression = (DeclarationExpression) expression;
                if (declarationExpression.getOperation().getType() == Types.ASSIGN) {
                    Expression rightExpression = declarationExpression.getRightExpression();
                    if (rightExpression instanceof ClassExpression) {
                        if (securityConf.getIllegalAssignmentClasses().contains(rightExpression.getType().getName())) {
                            return false;
                        }
                    }
                }
            }
            if (expression instanceof PropertyExpression) {
                PropertyExpression propertyExpression = (PropertyExpression) expression;
                if (securityConf.getIllegalProperties().containsKey(propertyExpression.getPropertyAsString())) {
                    Collection<String> classes = securityConf.getIllegalProperties().get(propertyExpression.getPropertyAsString());
                    if (classes.contains(propertyExpression.getObjectExpression().getType().getName())) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}

