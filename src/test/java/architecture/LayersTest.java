package architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "top.fsky.crawler", importOptions = { ImportOption.DoNotIncludeTests.class })
class LayersTest extends ArchitectureBaseTest {

    ImportOption ignoreTests = location -> {
        return !location.contains("/test/"); // ignore any URI to sources that contains '/test/'
    };
    
    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ignoreTests).importPackages("top.fsky.crawler");

    @Test
    void layer_dependencies_must_be_respected_include_the_tests() {
        layeredArchitecture()

                .layer("REST")
                .definedBy("top.fsky.crawler.adapter.inbound.rest..")
                .whereLayer("REST")
                .mayNotBeAccessedByAnyLayer()

                .layer("Rpc")
                .definedBy("top.fsky.crawler.adapter.inbound.rpc..")
                .whereLayer("Rpc")
                .mayNotBeAccessedByAnyLayer()
                
                .layer("Application")
                .definedBy("top.fsky.crawler.application..")
                .whereLayer("Application")
                .mayOnlyBeAccessedByLayers( "Rpc", "REST")

                .as("The layer dependencies should be respected, include tests)")
                .because("the DIP principle is recommended")
                .check(classes);
    }
}
