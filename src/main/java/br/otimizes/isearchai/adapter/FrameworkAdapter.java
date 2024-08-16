package br.otimizes.isearchai.adapter;

import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FrameworkAdapter<E extends FrameworkAdapter<E>> {

    protected String frameworkClazz;
    protected String frameworkPackage;
    protected Class javaClazz;
    private String projectPath;
    private String sourceClazzPath;
    private SourceRoot sourceRoot;
    private CompilationUnit sourceUnit;
    private CompilationUnit frameworkUnit;
    private String typeParameter;

    public FrameworkAdapter(String frameworkPackage, String frameworkClazz) {
        this.frameworkClazz = frameworkClazz;
        this.frameworkPackage = frameworkPackage;
    }

    public FrameworkAdapter(Class javaClazz) {
        this.javaClazz = javaClazz;
        this.frameworkClazz = javaClazz.getSimpleName();
        this.frameworkPackage = javaClazz.getPackage().getName();
        this.frameworkUnit = getClassOrInterface(this.frameworkPackage, this.frameworkClazz);
    }

    public CompilationUnit getClassOrInterface(String implementationPackage, String implementationClazz) {
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(MLElementAdapter.class).resolve("./"));
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
            writer.write(sourceUnit.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("foi");
        return (E) this;
    }

    public String getFullyQualifiedName() {
        return getSourceClassOrInterface().getFullyQualifiedName().get();
    }

    public String getSimpleQualifiedName() {
        return getSourceClassOrInterface().getNameAsString();
    }

    public E projectClazz(String projectPath, String clazzPath) {
        this.setProjectPath(projectPath);
        this.setSourceClazzPath(clazzPath);
        sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(MLElementAdapter.class).resolve(projectPath));

        sourceUnit = sourceRoot.parse("", clazzPath);

        getSourceClassOrInterface();
        return (E) this;
    }

    private ClassOrInterfaceDeclaration getMainClassOrInterfaceFrom(CompilationUnit sourceUnit) {
        return (ClassOrInterfaceDeclaration) sourceUnit.getTypes().stream().findFirst().get();
    }

    public boolean hasImport() {
        return sourceUnit.getImports().stream().anyMatch(importDeclaration -> importDeclaration.getNameAsString().equals(getIdentifier()));
    }

    public E implement() {
        return this.implement(null);
    }

    public E implement(FrameworkAdapter... qualifieds) {
        if (!hasImplementationClazz()) {
            ClassOrInterfaceType implementation = new ClassOrInterfaceType(null, frameworkClazz);
            setTypesOnImplementation(qualifieds, implementation);
            getSourceClassOrInterface().addImplementedType(implementation);
        }

        if (!hasImport()) {
            NodeList<ImportDeclaration> imports = sourceUnit.getImports();
            imports.add(new ImportDeclaration(new Name(getIdentifier()), false, false));
            sourceUnit.setImports(imports);
        }

        for (MethodDeclaration method : getFrameworkMethods()) {
            MethodDeclaration methodStub = getClonedMethod(method, qualifieds);
            boolean hasMethod = hasImplementationMethod(methodStub);
            if (!hasMethod) {
                methodStub.setBody(StaticJavaParser.parseBlock("{ throw new UnsupportedOperationException(\"Not implemented yet.\"); }"));
                getSourceClassOrInterface().addMember(methodStub);
            }
        }
        return (E) this;
    }

    private void setTypesOnImplementation(FrameworkAdapter[] qualifieds, ClassOrInterfaceType implementation) {
        if (qualifieds != null) {
            NodeList<Type> nodes = new NodeList<>();
            for (TypeParameter typeParameter : getFrameworkClassOrInterface().getTypeParameters()) {
                String nameAsString = typeParameter.getTypeBound().get(0).getNameAsString();
                FrameworkAdapter found = Arrays.stream(qualifieds).filter(nn -> nn.getFrameworkClazz().equals(nameAsString)).findFirst().orElse(null);
                if (found != null) {
                    sourceUnit.addImport(found.getFullyQualifiedName());
                    nodes.add(new TypeParameter(found.getSimpleQualifiedName()));
                }
            }
            implementation.setTypeArguments(nodes);
        }
    }

    public E extend(FrameworkAdapter... qualifieds) {
        if (!hasExtensionClazz()) {
            ClassOrInterfaceType implementation = new ClassOrInterfaceType(null, frameworkClazz);
            setTypesOnImplementation(qualifieds, implementation);
            getSourceClassOrInterface().addExtendedType(implementation);
        }

        if (!hasImport()) {
            NodeList<ImportDeclaration> imports = sourceUnit.getImports();
            imports.add(new ImportDeclaration(new Name(getIdentifier()), false, false));
            sourceUnit.setImports(imports);
        }

        for (MethodDeclaration method : getFrameworkMethods()) {
            if (!method.getBody().isPresent()) {
                MethodDeclaration methodStub = getClonedMethod(method, qualifieds);
                boolean hasMethod = hasImplementationMethod(methodStub);
                if (!hasMethod) {
                    methodStub.setBody(StaticJavaParser.parseBlock("{ throw new UnsupportedOperationException(\"Not implemented yet.\"); }"));
                    getSourceClassOrInterface().addMember(methodStub);
                }
            }
        }
        return (E) this;
    }

    private List<MethodDeclaration> getFrameworkMethods() {
        return getFrameworkClassOrInterface().getMethods();
    }

    MethodDeclaration getClonedMethod(MethodDeclaration method, FrameworkAdapter... qualifieds) {
        MethodDeclaration methodStub = method.clone();
        methodStub.setAbstract(false);
        methodStub.setPublic(true);
        if (methodStub.getNameAsString().contains("totalyEquals")) {
            System.out.println("");
        }


        NodeList<TypeParameter> typeParameters = methodStub.getTypeParameters();
        typeParameters.addAll(getFrameworkClassOrInterface().getTypeParameters());
        if (!typeParameters.isEmpty()) {
            HashMap<String, ClassOrInterfaceDeclaration> objectObjectHashMap = new HashMap<>();
            if (qualifieds == null) {
                for (Parameter parameter : methodStub.getParameters()) {
                    TypeParameter tt = method.getTypeParameters().stream().filter(t -> ((TypeParameter)t.getElementType()).getName().getId().equals(parameter.getType().toString())).findFirst().get();
                    parameter.setType(tt.getTypeBound().get(0));
                }
                methodStub.setTypeParameters(new NodeList<>());
                return methodStub;
            }
            methodStub.setTypeParameters(new NodeList<>());
            for (FrameworkAdapter qualified : qualifieds) {
                objectObjectHashMap.put(qualified.getTypeParameter(), qualified.getSourceClassOrInterface());
            }

            for (Parameter parameter : methodStub.getParameters()) {
                List<Node> childNodes = parameter.getType().getChildNodes();
                if (childNodes.isEmpty()) {
                    String type = parameter.getType().toString();
                    if (objectObjectHashMap.containsKey(type)) {
                        parameter.setType(new TypeParameter(objectObjectHashMap.get(type).getNameAsString()));
                    }
                } else {
                    for (Node childNode : childNodes) {
                        if (objectObjectHashMap.containsKey(childNode.toString()))
                            try {
                                if (childNode instanceof SimpleName)
                                    ((SimpleName) childNode).setIdentifier(objectObjectHashMap.get(childNode.toString()).getNameAsString());
                                else
                                    ((ClassOrInterfaceType) childNode).setName(objectObjectHashMap.get(childNode.toString()).getNameAsString());
                            } catch (RuntimeException e) {
                                System.out.println(e);
                            }
                        System.out.println(childNode);
                    }
                }

                System.out.println("aqui");
            }

        }
        return methodStub;
    }

    private ClassOrInterfaceDeclaration getFrameworkClassOrInterface() {
        return getMainClassOrInterfaceFrom(frameworkUnit);
    }

    private ClassOrInterfaceDeclaration getSourceClassOrInterface() {
        return getMainClassOrInterfaceFrom(sourceUnit);
    }

    public boolean hasImplementationClazz() {
        return getSourceClassOrInterface().getImplementedTypes().stream().anyMatch(type -> type.getName().getId().equals(frameworkClazz));
    }

    public boolean hasExtensionClazz() {
        return getSourceClassOrInterface().getExtendedTypes().stream().anyMatch(type -> type.getName().getId().equals(frameworkClazz));
    }

    public boolean hasImplementationMethod(MethodDeclaration method) {
        return getSourceClassOrInterface().getMethodsByName(method.getNameAsString()).stream().anyMatch(impMethod -> impMethod.getSignature().asString().equals(method.getSignature().asString()));
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
        return this.sourceUnit.toString();
    }


    public String getFrameworkClazz() {
        return frameworkClazz;
    }

    public void setFrameworkClazz(String frameworkClazz) {
        this.frameworkClazz = frameworkClazz;
    }

    public String getFrameworkPackage() {
        return frameworkPackage;
    }

    public void setFrameworkPackage(String frameworkPackage) {
        this.frameworkPackage = frameworkPackage;
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

    public String getSourceClazzPath() {
        return sourceClazzPath;
    }

    public void setSourceClazzPath(String sourceClazzPath) {
        this.sourceClazzPath = sourceClazzPath;
    }

    public String getFileName() {
        return projectPath + "/" + sourceClazzPath;
    }


    public String getIdentifier() {
        return frameworkPackage + "." + frameworkClazz;
    }

    public E replaceComments() {
        List<Comment> allContainedComments = getSourceClassOrInterface().getAllContainedComments();
        for (Comment comment : allContainedComments) {
            String value = comment.toString();
            if (value.contains("ISEARCHAI::ADD_METHOD")) {
                value = value.replace("ISEARCHAI::ADD_METHOD::", "");


                System.out.println("a");
            }
        }
        return (E) this;
    }

    public E withTypeParameter(String typeParameter) {
        this.typeParameter = typeParameter;
        return (E) this;
    }

    public String getTypeParameter() {
        return typeParameter;
    }
}
