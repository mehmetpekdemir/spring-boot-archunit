# ArchUnit

- ArchUnit is a free, simple and extensible library for checking the architecture of your Java code. 
- That is, ArchUnit can check dependencies between packages and classes, layers and slices, check for cyclic dependencies and more. 
- It does so by analyzing given Java bytecode, importing all classes into a Java code structure. 
- ArchUnit's main focus is to automatically test architecture and coding rules, using any plain Java unit testing framework.

### Import maven dependency

    <dependency>
        <groupId>com.tngtech.archunit</groupId>
        <artifactId>archunit-junit5</artifactId>
        <version>${archunit.version}</version>
        <scope>test</scope>
    </dependency>

### Create a test class

    class ArchunitApplicationTests {

          private JavaClasses importedClasses;

          @BeforeEach
          void setup() {
              importedClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.springboot.testing.archunit");
          }
     }

### Annotations checks

    @Test
    void field_injection_not_use_autowired_annotation() {
        noFields()
                .should().beAnnotatedWith(Autowired.class)
                .check(importedClasses);
    }

    @Test
    void repository_classes_should_have_spring_repository_annotation() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().beAnnotatedWith(Repository.class)
                .check(importedClasses);
    }

    @Test
    void service_classes_should_have_spring_service_annotation() {
        classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(Service.class)
                .check(importedClasses);
    }

### Package Dependency Checks

    @Test
    void services_and_repositories_should_not_depend_on_controller() {
        noClasses()
                .that().resideInAnyPackage("com.springboot.testing.archunit.service..")
                .or().resideInAnyPackage("com.springboot.testing.archunit.repository..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("com.springboot.testing.archunit.controller..")
                .because("Services and repositories should not depend on web layer")
                .check(importedClasses);
    }

### Class Dependency Checks

    @Test
    void service_classes_should_only_be_accessed_by_controller() {
        classes()
                .that().resideInAPackage("..service..")
                .should().onlyBeAccessed().byAnyPackage("..service..", "..controller..")
                .check(importedClasses);
    }

    @Test
    void repository_classes_should_only_be_accessed_by_service() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().onlyBeAccessed().byAnyPackage("..repository..", "..service..")
                .check(importedClasses);
    }

### Naming convention

    @Test
    void service_classes_should_be_named_blabla_service() {
        classes()
                .that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .check(importedClasses);
    }

    @Test
    void repository_classes_should_be_named_blabla_repository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().haveSimpleNameEndingWith("Repository")
                .check(importedClasses);
    }

    @Test
    void controller_classes_should_be_named_blabla_controller() {
        classes()
                .that().resideInAPackage("..controller..")
                .should().haveSimpleNameEndingWith("Controller")
                .check(importedClasses);
    }

### Layer Dependency Rules Test

    @Test
    void layered_architecture_should_be_respected() {
        layeredArchitecture()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")

                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                .check(importedClasses);
    }

    @Test
    void repositories_must_reside_in_repository_package() {
        classes().that()
                .haveNameMatching(".*Repository").should().resideInAPackage("..repository..")
                .as("Repositories should reside in a package '..repository..'")
                .check(importedClasses);
    }

    @Test
    void services_must_reside_in_service_package() {
        classes().that()
                .haveNameMatching(".*Service").should().resideInAPackage("..service..")
                .as("Services should reside in a package '..service..'")
                .check(importedClasses);
    }

    @Test
    void entity_classes_should_be_public() {
        classes()
                .that().resideInAPackage("..entity..")
                .should()
                .bePublic()
                .check(importedClasses);
    }

### License
- Distributed under the MIT License. See [LICENSE](LICENSE) for more information.
