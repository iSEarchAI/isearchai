package br.otimizes.isearchai.adapter;

import br.otimizes.isearchai.util.StringUtils;
import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
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
import java.util.concurrent.atomic.AtomicReference;

/**
 * The type Framework adapter.
 *
 * @param <E> the type parameter
 */
public class FrameworkAdapter<E extends FrameworkAdapter<E>> {

    /**
     * The Framework clazz.
     */
    protected String frameworkClazz;
    /**
     * The Framework package.
     */
    protected String frameworkPackage;
    /**
     * The Java clazz.
     */
    protected Class javaClazz;
    /**
     * The Project path.
     */
    protected String projectPath;
    /**
     * The Source clazz path.
     */
    protected String sourceClazzPath;
    /**
     * The Source root.
     */
    protected SourceRoot sourceRoot;
    /**
     * The Framework root.
     */
    protected SourceRoot frameworkRoot;
    /**
     * The Source unit.
     */
    protected CompilationUnit sourceUnit;
    /**
     * The Framework unit.
     */
    protected CompilationUnit frameworkUnit;
    /**
     * The Type parameter.
     */
    protected String typeParameter;

    /**
     * Instantiates a new Framework adapter.
     *
     * @param frameworkPackage the framework package
     * @param frameworkClazz   the framework clazz
     */
    public FrameworkAdapter(String frameworkPackage, String frameworkClazz) {
        this.frameworkClazz = frameworkClazz;
        this.frameworkPackage = frameworkPackage;
    }

    /**
     * Instantiates a new Framework adapter.
     *
     * @param javaClazz the java clazz
     */
    public FrameworkAdapter(Class javaClazz) {
        this.javaClazz = javaClazz;
        this.frameworkClazz = javaClazz.getSimpleName();
        this.frameworkPackage = javaClazz.getPackage().getName();
        this.frameworkUnit = getFrameworkUnit(this.frameworkPackage, this.frameworkClazz);
    }

    /**
     * Gets framework unit.
     *
     * @param implementationPackage the implementation package
     * @param implementationClazz   the implementation clazz
     * @return the framework unit
     */
    public CompilationUnit getFrameworkUnit(String implementationPackage, String implementationClazz) {
        frameworkRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(MLElementAdapter.class).resolve("./"));
        try {
            return frameworkRoot.tryToParse("",
                "src/main/java/" + implementationPackage.replace('.', '/') + "/" + implementationClazz + ".java").getResult().get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Write file e.
     *
     * @return the e
     */
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

    /**
     * Gets fully qualified name.
     *
     * @return the fully qualified name
     */
    public String getFullyQualifiedName() {
        return getMainClassFromSource().getFullyQualifiedName().get();
    }

    /**
     * Gets simple qualified name.
     *
     * @return the simple qualified name
     */
    public String getSimpleQualifiedName() {
        return getMainClassFromSource().getNameAsString();
    }

    /**
     * Sets source clazz.
     *
     * @param projectPath the project path
     * @param clazzPath   the clazz path
     * @return the source clazz
     */
    public E setSourceClazz(String projectPath, String clazzPath) {
        this.setProjectPath(projectPath);
        this.setSourceClazzPath(clazzPath);
        sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(MLElementAdapter.class).resolve(projectPath));

        sourceUnit = sourceRoot.parse("", clazzPath);

        getMainClassFromSource();
        return (E) this;
    }

    private ClassOrInterfaceDeclaration getMainClassOrInterfaceFrom(CompilationUnit sourceUnit) {
        return (ClassOrInterfaceDeclaration) sourceUnit.getTypes().stream().findFirst().get();
    }

    /**
     * Has import boolean.
     *
     * @return the boolean
     */
    public boolean hasImport() {
        return sourceUnit.getImports().stream().anyMatch(importDeclaration -> importDeclaration.getNameAsString().equals(getIdentifier()));
    }

    /**
     * Implement e.
     *
     * @return the e
     */
    public E implement() {
        return this.implement(null);
    }

    /**
     * Implement e.
     *
     * @param qualifieds the qualifieds
     * @return the e
     */
    public E implement(FrameworkAdapter... qualifieds) {
        if (!hasImplementationClazz()) {
            ClassOrInterfaceType implementation = new ClassOrInterfaceType(null, frameworkClazz);
            setTypesOnImplementation(qualifieds, implementation);
            getMainClassFromSource().addImplementedType(implementation);
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
                String comment = getMethodExampleBody(methodStub);
                methodStub.setBody(StaticJavaParser.parseBlock(comment));
                getMainClassFromSource().addMember(methodStub);
            }
        }
        return (E) this;
    }

    private static String getMethodExampleBody(MethodDeclaration methodStub) {
        String comment = methodStub.getComment().orElse(new BlockComment("")).getContent();
        String matchingLine = Arrays.stream(comment.split("\n"))
            .filter(line -> line.contains("ISEARCHAI::EXAMPLE::"))
            .findFirst()
            .orElse("")
            .trim();
        if (matchingLine.isEmpty()) {
            matchingLine = "{ throw new UnsupportedOperationException(\"Not implemented yet.\"); }";
        } else {
            matchingLine = matchingLine.replace("* ISEARCHAI::EXAMPLE::", "");
        }
        return matchingLine;
    }

    private void setTypesOnImplementation(FrameworkAdapter[] qualifieds, ClassOrInterfaceType implementation) {
        if (qualifieds != null) {
            NodeList<Type> nodes = new NodeList<>();
            for (TypeParameter typeParameter : getMainClassFromFramework().getTypeParameters()) {
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

    /**
     * Extend e.
     *
     * @param qualifieds the qualifieds
     * @return the e
     */
    public E extend(FrameworkAdapter... qualifieds) {
        if (!hasExtensionClazz()) {
            ClassOrInterfaceType implementation = new ClassOrInterfaceType(null, frameworkClazz);
            setTypesOnImplementation(qualifieds, implementation);
            getMainClassFromSource().addExtendedType(implementation);
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
                    String comment = getMethodExampleBody(methodStub);
                    methodStub.setBody(StaticJavaParser.parseBlock(comment));
                    getMainClassFromSource().addMember(methodStub);
                }
            }
        }
        return (E) this;
    }

    private List<MethodDeclaration> getFrameworkMethods() {
        return getMainClassFromFramework().getMethods();
    }

    /**
     * Gets cloned method.
     *
     * @param method     the method
     * @param qualifieds the qualifieds
     * @return the cloned method
     */
    MethodDeclaration getClonedMethod(MethodDeclaration method, FrameworkAdapter... qualifieds) {
        MethodDeclaration methodStub = method.clone();
        methodStub.setAbstract(false);
        methodStub.setPublic(true);
        if (methodStub.getNameAsString().contains("totalyEquals")) {
            System.out.println("");
        }


        NodeList<TypeParameter> typeParameters = methodStub.getTypeParameters();
        typeParameters.addAll(getMainClassFromFramework().getTypeParameters());
        if (!typeParameters.isEmpty()) {
            HashMap<String, ClassOrInterfaceDeclaration> objectObjectHashMap = new HashMap<>();
            if (qualifieds == null) {
                for (Parameter parameter : methodStub.getParameters()) {
                    TypeParameter tt = method.getTypeParameters().stream().filter(t -> ((TypeParameter) t.getElementType()).getName().getId().equals(parameter.getType().toString())).findFirst().get();
                    parameter.setType(tt.getTypeBound().get(0));
                }
                methodStub.setTypeParameters(new NodeList<>());
                return methodStub;
            }
            methodStub.setTypeParameters(new NodeList<>());
            for (FrameworkAdapter qualified : qualifieds) {
                objectObjectHashMap.put(qualified.getTypeParameter(), qualified.getMainClassFromSource());
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

    /**
     * Gets main class from framework.
     *
     * @return the main class from framework
     */
    public ClassOrInterfaceDeclaration getMainClassFromFramework() {
        return getMainClassOrInterfaceFrom(frameworkUnit);
    }

    /**
     * Gets main class from source.
     *
     * @return the main class from source
     */
    public ClassOrInterfaceDeclaration getMainClassFromSource() {
        return getMainClassOrInterfaceFrom(sourceUnit);
    }

    /**
     * Has implementation clazz boolean.
     *
     * @return the boolean
     */
    public boolean hasImplementationClazz() {
        return getMainClassFromSource().getImplementedTypes().stream().anyMatch(type -> type.getName().getId().equals(frameworkClazz));
    }

    /**
     * Has extension clazz boolean.
     *
     * @return the boolean
     */
    public boolean hasExtensionClazz() {
        return getMainClassFromSource().getExtendedTypes().stream().anyMatch(type -> type.getName().getId().equals(frameworkClazz));
    }

    /**
     * Has implementation method boolean.
     *
     * @param method the method
     * @return the boolean
     */
    public boolean hasImplementationMethod(MethodDeclaration method) {
        return getMainClassFromSource().getMethodsByName(method.getNameAsString()).stream().anyMatch(impMethod -> impMethod.getSignature().asString().equals(method.getSignature().asString()));
    }

    /**
     * List all classes source.
     *
     * @throws IOException the io exception
     */
    public void listAllClassesSource() throws IOException {
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


    /**
     * Add private field e.
     *
     * @param clazzToAdd the clazz to add
     * @param fieldName  the field name
     * @return the e
     */
    public E addPrivateField(Class clazzToAdd, String fieldName) {
        String fieldType = clazzToAdd.getSimpleName();
        ClassOrInterfaceDeclaration clazzFromSource = getMainClassFromSource();
        boolean hasField = clazzFromSource.getFields().stream().anyMatch(f -> f.getElementType().toString().equals(fieldType));
        if (!hasField) {
            clazzFromSource.addPrivateField(fieldType, fieldName);
            sourceUnit.addImport(clazzToAdd.getName());
        }
        addGetter(fieldName, fieldType)
            .addSetter(fieldName, fieldType);
        return (E) this;
    }

    /**
     * Add getter e.
     *
     * @param fieldName the field name
     * @param fieldType the field type
     * @return the e
     */
    public E addGetter(String fieldName, String fieldType) {
        String getter = "get" + StringUtils.camelcase(fieldName);
        ClassOrInterfaceDeclaration clazzFromSource = getMainClassFromSource();
        boolean hasGetter = clazzFromSource.getMethods().stream().anyMatch(f -> f.getNameAsString().equals(getter));
        if (!hasGetter) {
            clazzFromSource.addMethod(getter, Modifier.Keyword.PUBLIC)
                .setType(fieldType)
                .setBody(StaticJavaParser.parseBlock("{return this." + fieldName + ";}"));
        }
        return (E) this;
    }

    /**
     * Add setter e.
     *
     * @param fieldName the field name
     * @param fieldType the field type
     * @return the e
     */
    public E addSetter(String fieldName, String fieldType) {
        String setter = "set" + StringUtils.camelcase(fieldName);
        ClassOrInterfaceDeclaration clazzFromSource = getMainClassFromSource();
        boolean hasSetter = clazzFromSource.getMethods().stream().anyMatch(f -> f.getNameAsString().equals(setter));
        if (!hasSetter) {
            clazzFromSource.addMethod(setter, Modifier.Keyword.PUBLIC)
                .addParameter(new Parameter(new TypeParameter(fieldType), fieldName))
                .setBody(StaticJavaParser.parseBlock("{this." + fieldName + " = " + fieldName + ";}"));
        }
        return (E) this;
    }

    /**
     * Gets type from framework.
     *
     * @param clazz the clazz
     * @return the type from framework
     */
    public TypeDeclaration<?> getTypeFromFramework(Class clazz) {
        return getTypeFromFramework(clazz.getName());
    }

    /**
     * Gets type from framework.
     *
     * @param clazz the clazz
     * @return the type from framework
     */
    public TypeDeclaration<?> getTypeFromFramework(String clazz) {
        // Recursively parse all Java files in the modules directory
        AtomicReference<TypeDeclaration<?>> toReturn = new AtomicReference<>();
        List<ParseResult<CompilationUnit>> parseResults = null;
        try {
            parseResults = frameworkRoot.tryToParse("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Iterate over the parsed results
        for (ParseResult<CompilationUnit> parseResult : parseResults) {
            if (toReturn.get() != null)
                break;
            parseResult.ifSuccessful(compilationUnit -> {
                // Get all types (classes, interfaces, enums) declared in the compilation unit
                for (TypeDeclaration<?> type : compilationUnit.getTypes()) {
                    if (type.getFullyQualifiedName().get().equals(clazz)) {
                        toReturn.set(type);
                        break;
                    }
                    // Print the fully qualified name of the type
                    System.out.println(type.getFullyQualifiedName().orElse("[Anonymous]"));
                }
            });
        }
        return toReturn.get();
    }

    /**
     * List all classes framework.
     *
     * @throws IOException the io exception
     */
    public void listAllClassesFramework() throws IOException {
        // Recursively parse all Java files in the modules directory
        List<ParseResult<CompilationUnit>> parseResults = frameworkRoot.tryToParse("");

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


    /**
     * Gets framework clazz.
     *
     * @return the framework clazz
     */
    public String getFrameworkClazz() {
        return frameworkClazz;
    }

    /**
     * Sets framework clazz.
     *
     * @param frameworkClazz the framework clazz
     */
    public void setFrameworkClazz(String frameworkClazz) {
        this.frameworkClazz = frameworkClazz;
    }

    /**
     * Gets framework package.
     *
     * @return the framework package
     */
    public String getFrameworkPackage() {
        return frameworkPackage;
    }

    /**
     * Sets framework package.
     *
     * @param frameworkPackage the framework package
     */
    public void setFrameworkPackage(String frameworkPackage) {
        this.frameworkPackage = frameworkPackage;
    }

    /**
     * Gets java clazz.
     *
     * @return the java clazz
     */
    public Class getJavaClazz() {
        return javaClazz;
    }

    /**
     * Sets java clazz.
     *
     * @param javaClazz the java clazz
     */
    public void setJavaClazz(Class javaClazz) {
        this.javaClazz = javaClazz;
    }

    /**
     * Gets project path.
     *
     * @return the project path
     */
    public String getProjectPath() {
        return projectPath;
    }

    /**
     * Sets project path.
     *
     * @param projectPath the project path
     */
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    /**
     * Gets source clazz path.
     *
     * @return the source clazz path
     */
    public String getSourceClazzPath() {
        return sourceClazzPath;
    }

    /**
     * Sets source clazz path.
     *
     * @param sourceClazzPath the source clazz path
     */
    public void setSourceClazzPath(String sourceClazzPath) {
        this.sourceClazzPath = sourceClazzPath;
    }

    /**
     * Gets file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return projectPath + "/" + sourceClazzPath;
    }


    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return frameworkPackage + "." + frameworkClazz;
    }

    /**
     * Replace comments e.
     *
     * @return the e
     */
    public E replaceComments() {
        List<Comment> allContainedComments = getMainClassFromSource().getAllContainedComments();
        for (Comment comment : allContainedComments) {
            String value = comment.toString();
            if (value.contains("ISEARCHAI::ADD_METHOD")) {
                value = value.replace("ISEARCHAI::ADD_METHOD::", "");


                System.out.println("a");
            }
        }
        return (E) this;
    }

    /**
     * With type parameter e.
     *
     * @param typeParameter the type parameter
     * @return the e
     */
    public E withTypeParameter(String typeParameter) {
        this.typeParameter = typeParameter;
        return (E) this;
    }

    /**
     * Gets type parameter.
     *
     * @return the type parameter
     */
    public String getTypeParameter() {
        return typeParameter;
    }
}
