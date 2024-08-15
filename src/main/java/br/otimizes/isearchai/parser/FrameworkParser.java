package br.otimizes.isearchai.parser;

import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FrameworkParser<E extends FrameworkParser> {

    protected String implementationClazz;
    protected String implementationPackage;
    protected Class javaClazz;
    private String projectPath;
    private String clazzPath;
    private SourceRoot sourceRoot;
    private CompilationUnit projectUnit;
    private ClassOrInterfaceDeclaration projectClass;
    private CompilationUnit frameworkUnit;

    public FrameworkParser(String implementationPackage, String implementationClazz) {
        this.implementationClazz = implementationClazz;
        this.implementationPackage = implementationPackage;
    }

    public FrameworkParser(Class javaClazz) {
        this.javaClazz = javaClazz;
        this.implementationClazz = javaClazz.getSimpleName();
        this.implementationPackage = javaClazz.getPackage().getName();
        this.frameworkUnit = getClassOrInterface(this.implementationPackage, this.implementationClazz);
    }

    public CompilationUnit getClassOrInterface(String implementationPackage, String implementationClazz) {
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(MLElementParser.class).resolve("./"));
        try {
            return sourceRoot.tryToParse("",
                "src/main/java/" + implementationPackage.replace('.', '/') + "/" + implementationClazz + ".java").getResult().get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public E writeFile() {
        try {
            FileWriter writer = new FileWriter(getFileName());
            writer.write(projectUnit.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("foi");
        return (E) this;
    }

    public String getFullyQualifiedName() {
        return projectClass.getFullyQualifiedName().get();
    }

    public String getSimpleQualifiedName() {
        return projectClass.getNameAsString();
    }

    public E projectClazz(String projectPath, String clazzPath) {
        this.setProjectPath(projectPath);
        this.setClazzPath(clazzPath);
        sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(MLElementParser.class).resolve(projectPath));

        projectUnit = sourceRoot.parse("", clazzPath);

        projectClass = (ClassOrInterfaceDeclaration) projectUnit.getTypes().stream().findFirst().get();
        return (E) this;
    }

    public boolean hasImport() {
        return projectUnit.getImports().stream().anyMatch(importDeclaration -> importDeclaration.getNameAsString().equals(getIdentifier()));
    }

    public E implement() {
        return this.implement(null);
    }

    public E implement(FrameworkParser qualified) {
        if (!hasImplementationClazz()) {
            ClassOrInterfaceType implementation = new ClassOrInterfaceType(null, implementationClazz);
            if (qualified != null) {
                projectUnit.addImport(qualified.getFullyQualifiedName());
                implementation.setTypeArguments(new NodeList<>(new TypeParameter(qualified.getSimpleQualifiedName())));
            }
            projectClass.addImplementedType(implementation);
        }

        if (!hasImport()) {
            NodeList<ImportDeclaration> imports = projectUnit.getImports();
            imports.add(new ImportDeclaration(new Name(getIdentifier()), false, false));
            projectUnit.setImports(imports);
        }

        for (MethodDeclaration method : getFrameworkMethods()) {
            MethodDeclaration methodStub = getClonedMethod(method);
            boolean hasMethod = hasImplementationMethod(methodStub);
            if (!hasMethod) {
                methodStub.setBody(StaticJavaParser.parseBlock("{ throw new UnsupportedOperationException(\"Not implemented yet.\"); }"));
                projectClass.addMember(methodStub);
            }
        }
        return (E) this;
    }

    private List<MethodDeclaration> getFrameworkMethods() {
        return frameworkUnit.getTypes().stream().findFirst().get().getMethods();
    }

    MethodDeclaration getClonedMethod(MethodDeclaration method) {
        MethodDeclaration methodStub = method.clone();

        NodeList<TypeParameter> typeParameters = methodStub.getTypeParameters();
        if (!typeParameters.isEmpty()) {
            TypeParameter typeParameter = typeParameters.get(0);
            ClassOrInterfaceType classOrInterfaceType = typeParameter.getTypeBound().get(0);
            methodStub.setTypeParameters(new NodeList<>());

            List<Parameter> parameters = methodStub.getParameters().stream().filter(parameter -> parameter.getType().toString().equals(typeParameter.getNameAsString())).collect(Collectors.toList());
            for (Parameter parameter : parameters) {
                parameter.setType(classOrInterfaceType);
            }
            if (methodStub.getType().toString().equals(typeParameter.getNameAsString())) {
                methodStub.setType(classOrInterfaceType);
            }
        }
        return methodStub;
    }

    public boolean hasImplementationClazz() {
        return projectClass.getImplementedTypes().stream().anyMatch(type -> type.getName().getId().equals(implementationClazz));
    }

    public boolean hasImplementationMethod(MethodDeclaration method) {
        return projectClass.getMethodsByName(method.getNameAsString()).stream().anyMatch(impMethod -> impMethod.getSignature().asString().equals(method.getSignature().asString()));
    }

    public void listAllClasses() throws IOException {
        // Recursively parse all Java files in the modules directory
        List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse("");

        // Iterate over the parsed results
        for (ParseResult<CompilationUnit> parseResult : parseResults) {
            parseResult.ifSuccessful(compilationUnit -> {
                // Get all types (classes, interfaces, enums) declared in the compilation unit
                for (TypeDeclaration<?> type : compilationUnit.getTypes()) {
                    // Print the fully qualified name of the type
                    System.out.println(type.getFullyQualifiedName().orElse("[Anonymous]"));
                }
            });
        }
    }

    @Override
    public String toString() {
        return this.projectUnit.toString();
    }


    public String getImplementationClazz() {
        return implementationClazz;
    }

    public void setImplementationClazz(String implementationClazz) {
        this.implementationClazz = implementationClazz;
    }

    public String getImplementationPackage() {
        return implementationPackage;
    }

    public void setImplementationPackage(String implementationPackage) {
        this.implementationPackage = implementationPackage;
    }

    public ClassOrInterfaceDeclaration getProjectClass() {
        return projectClass;
    }

    public void setProjectClass(ClassOrInterfaceDeclaration projectClass) {
        this.projectClass = projectClass;
    }

    public Class getJavaClazz() {
        return javaClazz;
    }

    public void setJavaClazz(Class javaClazz) {
        this.javaClazz = javaClazz;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getClazzPath() {
        return clazzPath;
    }

    public void setClazzPath(String clazzPath) {
        this.clazzPath = clazzPath;
    }

    public String getFileName() {
        return projectPath + "/" + clazzPath;
    }


    public String getIdentifier() {
        return implementationPackage + "." + implementationClazz;
    }
}
